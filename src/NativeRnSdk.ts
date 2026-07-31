import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  checkCameraPermission(): Promise<boolean>;
  requestCameraPermission(): Promise<boolean>;
  startJourney(
    appKey: string,
    environment: string,
    provider: string,
    onSuccess: (data: string) => void,
    onError: (error: string) => void,
    isCustomEnabled?: boolean,
    theme?: Object
  ): void;
  startSaasJourney(
    token: string,
    environment: string,
    onSuccess: (data: string) => void,
    onError: (error: string) => void,
    isCustomEnabled?: boolean,
    theme?: Object
  ): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('CertifaceRnSdk');
