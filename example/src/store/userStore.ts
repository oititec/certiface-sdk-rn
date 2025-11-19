import { LivenessProvider } from '@oiti/rn-sdk';
import { create } from 'zustand';

interface UserData {
  cpf: string;
  nome: string;
  nascimento: string;
}

type LivenessProvider = 'IPROOV' | 'FACETEC';

interface UserStore {
  userData: UserData;
  appKey: string;
  livenessProvider: LivenessProvider;
  setUserData: (data: Partial<UserData>) => void;
  setAppKey: (key: string) => void;
  setProvider: (provider: LivenessProvider) => void;
  generateCredential: () => Promise<any>;
  generateAppKey: () => Promise<string>;
  setLivenessProvider: (provider: LivenessProvider) => void;
}

export const useUserStore = create<UserStore>((set, get) => ({
  userData: {
    cpf: '08670833956',
    nome: 'Teste Mobile Homolog',
    nascimento: '08/10/1996',
  },
  appKey: '',
  livenessProvider: LivenessProvider.FACETEC,
  setUserData: (data) =>
    set((state) => ({
      userData: { ...state.userData, ...data },
    })),

  setAppKey: (key) => set({ appKey: key }),

  setProvider: (provider) => set({ provider }),

  generateCredential: async () => {
    const { provider } = get();
    
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');

    const facetecProvider = get().livenessProvider === LivenessProvider.FACETEC;
    const user = facetecProvider ? 'mobile.hml.apiglobal' : 'mobile.demo.app';
    const pass = facetecProvider
      ? 'c951c17decd9e06772853e23a35056bf'
      : 'ddc0ba9a6a5ab1681108a7e34c914207';
    
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
    const { userData, provider, generateCredential } = get();

    const credential = await generateCredential();

    const credentials = {
      IPROOV: {
        user: 'mobile.demo.app',
      },
      FACETEC: {
        user: 'mobile.hml.apiglobal',
      },
    };

    const { user } = credentials[provider];

    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');

    const user =
      get().livenessProvider === LivenessProvider.FACETEC
        ? 'mobile.hml.apiglobal'
        : 'mobile.demo.app';
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
}));
