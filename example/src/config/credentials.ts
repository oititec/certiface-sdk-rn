type FeatureType = 'IPROOV' | 'SAAS';

type CredentialPair = { user: string; pass: string };

export const apiCredentials: Record<'IPROOV', CredentialPair> = {
  IPROOV: { user: '', pass: '' },
};

export function getApiCredentials(feature: FeatureType): CredentialPair {
  if (feature !== 'IPROOV') {
    throw new Error('Credenciais de AppKey só se aplicam ao fluxo IProov.');
  }
  const creds = apiCredentials.IPROOV;
  if (!creds.user || !creds.pass) {
    throw new Error(
      'Preencha example/src/config/credentials.ts com credenciais de HML fornecidas pela Certiface.'
    );
  }
  return creds;
}
