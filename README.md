<IMG  src="https://github.com/oititec/liveness-android-sdk/blob/main/Documentation/Images/OitiHeader.png?raw=true"  alt="OitiHeader.png"/>
<div align="left">

# @oiti/rn-sdk

SDK React Native oficial da Oiti para verificação biométrica de liveness (prova de vida). Integre detecção facial avançada com FaceTec e iProov em aplicações Android e iOS com suporte completo a personalização de temas e interface nativa de alta performance.


</div>

---

## 📦 Instalação

```bash
npm install @oiti/rn-sdk
```

ou

```bash
yarn add @oiti/rn-sdk
```

## ⚙️ Configuração

### Android

1. Configure os repositórios necessários no `android/build.gradle`:

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url 'https://raw.githubusercontent.com/oititec/android-oiti-sdk-versions/master'
        }
    }
}
```

2. Adicione as permissões no `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.INTERNET" />
```

### iOS

1. Configure as fontes do CocoaPods no `ios/Podfile`:

```ruby
source 'https://github.com/oititec/ios-artifactory.git'
source 'https://github.com/CocoaPods/Specs.git'
```

2. Adicione as permissões no `ios/YourApp/Info.plist`:

```xml
<key>NSCameraUsageDescription</key>
<string>Precisamos acessar sua câmera para verificação de identidade</string>
<key>NSPhotoLibraryUsageDescription</key>
<string>Precisamos acessar suas fotos para verificação de identidade</string>
```

3. Execute a instalação dos pods:

```bash
cd ios && pod install
```

## 🚀 Uso Básico

### Importação

```typescript
import {
  startJourney,
  checkCameraPermission,
  requestCameraPermission,
  LivenessProvider,
  Environment,
  type OitiTheme,
} from '@oiti/rn-sdk';
```

### Exemplo Simples

```typescript
import React from 'react';
import { Button, Alert } from 'react-native';
import { startJourney, Environment } from '@oiti/rn-sdk';

export default function App() {
  const handleVerification = async () => {
    try {
      const appKey = 'your-app-key-here';
      const result = await startJourney(appKey, Environment.HML);

      Alert.alert('Sucesso!', `Verificação concluída: ${result}`);
    } catch (error) {
      Alert.alert('Erro', `Falha: ${error.message}`);
    }
  };

  return <Button title="Iniciar Verificação" onPress={handleVerification} />;
}
```

### Resultado Esperado

Quando bem-sucedido, o método `startJourney` retorna uma string JSON contendo:

```json
{
  "valid": true,
  "codID": "abc123def456",
  "cause": "Approved",
  "protocol": "20231105-001"
}
```

## 📚 API

### `startJourney(appKey, environment, isCustomEnabled?, theme?)`

Inicia o processo de verificação de liveness.

**Parâmetros:**

| Nome              | Tipo          | Obrigatório | Descrição                                      |
| ----------------- | ------------- | ----------- | ---------------------------------------------- |
| `appKey`          | `string`      | ✅          | Chave de aplicação fornecida pela Oiti         |
| `environment`     | `Environment` | ✅          | Ambiente de execução (`HML` ou `PRD`)          |
| `isCustomEnabled` | `boolean`     | ❌          | Habilita tema personalizado (padrão: `false`)  |
| `theme`           | `OitiTheme`   | ❌          | Objeto com configurações de tema personalizado |

**Retorna:** `Promise<string>` com o resultado da verificação

**Exemplo:**

```typescript
const result = await startJourney('your-app-key', Environment.HML);

const resultWithTheme = await startJourney(
  'your-app-key',
  Environment.PRD,
  true,
  customTheme
);
```

---

### `checkCameraPermission()`

Verifica se a permissão da câmera foi concedida.

**Retorna:** `Promise<boolean>`

**Exemplo:**

```typescript
const hasPermission = await checkCameraPermission();
if (!hasPermission) {
  console.log('Permissão não concedida');
}
```

**Resultado:**

- `true` - Permissão concedida
- `false` - Permissão negada

---

### `requestCameraPermission()`

Solicita permissão da câmera ao usuário.

**Retorna:** `Promise<boolean>`

**Exemplo:**

```typescript
const granted = await requestCameraPermission();
if (granted) {
  console.log('Usuário concedeu permissão');
}
```

**Resultado:**

- `true` - Usuário concedeu permissão
- `false` - Usuário negou permissão

---

## 🎨 Personalização de Tema

O SDK oferece suporte a temas personalizados para os provedores **FaceTec** e **iProov**.

### Estrutura do Tema

```typescript
import { LivenessProvider, Environment, type OitiTheme } from '@oiti/rn-sdk';

const customTheme: OitiTheme = {
  provider: LivenessProvider.FACETEC,
  facetec: {
    colors: {
      frameBackground: '#1A1A1A',
      frameBorder: '#FF6B35',
      ovalStroke: '#FF6B35',
      ovalProgressFirst: '#FF6B35',
      ovalProgressSecond: '#FFD700',
    },
    texts: {
      readyHeader1: 'Prepare-se',
      readyHeader2: 'para verificação',
      readyButton: 'Iniciar',
    },
    fonts: {
      readyScreenHeader: 'CustomFont',
    },
  },
};

await startJourney(appKey, Environment.HML, true, customTheme);
```

### Opções de Provider

```typescript
enum LivenessProvider {
  FACETEC = 'FACETEC',
  IPROOV = 'IPROOV',
}
```

### Opções de Environment

```typescript
enum Environment {
  HML = 'HML',
  PRD = 'PRD',
}
```

## 💡 Exemplos

### Exemplo com Gerenciamento de Permissões

```typescript
import React, { useState } from 'react';
import { View, Button, Alert, ActivityIndicator } from 'react-native';
import {
  startJourney,
  checkCameraPermission,
  requestCameraPermission,
  Environment,
} from '@oiti/rn-sdk';

export default function LivenessScreen() {
  const [loading, setLoading] = useState(false);

  const handleVerification = async () => {
    setLoading(true);

    try {
      const hasPermission = await checkCameraPermission();

      if (!hasPermission) {
        const granted = await requestCameraPermission();
        if (!granted) {
          Alert.alert('Erro', 'Permissão da câmera é necessária');
          setLoading(false);
          return;
        }
      }

      const appKey = 'your-app-key-here';
      const result = await startJourney(appKey, Environment.HML);

      const data = JSON.parse(result);

      if (data.valid) {
        Alert.alert(
          'Verificação Aprovada!',
          `Protocolo: ${data.protocol}\nCódigo: ${data.codID}`
        );
      } else {
        Alert.alert('Verificação Recusada', `Motivo: ${data.cause}`);
      }
    } catch (error) {
      Alert.alert('Erro', error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', padding: 20 }}>
      {loading ? (
        <ActivityIndicator size="large" color="#007AFF" />
      ) : (
        <Button title="Iniciar Verificação" onPress={handleVerification} />
      )}
    </View>
  );
}
```

**Resultado de Sucesso:**

```json
{
  "valid": true,
  "codID": "abc123def456",
  "cause": "Approved",
  "protocol": "20231105-001"
}
```

**Resultado de Erro:**

```json
{
  "valid": false,
  "cause": "User cancelled",
  "protocol": null
}
```

---

### Exemplo com Tema Customizado Completo

```typescript
import React from 'react';
import { Button } from 'react-native';
import {
  startJourney,
  LivenessProvider,
  Environment,
  type OitiTheme,
} from '@oiti/rn-sdk';

const customTheme: OitiTheme = {
  provider: LivenessProvider.FACETEC,
  facetec: {
    colors: {
      frameBackground: '#1A1A1A',
      frameBorder: '#FF6B35',
      ovalStroke: '#FF6B35',
      ovalProgressFirst: '#FF6B35',
      ovalProgressSecond: '#FFD700',
      guidanceButtonBackgroundNormal: '#FF6B35',
      guidanceButtonTextNormal: '#FFFFFF',
    },
    texts: {
      readyHeader1: 'Prepare-se',
      readyHeader2: 'para a verificação',
      readyMessage1: 'Posicione seu rosto',
      readyMessage2: 'dentro do círculo',
      readyButton: 'Começar',
      feedbackCenterFace: 'Centralize seu rosto',
      feedbackHoldSteady: 'Mantenha-se parado',
    },
  },
  instructions: {
    colors: {
      background: '#2E2E2E',
      title: '#FFFFFF',
      continueButtonBackground: '#FF6B35',
    },
    texts: {
      title: 'Verificação de Identidade',
      caption: 'Siga as instruções',
      continueButton: 'Continuar',
    },
  },
  result: {
    colors: {
      successBackground: '#E8F5E8',
      successText: '#2E7D32',
      errorBackground: '#FFEBEE',
      errorText: '#C62828',
    },
    texts: {
      success: 'Verificação concluída com sucesso!',
      error: 'Erro na verificação. Tente novamente.',
    },
  },
};

export default function ThemedVerification() {
  const handleStart = async () => {
    const appKey = 'your-app-key-here';
    const result = await startJourney(appKey, Environment.PRD, true, customTheme);
    console.log('Result:', result);
  };

  return <Button title="Iniciar com Tema Custom" onPress={handleStart} />;
}
```

---

### Exemplo com TypeScript e Tratamento de Erros

```typescript
import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import { startJourney, Environment } from '@oiti/rn-sdk';

interface VerificationResult {
  valid: boolean;
  codID?: string;
  cause: string;
  protocol?: string;
}

export default function VerificationComponent() {
  const [result, setResult] = useState<VerificationResult | null>(null);
  const [loading, setLoading] = useState(false);

  const handleVerification = async () => {
    setLoading(true);
    setResult(null);

    try {
      const appKey = process.env.OITI_APP_KEY || 'your-app-key';
      const response = await startJourney(appKey, Environment.HML);

      const data: VerificationResult = JSON.parse(response);
      setResult(data);

      if (data.valid) {
        Alert.alert('✅ Sucesso', 'Identidade verificada!');
      } else {
        Alert.alert('❌ Falha', `Motivo: ${data.cause}`);
      }
    } catch (error) {
      Alert.alert('Erro', `Não foi possível completar: ${error.message}`);
      setResult({
        valid: false,
        cause: error.message,
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={handleVerification}
        disabled={loading}
        >
        <Text style={styles.buttonText}>
          {loading ? 'Verificando...' : 'Iniciar Verificação'}
        </Text>
      </TouchableOpacity>

      {result && (
        <View style={styles.resultContainer}>
          <Text style={styles.resultTitle}>Resultado:</Text>
          <Text>Status: {result.valid ? '✅ Aprovado' : '❌ Reprovado'}</Text>
          <Text>Motivo: {result.cause}</Text>
          {result.protocol && <Text>Protocolo: {result.protocol}</Text>}
          {result.codID && <Text>Código: {result.codID}</Text>}
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 20,
  },
  button: {
    backgroundColor: '#007AFF',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
  },
  buttonDisabled: {
    backgroundColor: '#cccccc',
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
  resultContainer: {
    marginTop: 20,
    padding: 15,
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
  },
  resultTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
});
```

**Possíveis Resultados:**

✅ **Sucesso:**

```json
{
  "valid": true,
  "codID": "abc123def456",
  "cause": "Approved",
  "protocol": "20231105-001"
}
```

❌ **Usuário Cancelou:**

```json
{
  "valid": false,
  "cause": "User cancelled",
  "protocol": null
}
```

❌ **Falha na Verificação:**

```json
{
  "valid": false,
  "cause": "Liveness check failed",
  "protocol": "20231105-002"
}
```

❌ **Timeout:**

```json
{
  "valid": false,
  "cause": "Session timeout",
  "protocol": null
}
```

---

## 🎭 Tema Completo

### Exemplo de Tema Personalizado Completo

```typescript
const customTheme: OitiTheme = {
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
        resultScreenCustomActivityIndicatorImage: 'resultScreenCustomActivityIndicatorImage',
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

- ✅ Verificação de liveness com FaceTec e iProov
- ✅ Gerenciamento automático de permissões
- ✅ Suporte completo a temas personalizados
- ✅ Interface TypeScript com tipagem completa
- ✅ Suporte para Android e iOS
- ✅ Integração com TurboModules para performance otimizada
- ✅ Callbacks de sucesso e erro
- ✅ Compatível com React Native 0.60+

## 📋 Requisitos

- React Native ≥ 0.79
- Android API ≥ 26
- iOS ≥ 12.0
- TypeScript ≥ 4.0 (recomendado)

## 🔗 Links Úteis

- [Changelog](https://github.com/oititec/rn-sdk/releases)



## 📄 Licença

MIT © [Oiti](https://github.com/oititec)

---

<div align="center">

**Feito com ❤️ pela equipe Oiti**

</div>
