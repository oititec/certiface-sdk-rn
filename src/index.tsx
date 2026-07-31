import CertifaceRnSdk from './NativeRnSdk';
import type { CertifaceTheme, LivenessProvider } from './@types/theme';

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
 * Start a liveness journey using an **appKey** (legacy flow).
 *
 * Use this for providers that authenticate via appKey (e.g. iProov).
 * For token-based providers (FaceTec / FortFace), use {@link startSaasJourney} instead.
 */
async function startJourney(
  appKey: string,
  environment: Environment,
  provider: LivenessProvider,
  isCustomEnabled?: boolean,
  theme?: CertifaceTheme
): Promise<LivenessResult> {
  return new Promise((resolve, reject) => {
    CertifaceRnSdk.startJourney(
      appKey,
      environment,
      provider,
      (data: string) => {
        try {
          const parsedResponse: LivenessResponse = JSON.parse(data);

          if (parsedResponse.status === 'success') {
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
 * Start a liveness journey using a **journeyToken** (SaaS flow).
 *
 * The provider (FaceTec or FortFace) is resolved server-side based on the token.
 * Use this for the new SaaS architecture. For appKey-based providers (iProov),
 * use {@link startJourney} instead.
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
