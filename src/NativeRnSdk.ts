import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  multiply(a: number, b: number): number;
  checkCameraPermission(): Promise<boolean>;
  requestCameraPermission(): Promise<boolean>;
  startJourney(
    appKey: string,
    onSuccess: (data: string) => void,
    onError: (error: string) => void
  ): void;
  testString(appKey: string): string;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RnSdk');
