import type { SaasProvider } from '@certiface/sdk';
import { md5 } from '../utils/md5';

const SAAS_BASE_URL = 'https://apis-dev.biometria.io/certiface-saas';

export type SaasCredentials = {
  operatorLogin: string;
  operatorPassword: string;
  birthDate: string;
  fullName: string;
};

async function authenticate(
  operatorLogin: string,
  operatorPassword: string
): Promise<string> {
  const response = await fetch(`${SAAS_BASE_URL}/api/v1/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      login: operatorLogin.trim(),
      password: md5(operatorPassword),
    }),
  });

  if (!response.ok) {
    throw new Error(`Credenciais de operador inválidas (${response.status})`);
  }

  const body = (await response.json()) as { token?: string };
  if (!body.token) {
    throw new Error('Resposta de login SAAS sem token');
  }
  return body.token;
}

export async function createJourneyToken(
  credentials: SaasCredentials,
  documentNumber: string,
  livenessProvider: SaasProvider
): Promise<string> {
  const jwt = await authenticate(
    credentials.operatorLogin,
    credentials.operatorPassword
  );

  const response = await fetch(`${SAAS_BASE_URL}/api/v1/protected/genToken`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwt}`,
    },
    body: JSON.stringify({
      documentNumber: documentNumber.replace(/\D/g, ''),
      birthDate: credentials.birthDate,
      fullName: credentials.fullName.trim(),
      journeyType: 1,
      livenessProvider,
    }),
  });

  if (!response.ok) {
    throw new Error(`Erro ao gerar token SAAS (${response.status})`);
  }

  const body = (await response.json()) as { uuid?: string };
  if (!body.uuid) {
    throw new Error('Resposta de token SAAS sem uuid');
  }
  return body.uuid;
}

export async function cancelJourneyToken(
  operatorLogin: string,
  operatorPassword: string,
  journeyToken: string
): Promise<void> {
  const jwt = await authenticate(operatorLogin, operatorPassword);
  const response = await fetch(
    `${SAAS_BASE_URL}/api/v1/token/${encodeURIComponent(journeyToken)}`,
    {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${jwt}` },
    }
  );

  if (!response.ok && response.status !== 409) {
    throw new Error(`Erro ao cancelar token SAAS (${response.status})`);
  }
}
