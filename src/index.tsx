import OitiSDK from './NativeRnSdk';
import type { OitiTheme } from './@types/theme';
import { AssetProcessor } from './utils/AssetProcessor';

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

export async function startJourney(
  appKey: string,
  isCustomEnabled?: boolean,
  theme?: OitiTheme
): Promise<string> {
  try {
    // Process assets and convert to base64
    const processedTheme = theme
      ? await AssetProcessor.processThemeAssets(theme)
      : theme;

    return new Promise((resolve, reject) => {
      OitiSDK.startJourney(
        appKey,
        (data: string) => resolve(data),
        (error: string) => reject(new Error(error)),
        isCustomEnabled,
        processedTheme as Object
      );
    });
  } catch (error) {
    throw new Error(`Failed to process theme assets: ${error}`);
  }
}

export * from './@types/theme';
