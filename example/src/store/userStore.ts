import { LivenessProvider } from '@oiti/rn-sdk';
import { create } from 'zustand';

interface UserData {
  cpf: string;
  nome: string;
  nascimento: string;
}

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

  setProvider: (provider) => set({ livenessProvider: provider }),

  generateCredential: async () => {
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');

    const facetecProvider = get().livenessProvider === LivenessProvider.FACETEC;
    const user = facetecProvider ? 'mobile.hml.apiglobal' : 'mobile.demo.app';
    const pass = facetecProvider
      ? '48667f589a0a56ab74acb5f7da548462'
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
    const { userData, livenessProvider, generateCredential } = get();

    const credential = await generateCredential();

    const credentials = {
      IPROOV: {
        user: 'mobile.demo.app',
      },
      FACETEC: {
        user: 'mobile.hml.apiglobal',
      },
    };

    const { user } = credentials[livenessProvider];

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
}));
