import OitiSDK from './NativeRnSdk';
import type { OitiTheme } from './@types/theme';
import { Environment } from './@types/theme';

export function checkCameraPermission(): Promise<boolean> {
  return OitiSDK.checkCameraPermission();
}

export function requestCameraPermission(): Promise<boolean> {
  return OitiSDK.requestCameraPermission();
}

export async function startJourney(
  appKey: string,
  environment: Environment,
  isCustomEnabled?: boolean,
  theme?: OitiTheme
): Promise<string> {
  return new Promise((resolve, reject) => {
    OitiSDK.startJourney(
      appKey,
      environment,
      (data: string) => resolve(data),
      (error: string) => reject(new Error(error)),
      isCustomEnabled,
      theme as Object
    );
  });
}

export * from './@types/theme';
