import CertifaceRnSdk from './NativeRnSdk';
import { LivenessProvider, type CertifaceTheme } from './@types/theme';

import type { LivenessResponse, LivenessResult } from './@types/result';
import type { Environment } from './@types/theme';
import { CertifaceError } from './errors/CertifaceError';
import { parseNativeError } from './errors/parseNativeError';

function checkCameraPermission(): Promise<boolean> {
  return CertifaceRnSdk.checkCameraPermission();
}

function requestCameraPermission(): Promise<boolean> {
  return CertifaceRnSdk.requestCameraPermission();
}

/**
 * Start an iProov liveness journey using an **appKey**.
 *
 * The only supported provider is {@link LivenessProvider.IPROOV}.
 * For SaaS (FaceTec / Fortface via journeyToken), use {@link startSaasJourney}.
 */
async function startJourney(
  appKey: string,
  environment: Environment,
  provider: LivenessProvider,
  isCustomEnabled?: boolean,
  theme?: CertifaceTheme
): Promise<LivenessResult> {
  if (provider !== LivenessProvider.IPROOV) {
    return Promise.reject(
      new CertifaceError(
        'UNSUPPORTED_OPERATION',
        "Apenas LivenessProvider.IPROOV é suportado em startJourney. Use CertifaceSDK.startSaasJourney(token, environment, ...) para o fluxo SaaS."
      )
    );
  }

  return new Promise((resolve, reject) => {
    CertifaceRnSdk.startJourney(
      appKey,
      environment,
      provider,
      (data: string) => {
        try {
          const parsedResponse: LivenessResponse = JSON.parse(data);

          if (parsedResponse.status === 'success') {
            if (!parsedResponse.result) {
              reject(
                new CertifaceError(
                  'PARSE_ERROR',
                  'Success response missing result payload'
                )
              );
              return;
            }
            resolve(parsedResponse.result);
            return;
          }

          reject(
            CertifaceError.fromPayload({
              code: parsedResponse.code ?? 'UNKNOWN_ERROR',
              message: parsedResponse.message,
              invalidParam: parsedResponse.invalidParam,
            })
          );
        } catch (parseError) {
          reject(
            new CertifaceError(
              'PARSE_ERROR',
              `Failed to parse response: ${parseError}`
            )
          );
        }
      },
      (error: string) => reject(parseNativeError(error)),
      isCustomEnabled,
      theme as Object
    );
  });
}

/**
 * Start a SaaS liveness journey using a **journeyToken**.
 *
 * FaceTec or Fortface is resolved server-side from the token.
 * For iProov with appKey, use {@link startJourney}.
 */
async function startSaasJourney(
  token: string,
  environment: Environment,
  isCustomEnabled?: boolean,
  theme?: CertifaceTheme
): Promise<LivenessResult> {
  return new Promise((resolve, reject) => {
    CertifaceRnSdk.startSaasJourney(
      token,
      environment,
      (data: string) => {
        try {
          const parsedResponse: LivenessResponse = JSON.parse(data);

          if (parsedResponse.status === 'success') {
            if (!parsedResponse.result) {
              reject(
                new CertifaceError(
                  'PARSE_ERROR',
                  'Success response missing result payload'
                )
              );
              return;
            }
            resolve(parsedResponse.result);
            return;
          }

          reject(
            CertifaceError.fromPayload({
              code: parsedResponse.code ?? 'UNKNOWN_ERROR',
              message: parsedResponse.message,
              invalidParam: parsedResponse.invalidParam,
            })
          );
        } catch (parseError) {
          reject(
            new CertifaceError(
              'PARSE_ERROR',
              `Failed to parse response: ${parseError}`
            )
          );
        }
      },
      (error: string) => reject(parseNativeError(error)),
      isCustomEnabled,
      theme as Object
    );
  });
}

export const CertifaceSDK = {
  checkCameraPermission,
  requestCameraPermission,
  startJourney,
  startSaasJourney,
};

export { CertifaceError } from './errors/CertifaceError';
export * from './@types/theme';
export * from './@types/result';
