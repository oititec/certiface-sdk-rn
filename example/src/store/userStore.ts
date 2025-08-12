import { create } from 'zustand';

interface UserData {
  cpf: string;
  nome: string;
  nascimento: string;
}

interface UserStore {
  userData: UserData;
  appKey: string;
  setUserData: (data: Partial<UserData>) => void;
  setAppKey: (key: string) => void;
  generateCredential: () => Promise<any>;
  generateAppKey: () => Promise<string>;
}

export const useUserStore = create<UserStore>((set, get) => ({
  userData: {
    cpf: '08670833956',
    nome: 'Teste Mobile Homolog',
    nascimento: '08/10/1996',
  },
  appKey: '',

  setUserData: (data) =>
    set((state) => ({
      userData: { ...state.userData, ...data },
    })),

  setAppKey: (key) => set({ appKey: key }),

  generateCredential: async () => {
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');

    const urlencoded = new URLSearchParams();
    urlencoded.append('user', 'mobile.demo.app');
    urlencoded.append('pass', 'f2b9c75dc5d721aa65ec652e98e6d35e');

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
    const { userData, generateCredential } = get();

    const credential = await generateCredential();

    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/x-www-form-urlencoded');

    const urlencoded = new URLSearchParams();
    urlencoded.append('user', 'mobile.demo.app');
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
}));
