type FeatureType = 'IPROOV' | 'FACETEC';

type CredentialPair = { user: string; pass: string };

export const apiCredentials: Record<FeatureType, CredentialPair> = {
  IPROOV: { user: '', pass: '' },
  FACETEC: { user: '', pass: '' },
};

export function getApiCredentials(feature: FeatureType): CredentialPair {
  const creds = apiCredentials[feature];
  if (!creds.user || !creds.pass) {
    throw new Error(
      'Preencha example/src/config/credentials.ts com credenciais de HML fornecidas pela Certiface.'
    );
  }
  return creds;
}
