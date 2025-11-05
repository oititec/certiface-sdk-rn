import OitiSDK from './NativeRnSdk';
import type { OitiTheme } from './@types/theme';
import type { LivenessResponse, LivenessResult } from './@types/result';

export function checkCameraPermission(): Promise<boolean> {
  return OitiSDK.checkCameraPermission();
}

export function requestCameraPermission(): Promise<boolean> {
  return OitiSDK.requestCameraPermission();
}

export async function startJourney(
  appKey: string,
  isCustomEnabled?: boolean,
  theme?: OitiTheme
): Promise<LivenessResult> {
  return new Promise((resolve, reject) => {
    OitiSDK.startJourney(
      appKey,
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

export * from './@types/theme';
export * from './@types/result';
