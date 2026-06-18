import { Environment, LivenessProvider } from '@certiface/sdk';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { create } from 'zustand';
import { createJSONStorage, persist } from 'zustand/middleware';
import { getApiCredentials } from '../config/credentials';

export type FeatureType = 'IPROOV' | 'FACETEC';

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
  generateCredential: () => Promise<any>;
  generateAppKey: () => Promise<string>;
  setLivenessProvider: (provider: LivenessProvider) => void;
}

export const useUserStore = create<UserStore>()(
  persist(
    (set, get) => ({
      userData: {
        cpf: '00000000000',
        nome: 'Usuário Exemplo',
        nascimento: '01/01/1990',
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
        set((state) => ({
          results: [
            ...state.results,
            `${new Date().toLocaleTimeString()}: ${result}`,
          ],
        })),
      clearResults: () => set({ results: [] }),
      generateCredential: async () => {
        const myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');
        const { selectedFeature } = get();
        const { user, pass } = getApiCredentials(selectedFeature);
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
        const { user } = getApiCredentials(selectedFeature);
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
