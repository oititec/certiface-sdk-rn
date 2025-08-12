import OitiSDK from './NativeRnSdk';
import type { OitiTheme } from './@types/theme';

export function multiply(a: number, b: number): number {
  return OitiSDK.multiply(a, b);
}

export function checkCameraPermission(): Promise<boolean> {
  return OitiSDK.checkCameraPermission();
}

export function requestCameraPermission(): Promise<boolean> {
  return OitiSDK.requestCameraPermission();
}

export function testString(string: string): string {
  return OitiSDK.testString(string);
}

export function startJourney(
  appKey: string,
  isCustomEnabled?: boolean,
  theme?: OitiTheme
): Promise<string> {
  return new Promise((resolve, reject) => {
    OitiSDK.startJourney(
      appKey,
      (data: string) => resolve(data),
      (error: string) => reject(new Error(error)),
      isCustomEnabled,
      theme as Object
    );
  });
}

export * from './@types/theme';
