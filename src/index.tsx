import OitiSDK from './NativeRnSdk';
import type { LivenessProvider, OitiTheme } from './@types/theme';

import type { LivenessResponse, LivenessResult } from './@types/result';
import type { Environment } from './@types/theme';

function checkCameraPermission(): Promise<boolean> {
  return OitiSDK.checkCameraPermission();
}

function requestCameraPermission(): Promise<boolean> {
  return OitiSDK.requestCameraPermission();
}

async function startJourney(
  appKey: string,
  environment: Environment,
  provider: LivenessProvider,
  isCustomEnabled?: boolean,
  theme?: OitiTheme
): Promise<LivenessResult> {
  return new Promise((resolve, reject) => {
    OitiSDK.startJourney(
      appKey,
      environment,
      provider,
      (data: string) => {
        try {
          const parsedResponse: LivenessResponse = JSON.parse(data);

          if (parsedResponse.status === 'success') {
            resolve(parsedResponse.result);
          } else {
            reject(new Error(parsedResponse.message));
          }
        } catch (parseError) {
          reject(new Error(`Failed to parse response: ${parseError}`));
        }
      },
      (error: string) => reject(new Error(error)),
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

export * from './@types/theme';
export * from './@types/result';
