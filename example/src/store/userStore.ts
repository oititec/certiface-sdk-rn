import {
  Environment,
  type SaasProvider,
} from '@certiface/sdk';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { create } from 'zustand';
import { createJSONStorage, persist } from 'zustand/middleware';
import { getApiCredentials } from '../config/credentials';
import { cancelJourneyToken, createJourneyToken } from '../services/saasApi';

export type FeatureType = 'IPROOV' | 'SAAS';

const MAX_RESULTS = 50;

interface UserData {
  cpf: string;
  nome: string;
  nascimento: string;
}

interface SaasOperatorData {
  login: string;
  password: string;
}

interface UserStore {
  userData: UserData;
  saasOperator: SaasOperatorData;
  appKey: string;
  journeyToken: string;
  selectedFeature: FeatureType;
  saasProvider: SaasProvider;
  environment: Environment;
  isCustomThemeEnabled: boolean;
  results: string[];
  setUserData: (data: Partial<UserData>) => void;
  setSaasOperator: (data: Partial<SaasOperatorData>) => void;
  setAppKey: (key: string) => void;
  setJourneyToken: (token: string) => void;
  setSelectedFeature: (feature: FeatureType) => void;
  setSaasProvider: (provider: SaasProvider) => void;
  setEnvironment: (environment: Environment) => void;
  setCustomThemeEnabled: (enabled: boolean) => void;
  addResult: (result: string) => void;
  clearResults: () => void;
  generateAppKey: () => Promise<string>;
  generateJourneyToken: () => Promise<string>;
  clearSaasSession: () => Promise<void>;
  canRunLiveness: () => boolean;
}

export const useUserStore = create<UserStore>()(
  persist(
    (set, get) => ({
      userData: {
        cpf: '08670833956',
        nome: 'Teste Mobile Homolog',
        nascimento: '08/10/1996',
      },
      saasOperator: {
        login: '',
        password: '',
      },
      appKey: '',
      journeyToken: '',
      selectedFeature: 'SAAS',
      saasProvider: 'FORTFACE',
      environment: Environment.HML,
      isCustomThemeEnabled: false,
      results: [],
      setUserData: (data) =>
        set((state) => ({
          userData: { ...state.userData, ...data },
        })),
      setSaasOperator: (data) =>
        set((state) => ({
          saasOperator: { ...state.saasOperator, ...data },
        })),
      setAppKey: (key) => set({ appKey: key }),
      setJourneyToken: (token) => set({ journeyToken: token }),
      setSelectedFeature: (feature) =>
        set({
          selectedFeature: feature,
          ...(feature === 'SAAS' ? { appKey: '' } : { journeyToken: '' }),
        }),
      setSaasProvider: (provider) =>
        set({
          saasProvider: provider,
          journeyToken: '',
        }),
      setEnvironment: (environment) => set({ environment }),
      setCustomThemeEnabled: (enabled) =>
        set({ isCustomThemeEnabled: enabled }),
      addResult: (result) =>
        set((state) => {
          const next = [
            ...state.results,
            `${new Date().toLocaleTimeString()}: ${result}`,
          ];
          return {
            results: next.length > MAX_RESULTS ? next.slice(-MAX_RESULTS) : next,
          };
        }),
      clearResults: () => set({ results: [] }),
      canRunLiveness: () => {
        const { selectedFeature, appKey, journeyToken } = get();
        return selectedFeature === 'SAAS'
          ? journeyToken.trim().length > 0
          : appKey.trim().length > 0;
      },
      generateAppKey: async () => {
        const { userData } = get();
        const { user, pass } = getApiCredentials('IPROOV');
        const myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');

        const credentialBody = new URLSearchParams();
        credentialBody.append('user', user);
        credentialBody.append('pass', pass);

        const credentialResponse = await fetch(
          'https://comercial.certiface.com.br/facecaptcha/service/captcha/credencial',
          {
            method: 'POST',
            headers: myHeaders,
            body: credentialBody.toString(),
          }
        );
        const credential = JSON.parse(await credentialResponse.text());

        const appKeyBody = new URLSearchParams();
        appKeyBody.append('user', user);
        appKeyBody.append('token', JSON.stringify(credential));
        appKeyBody.append('cpf', userData.cpf);
        appKeyBody.append('nome', userData.nome);
        appKeyBody.append('nascimento', userData.nascimento);

        const response = await fetch(
          'https://comercial.certiface.com.br/facecaptcha/service/captcha/appkey',
          {
            method: 'POST',
            headers: myHeaders,
            body: appKeyBody.toString(),
          }
        );
        const parsedResult = JSON.parse(await response.text());
        if (parsedResult.appkey) {
          set({ appKey: parsedResult.appkey });
          return parsedResult.appkey;
        }
        throw new Error('Failed to generate app key');
      },
      generateJourneyToken: async () => {
        const { userData, saasOperator, saasProvider } = get();
        if (!saasOperator.login.trim() || !saasOperator.password.trim()) {
          throw new Error('Informe login e senha do operador SAAS');
        }
        if (!userData.nome.trim() || userData.nascimento.length !== 10) {
          throw new Error('Informe nome completo e data de nascimento');
        }
        if (userData.cpf.replace(/\D/g, '').length !== 11) {
          throw new Error('Informe um CPF válido');
        }

        const token = await createJourneyToken(
          {
            operatorLogin: saasOperator.login,
            operatorPassword: saasOperator.password,
            birthDate: userData.nascimento,
            fullName: userData.nome,
          },
          userData.cpf,
          saasProvider
        );
        set({ journeyToken: token });
        return token;
      },
      clearSaasSession: async () => {
        const { saasOperator, journeyToken } = get();
        if (
          journeyToken &&
          saasOperator.login.trim() &&
          saasOperator.password.trim()
        ) {
          try {
            await cancelJourneyToken(
              saasOperator.login,
              saasOperator.password,
              journeyToken
            );
          } catch {}
        }
        set({ journeyToken: '' });
      },
    }),
    {
      name: 'certiface-example-store',
      storage: createJSONStorage(() => AsyncStorage),
      version: 3,
      migrate: (persisted: any) => {
        if (!persisted || typeof persisted !== 'object') {
          return persisted;
        }
        const { livenessProvider: _removed, ...rest } = persisted;
        const legacyFeature = rest.selectedFeature;
        if (legacyFeature === 'FACETEC' || legacyFeature === 'FORTFACE') {
          return {
            ...rest,
            selectedFeature: 'SAAS',
            saasProvider: legacyFeature === 'FACETEC' ? 'FACETEC' : 'FORTFACE',
            journeyToken: rest.journeyToken ?? '',
            saasOperator: rest.saasOperator ?? { login: '', password: '' },
          };
        }
        return {
          ...rest,
          journeyToken: rest.journeyToken ?? '',
          saasOperator: rest.saasOperator ?? { login: '', password: '' },
          saasProvider: rest.saasProvider ?? 'FORTFACE',
        };
      },
      partialize: (state) => ({
        userData: state.userData,
        saasOperator: state.saasOperator,
        appKey: state.appKey,
        journeyToken: state.journeyToken,
        selectedFeature: state.selectedFeature,
        saasProvider: state.saasProvider,
        environment: state.environment,
        isCustomThemeEnabled: state.isCustomThemeEnabled,
        results: state.results,
      }),
    }
  )
);
