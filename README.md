<p align="center">
  <img src="Documentation/images/certiface-header.png" alt="CertiFace" />
</p>
<div align="left">

# @certiface/sdk

SDK React Native para verificação biométrica de liveness (prova de vida). Integra FaceTec, Fortface e iProov em Android e iOS, com temas customizáveis e API TypeScript.

</div>

---

## Instalação

```bash
npm install @certiface/sdk
```

ou

```bash
yarn add @certiface/sdk
```

## Configuração

### Android

1. Configure os repositórios no `android/build.gradle`:

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url 'https://raw.githubusercontent.com/oititec/android-certiface-sdk-versions/main'
        }
        maven {
            url 'https://cdn-fortface-sdk.fortface.com.br'
            content {
                includeGroup 'br.com.fortface'
            }
            credentials(HttpHeaderCredentials) {
                name = 'X-Sdk'
                value = 'certiface'
            }
            authentication {
                header(HttpHeaderAuthentication)
            }
        }
    }
}
```

2. Permissões em `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.INTERNET" />
```

### iOS

1. Fontes no `ios/Podfile`:

```ruby
source 'https://github.com/oititec/ios-artifactory.git'
source 'https://github.com/CocoaPods/Specs.git'
```

2. Permissões no `Info.plist`:

```xml
<key>NSCameraUsageDescription</key>
<string>Precisamos acessar sua câmera para verificação de identidade</string>
```

3. Instale os pods:

```bash
cd ios && pod install
```

## Qual fluxo usar?

| Fluxo | Método | Credencial | Providers |
| ----- | ------ | ---------- | --------- |
| **SaaS** (recomendado) | `CertifaceSDK.startSaasJourney` | `journeyToken` | FaceTec ou Fortface (resolvido no servidor) |
| **AppKey** | `CertifaceSDK.startJourney` | `appKey` | **iProov** |

> FaceTec via `startJourney(appKey, ..., FACETEC)` não é mais suportado. Use `startSaasJourney` com `journeyToken`.

## Uso básico

### Importação

```typescript
import {
  CertifaceSDK,
  CertifaceError,
  LivenessProvider,
  Environment,
  type CertifaceFlow,
  type CertifaceTheme,
  type LivenessResult,
  type SaasProvider,
} from '@certiface/sdk';
```

### SaaS (FaceTec / Fortface)

```typescript
const journeyToken = 'your-journey-token';

const result = await CertifaceSDK.startSaasJourney(
  journeyToken,
  Environment.HML,
  false
);

const resultWithTheme = await CertifaceSDK.startSaasJourney(
  journeyToken,
  Environment.PRD,
  true,
  customTheme
);
```

### iProov (appKey)

```typescript
const result = await CertifaceSDK.startJourney(
  'your-app-key',
  Environment.HML,
  LivenessProvider.IPROOV,
  false
);

const resultWithTheme = await CertifaceSDK.startJourney(
  'your-app-key',
  Environment.PRD,
  LivenessProvider.IPROOV,
  true,
  customTheme
);
```

### Resultado

```typescript
interface LivenessResult {
  valid: boolean;
  codID: string;
  cause: string;
  protocol: string;
  scanResultBlob: string;
}
```

## API

### `CertifaceSDK`

| Método | Descrição |
| ------ | --------- |
| `startSaasJourney(...)` | Jornada SaaS com `journeyToken` (FaceTec / Fortface) |
| `startJourney(...)` | Jornada com `appKey` (**iProov**) |
| `checkCameraPermission()` | Verifica permissão da câmera |
| `requestCameraPermission()` | Solicita permissão da câmera |

### `startSaasJourney(token, environment, isCustomEnabled?, theme?)`

| Nome | Tipo | Obrigatório | Descrição |
| ---- | ---- | ----------- | --------- |
| `token` | `string` | sim | `journeyToken` da Certiface |
| `environment` | `Environment` | sim | `HML` ou `PRD` |
| `isCustomEnabled` | `boolean` | não | Tema custom (padrão `false`) |
| `theme` | `CertifaceTheme` | não | Tema (FaceTec e/ou Fortface + telas comuns) |

**Retorna:** `Promise<LivenessResult>`

### `startJourney(appKey, environment, provider, isCustomEnabled?, theme?)`

| Nome | Tipo | Obrigatório | Descrição |
| ---- | ---- | ----------- | --------- |
| `appKey` | `string` | sim | AppKey Certiface |
| `environment` | `Environment` | sim | `HML` ou `PRD` |
| `provider` | `LivenessProvider` | sim | Use `LivenessProvider.IPROOV` |
| `isCustomEnabled` | `boolean` | não | Tema custom (padrão `false`) |
| `theme` | `CertifaceTheme` | não | Tema iProov + telas comuns |

**Retorna:** `Promise<LivenessResult>`

### Permissões

```typescript
const hasPermission = await CertifaceSDK.checkCameraPermission();
if (!hasPermission) {
  const granted = await CertifaceSDK.requestCameraPermission();
  if (!granted) {
    // usuário negou
  }
}
```

### Erros (`CertifaceError`)

```typescript
import { CertifaceSDK, CertifaceError, Environment } from '@certiface/sdk';

try {
  await CertifaceSDK.startSaasJourney(token, Environment.HML);
} catch (error) {
  if (error instanceof CertifaceError) {
    console.log(error.code, error.message, error.invalidParam);
  }
}
```

Códigos comuns:

| Código | Significado |
| ------ | ----------- |
| `JOURNEY_IN_PROGRESS` | Já existe uma jornada em andamento |
| `JOURNEY_TIMEOUT` | Jornada expirou sem resposta do nativo |
| `UNSUPPORTED_OPERATION` | Provider não-iProov em `startJourney` (use SaaS) |
| `INVALID_PARAMS` | Tema inválido (`invalidParam` indica o campo) |
| `NO_ACTIVITY` | Sem Activity / rootViewController |
| `TOKEN_NULO` / `APP_KEY_NULO` / `ENVIRONMENT_NULO` | Parâmetro obrigatório ausente |

### Enums / tipos

```typescript
enum LivenessProvider {
  IPROOV = 'IPROOV',
}

type CertifaceFlow = 'IPROOV' | 'SAAS';

type SaasProvider = 'FACETEC' | 'FORTFACE';

enum Environment {
  HML = 'HML',
  PRD = 'PRD',
}
```

`LivenessProvider` é só para `startJourney` (iProov). Os fluxos de produto são **IPROOV** e **SAAS** (`CertifaceFlow`). `SaasProvider` tipa a engine na **geração do token**, não o método de jornada.

## Personalização de tema

Suporte a temas para **FaceTec**, **Fortface** e **iProov** (além de `instructions`, `permission`, `processing`, `result`).

```typescript
import {
  CertifaceSDK,
  Environment,
  LivenessProvider,
  type CertifaceTheme,
} from '@certiface/sdk';

const customTheme: CertifaceTheme = {
  facetec: {
    colors: {
      frameBackground: '#1A1A1A',
      ovalStroke: '#FF6B35',
    },
    texts: {
      readyHeader1: 'Prepare-se',
      readyButton: 'Iniciar',
    },
  },
  fortface: {
    colors: {
      cameraMessage: '#FFFFFF',
    },
  },
  instructions: {
    configuration: {
      showInstructionScreen: true,
    },
  },
};

await CertifaceSDK.startSaasJourney(
  journeyToken,
  Environment.HML,
  true,
  customTheme
);

await CertifaceSDK.startJourney(
  appKey,
  Environment.HML,
  LivenessProvider.IPROOV,
  true,
  customTheme
);
```

No fluxo SaaS com tema custom, o SDK monta customização FaceTec **e** Fortface (o provider efetivo é definido pelo token no servidor).

## Exemplos

### Permissão + SaaS

```typescript
import React, { useState } from 'react';
import { View, Button, Alert, ActivityIndicator } from 'react-native';
import {
  CertifaceSDK,
  CertifaceError,
  Environment,
  type LivenessResult,
} from '@certiface/sdk';

export default function LivenessScreen() {
  const [loading, setLoading] = useState(false);

  const handleVerification = async () => {
    setLoading(true);
    try {
      const hasPermission = await CertifaceSDK.checkCameraPermission();
      if (!hasPermission) {
        const granted = await CertifaceSDK.requestCameraPermission();
        if (!granted) {
          Alert.alert('Erro', 'Permissão da câmera é necessária');
          return;
        }
      }

      const result: LivenessResult = await CertifaceSDK.startSaasJourney(
        'your-journey-token',
        Environment.HML,
        false
      );

      if (result.valid) {
        Alert.alert('Aprovado', `Protocolo: ${result.protocol}`);
      } else {
        Alert.alert('Recusado', result.cause || 'Verificação não aprovada');
      }
    } catch (error) {
      if (error instanceof CertifaceError) {
        Alert.alert('Erro', `${error.code}: ${error.message}`);
      } else {
        Alert.alert('Erro', String(error));
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', padding: 20 }}>
      {loading ? (
        <ActivityIndicator size="large" />
      ) : (
        <Button title="Iniciar verificação" onPress={handleVerification} />
      )}
    </View>
  );
}
```

### iProov com tema

```typescript
import { CertifaceSDK, Environment, LivenessProvider } from '@certiface/sdk';

await CertifaceSDK.startJourney(
  'your-app-key',
  Environment.HML,
  LivenessProvider.IPROOV,
  true,
  customTheme
);
```

---

## 🎭 Tema Completo

### Exemplo de Tema Personalizado Completo

```typescript
const customTheme: CertifaceTheme = {
  facetec: {
    colors: {
      readyScreenHeader: '#FFFFFF',
      readyScreenSubtext: '#CCCCCC',
      readyScreenOvalFill: '#FF6B35',
      readyScreenTextBackground: '#444444',
      resultScreenForeground: '#FF6B35',
      resultScreenBackground: '#F0F8FF',
      ovalStroke: '#FF6B35',
      ovalProgressFirst: '#FF6B35',
      ovalProgressSecond: '#FFD700',
      overlayBackground: '#80000000',
      frameBorder: '#FF6B35',
      frameBackground: '#1A1A1A',
      feedbackBarBackground: '#FFF8DC',
      feedbackMessage: '#333333',
      guidanceBackground: '#2E2E2E',
      guidanceForeground: '#FFFFFF',
      guidanceButtonTextNormal: '#FFFFFF',
      guidanceButtonTextHighlight: '#FFFFFF',
      guidanceButtonTextDisabled: '#AAAAAA',
      guidanceButtonBackgroundNormal: '#FF6B35',
      guidanceButtonBackgroundHighlight: '#FF6B35',
      guidanceButtonBackgroundDisabled: '#666666',
      guidanceButtonBorder: '#FF6B35',
    },
    texts: {
      readyHeader1: 'Prepare-se',
      readyHeader2: 'para verificação',
      readyMessage1: 'Posicione seu rosto',
      readyMessage2: 'dentro do círculo',
      readyButton: 'Iniciar',
      retryHeader: 'Vamos tentar novamente',
      retrySubheader: 'Ajustes necessários',
      retryButton: 'Tentar Novamente',
      resultSuccessMessage: 'Verificação concluída!',
      feedbackCenterFace: 'Centralize seu rosto',
      feedbackHoldSteady: 'Mantenha-se parado',
      feedbackMovePhoneCloser: 'Aproxime o dispositivo',
      feedbackMovePhoneAway: 'Afaste o dispositivo',
    },
    assets: {
      overlayBrandImage: 'overlayBrandImage',
      cancelButtonIcon: 'cancelButtonIcon',
      resultScreenCustomActivityIndicatorImage:
        'resultScreenCustomActivityIndicatorImage',
    },
    fonts: {
      readyScreenHeader: 'sixty',
      readyScreenSubtext: 'sixty',
      resultScreenMessage: 'sixty',
      retryScreenHeader: 'sixty',
      retryScreenSubtext: 'sixty',
      feedbackMessage: 'sixty',
      guidanceHeader: 'sixty',
      guidanceSubtext: 'sixty',
      guidanceButton: 'sixty',
    },
  },
  iproov: {
    colors: {
      title: '#FFFFFF',
      titleBackground: '#2E2E2E',
      promptText: '#FFFFFF',
      promptBackground: '#1A1A1A',
      background: '#FF6B35',
      ovalReady: '#FF6B35',
      ovalNotReady: '#FF3030',
      ovalCapturing: '#FFFFFF',
      ovalCompleted: '#FF6B35',
    },
    texts: {
      title: 'Verificação Biométrica',
    },
    assets: {
      closeButtonIcon: 'closeButtonIcon',
      logoImage: 'logoImage',
    },
    fonts: {
      instructionsTitleFont: 'sixty',
      instructionsCaptionFont: 'sixty',
      instructionsDocumentTypesInstructionsFont: 'sixty',
      instructionsDocumentTipsInstructionsFont: 'sixty',
      instructionsButtonFont: 'sixty',
      permissionTitleFont: 'sixty',
      permissionCaptionFont: 'sixty',
      permissionButtonFont: 'sixty',
      resultMessageFont: 'sixty',
      resultRetryButtonFont: 'sixty',
    },
  },
  instructions: {
    colors: {
      statusBar: '#2E2E2E',
      background: '#2E2E2E',
      backButtonIcon: '#2E2E2E',
      backButtonBackground: '#2E2E2E',
      backButtonBorder: '#2E2E2E',
      bottomSheet: '#1A1A1A',
      title: '#FFFFFF',
      caption: '#CCCCCC',
      firstInstructionTitle: '#FFFFFF',
      secondInstructionTitle: '#FFFFFF',
      continueButtonText: '#FFFFFF',
      continueButtonBackground: '#FF6B35',
      continueButtonBorder: '#FF6B35',
    },
    texts: {
      title: 'Verificação de Identidade',
      caption: 'Siga as instruções para completar o processo',
      firstInstruction: 'Mantenha o documento bem iluminado',
      secondInstruction: 'Use um documento oficial com foto',
      continueButton: 'Continuar',
    },
    assets: {
      backButtonIcon: 'backButtonIcon',
      contextImage: 'contextImage',
      firstInstructionIcon: 'firstInstructionIcon',
      secondInstructionIcon: 'secondInstructionIcon',
    },
    fonts: {
      title: 'sixty',
      caption: 'sixty',
      firstInstructionTitle: 'sixty',
      secondInstructionTitle: 'sixty',
      continueButton: 'sixty',
    },
  },
  permission: {
    colors: {
      statusBar: '#2E2E2E',
      background: '#2E2E2E',
      backButtonIcon: '#2E2E2E',
      backButtonBackground: '#2E2E2E',
      backButtonBorder: '#2E2E2E',
      cameraImage: '#FFFFFF',
      title: '#FFFFFF',
      caption: '#FFFFFF',
      checkPermissionButtonText: '#FFFFFF',
      checkPermissionButtonBackground: '#FF6B35',
      checkPermissionButtonBorder: '#FF6B35',
      bottomSheet: '#FF6B35',
      bottomSheetTitle: '#FF6B35',
      bottomSheetCaption: '#FF6B35',
      openSettingsButtonText: '#FF6B35',
      openSettingsButtonBackground: '#FF6B35',
      openSettingsButtonBorder: '#FF6B35',
      closeButtonText: '#FF6B35',
      closeButtonBackground: '#FF6B35',
      closeButtonBorder: '#FF6B35',
    },
    texts: {
      title: 'Permissões Necessárias',
      caption: 'Permissões Necessárias',
      checkPermissionButton: 'Permitir Acesso',
      bottomSheetTitle: 'Permitir Acesso',
      bottomSheetCaption: 'Permitir Acesso',
      openSettingsButton: 'Permitir Acesso',
      closeButton: 'Permitir Acesso',
    },
    assets: {
      backButtonIcon: 'backButtonIcon',
      cameraImage: 'cameraImage',
    },
    fonts: {
      title: 'sixty',
      caption: 'sixty',
      checkPermissionButton: 'sixty',
      bottomSheetTitle: 'sixty',
      bottomSheetCaption: 'sixty',
      opentSettingsButton: 'sixty',
      closeButton: 'sixty',
    },
  },
  processing: {
    colors: {
      statusBar: '#1A1A1A',
      background: '#1A1A1A',
      loading: '#FFFFFF',
    },
  },
  result: {
    colors: {
      successStatusBar: '#E8F5E8',
      successBackground: '#E8F5E8',
      successText: '#2E7D32',
      errorStatusBar: '#FFEBEE',
      errorBackground: '#FFEBEE',
      errorText: '#C62828',
      retryBackground: '#C62828',
      retryText: '#FFEBEE',
      retryButtonText: '#FF6B35',
      retryButtonBackground: '#FFFFFF',
      retryButtonBorder: '#FFFFFF',
    },
    texts: {
      success: 'Verificação concluída com sucesso!',
      error: 'Houve um erro na verificação. Tente novamente.',
      retryButton: 'Tentar Novamente',
    },
    assets: {
      successImage: 'successImage',
      errorImage: 'errorImage',
      retryImage: 'retryImage',
    },
    fonts: {
      text: 'sixty',
      retryButton: 'sixty',
    },
  },
};
```

### Configuração de Assets e Fonts

Os assets e fonts referenciados no tema precisam ser cadastrados nativamente em cada plataforma:

#### Android - Drawable Resources

Os assets devem ser adicionados em `android/app/src/main/res/drawable/`:

```
android/app/src/main/res/
└── drawable/
    ├── shell.png
    └── outros_assets.png
```

Formatos aceitos: `.png`, `.jpg`, `.xml` (vector drawables)

#### Android - Fonts

As fontes devem ser adicionadas em `android/app/src/main/res/font/`:

```
android/app/src/main/res/
└── font/
    ├── sixty.ttf
    └── outras_fontes.ttf
```

Formatos aceitos: `.ttf`, `.otf`

#### iOS - Assets

Os assets devem ser adicionados em um Asset Catalog (`.xcassets`):

```
ios/YourApp/
└── Images.xcassets/
    ├── shell.imageset/
    │   ├── Contents.json
    │   ├── shell.png
    │   ├── shell@2x.png
    │   └── shell@3x.png
    └── Contents.json
```

Estrutura do `Contents.json` para cada imageset:

```json
{
  "images": [
    {
      "filename": "shell.png",
      "idiom": "universal",
      "scale": "1x"
    },
    {
      "filename": "shell@2x.png",
      "idiom": "universal",
      "scale": "2x"
    },
    {
      "filename": "shell@3x.png",
      "idiom": "universal",
      "scale": "3x"
    }
  ],
  "info": {
    "author": "xcode",
    "version": 1
  }
}
```

#### iOS - Fonts

As fontes devem ser adicionadas ao projeto e registradas no `Info.plist`:

1. Adicione os arquivos de fonte ao projeto:

```
ios/YourApp/
└── Fonts/
    ├── sixty.ttf
    └── outras_fontes.ttf
```

2. Registre no `Info.plist`:

```xml
<key>UIAppFonts</key>
<array>
  <string>sixty.ttf</string>
  <string>outras_fontes.ttf</string>
</array>
```

3. Adicione no Xcode: arraste os arquivos `.ttf` para o projeto no Xcode e marque "Copy items if needed" e o target correto.

### Referenciando no Tema

Use apenas o nome base do asset/font (sem extensão):

```typescript
assets: {
  overlayBrandImage: 'shell',
},
fonts: {
  readyScreenHeader: 'sixty',
}
```

---

## ✨ Funcionalidades

- ✅ Verificação de liveness com FaceTec, Fortface (SaaS) e iProov
- ✅ Fluxo SaaS com `journeyToken` e fluxo legado com `appKey` (iProov)
- ✅ Gerenciamento de permissões de câmera
- ✅ Temas personalizados (FaceTec, Fortface, iProov)
- ✅ `CertifaceError` com código e `invalidParam`
- ✅ TypeScript completo
- ✅ Android e iOS via TurboModules
- ✅ React Native ≥ 0.79

## 📋 Requisitos

- React Native ≥ 0.79
- Android API ≥ 26
- iOS ≥ 12.0
- TypeScript ≥ 4.0 (recomendado)

## 🔗 Links Úteis

- [Changelog](https://github.com/oititec/certiface-sdk-rn/releases)

## 📄 Licença

MIT © [Certiface](https://github.com/oititec)

---

<div align="center">

**Feito com ❤️ pela equipe Certiface**

</div>
