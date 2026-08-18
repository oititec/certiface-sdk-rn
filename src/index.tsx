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
};

export { CertifaceError } from './errors/CertifaceError';
export * from './@types/theme';
export * from './@types/result';
