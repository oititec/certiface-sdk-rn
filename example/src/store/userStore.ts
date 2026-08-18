import { Environment, LivenessProvider } from '@certiface/sdk';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { create } from 'zustand';
import { createJSONStorage, persist } from 'zustand/middleware';

export type FeatureType = 'IPROOV' | 'FACETEC';

const MAX_RESULTS = 50;

interface UserData {
  cpf: string;
  nome: string;
  nascimento: string;
}

interface UserStore {
  userData: UserData;
  appKey: string;
  selectedFeature: FeatureType;
  livenessProvider: LivenessProvider;
  environment: Environment;
  isCustomThemeEnabled: boolean;
  results: string[];
  setUserData: (data: Partial<UserData>) => void;
  setAppKey: (key: string) => void;
  setSelectedFeature: (feature: FeatureType) => void;
  setProvider: (provider: LivenessProvider) => void;
  setEnvironment: (environment: Environment) => void;
  setCustomThemeEnabled: (enabled: boolean) => void;
  addResult: (result: string) => void;
  clearResults: () => void;
  canRunLiveness: () => boolean;
  generateCredential: () => Promise<any>;
  generateAppKey: () => Promise<string>;
  setLivenessProvider: (provider: LivenessProvider) => void;
}

export const useUserStore = create<UserStore>()(
  persist(
    (set, get) => ({
      userData: {
        cpf: '08670833956',
        nome: 'Teste Mobile Homolog',
        nascimento: '08/10/1996',
      },
      appKey: '',
      selectedFeature: 'FACETEC',
      livenessProvider: LivenessProvider.FACETEC,
      environment: Environment.HML,
      isCustomThemeEnabled: false,
      results: [],
      setUserData: (data) =>
        set((state) => ({
          userData: { ...state.userData, ...data },
        })),
      setAppKey: (key) => set({ appKey: key }),
      setSelectedFeature: (feature) =>
        set({
          selectedFeature: feature,
          livenessProvider:
            feature === 'IPROOV'
              ? LivenessProvider.IPROOV
              : LivenessProvider.FACETEC,
        }),
      setProvider: (provider) => set({ livenessProvider: provider }),
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
      canRunLiveness: () => get().appKey.trim().length > 0,
      generateCredential: async () => {
        const myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');
        const { selectedFeature } = get();
        const isIProov = selectedFeature === 'IPROOV';
        const user = isIProov ? 'mobile.demo.app' : 'mobile.hml.apiglobal';
        const pass = isIProov
          ? 'ddc0ba9a6a5ab1681108a7e34c914207'
          : '48667f589a0a56ab74acb5f7da548462';
        const urlencoded = new URLSearchParams();
        urlencoded.append('user', user);
        urlencoded.append('pass', pass);
        const requestOptions = {
          method: 'POST',
          headers: myHeaders,
          body: urlencoded.toString(),
        };
        const response = await fetch(
          'https://comercial.certiface.com.br/facecaptcha/service/captcha/credencial',
          requestOptions
        );
        const result = await response.text();
        return JSON.parse(result);
      },
      generateAppKey: async () => {
        const { userData, selectedFeature, generateCredential } = get();
        const credential = await generateCredential();
        const credentials = {
          IPROOV: { user: 'mobile.demo.app' },
          FACETEC: { user: 'mobile.hml.apiglobal' },
        };
        const { user } = credentials[selectedFeature];
        const myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');
        const urlencoded = new URLSearchParams();
        urlencoded.append('user', user);
        urlencoded.append('token', JSON.stringify(credential));
        urlencoded.append('cpf', userData.cpf);
        urlencoded.append('nome', userData.nome);
        urlencoded.append('nascimento', userData.nascimento);
        const requestOptions = {
          method: 'POST',
          headers: myHeaders,
          body: urlencoded.toString(),
        };
        const response = await fetch(
          'https://comercial.certiface.com.br/facecaptcha/service/captcha/appkey',
          requestOptions
        );
        const result = await response.text();
        const parsedResult = JSON.parse(result);
        if (parsedResult.appkey) {
          set({ appKey: parsedResult.appkey });
          return parsedResult.appkey;
        }
        throw new Error('Failed to generate app key');
      },
      setLivenessProvider: (provider) => set({ livenessProvider: provider }),
    }),
    {
      name: 'certiface-example-store',
      storage: createJSONStorage(() => AsyncStorage),
      partialize: (state) => ({
        userData: state.userData,
        appKey: state.appKey,
        selectedFeature: state.selectedFeature,
        livenessProvider: state.livenessProvider,
        environment: state.environment,
        isCustomThemeEnabled: state.isCustomThemeEnabled,
        results: state.results,
      }),
    }
  )
);
