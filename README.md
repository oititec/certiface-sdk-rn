# @oiti/rn-sdk

SDK React Native da Oiti para prova de vida (Liveness Detection)

## Descrição

O `@oiti/rn-sdk` é uma biblioteca React Native que integra o OitiSDK para funcionalidades de prova de vida (liveness detection) em aplicações móveis. Esta biblioteca fornece uma interface simples e eficiente para implementar verificação de identidade biométrica.

## Instalação

```sh
npm install @oiti/rn-sdk
```

ou

```sh
yarn add @oiti/rn-sdk
```

## Configuração

### Android

Adicione as permissões necessárias no `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

### iOS

Adicione as permissões necessárias no `ios/YourApp/Info.plist`:

```xml
<key>NSCameraUsageDescription</key>
<string>Esta aplicação precisa acessar a câmera para verificação de identidade</string>
```

## Uso

### Importação

```js
import {
  startJourney,
  checkCameraPermission,
  requestCameraPermission,
} from '@oiti/rn-sdk';
```

### Verificação de Permissões

```js
// Verificar se a permissão da câmera já foi concedida
const hasPermission = await checkCameraPermission();

// Solicitar permissão da câmera
const granted = await requestCameraPermission();
```

### Iniciar Verificação de Liveness

```js
const appKey = 'sua_app_key_aqui';

try {
  const result = await startJourney(appKey);
  console.log('Resultado da verificação:', result);
} catch (error) {
  console.error('Erro na verificação:', error);
}
```

## API

### `startJourney(appKey: string): Promise<string>`

Inicia o processo de verificação de liveness.

**Parâmetros:**

- `appKey` (string): Chave de aplicação fornecida pela Oiti

**Retorna:** Promise que resolve com o resultado da verificação

### `checkCameraPermission(): Promise<boolean>`

Verifica se a permissão da câmera foi concedida.

**Retorna:** Promise que resolve com `true` se a permissão foi concedida, `false` caso contrário

### `requestCameraPermission(): Promise<boolean>`

Solicita permissão da câmera ao usuário.

**Retorna:** Promise que resolve com `true` se a permissão foi concedida, `false` caso contrário

## Exemplo Completo

```jsx
import React, { useState } from 'react';
import { View, Text, TouchableOpacity, Alert } from 'react-native';
import {
  startJourney,
  checkCameraPermission,
  requestCameraPermission,
} from '@oiti/rn-sdk';

export default function LivenessExample() {
  const [isLoading, setIsLoading] = useState(false);

  const handleLivenessCheck = async () => {
    setIsLoading(true);

    try {
      // Verificar permissão da câmera
      const hasPermission = await checkCameraPermission();

      if (!hasPermission) {
        const granted = await requestCameraPermission();
        if (!granted) {
          Alert.alert('Erro', 'Permissão da câmera é necessária');
          return;
        }
      }

      // Iniciar verificação de liveness
      const appKey = 'sua_app_key_aqui';
      const result = await startJourney(appKey);

      Alert.alert('Sucesso', `Verificação concluída: ${result}`);
    } catch (error) {
      Alert.alert('Erro', `Falha na verificação: ${error.message}`);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <TouchableOpacity
        onPress={handleLivenessCheck}
        disabled={isLoading}
        style={{
          backgroundColor: '#007AFF',
          padding: 15,
          borderRadius: 8,
        }}
      >
        <Text style={{ color: 'white', fontSize: 16 }}>
          {isLoading ? 'Verificando...' : 'Iniciar Verificação de Liveness'}
        </Text>
      </TouchableOpacity>
    </View>
  );
}
```

## Funcionalidades

- ✅ Verificação de liveness com OitiSDK
- ✅ Gerenciamento automático de permissões de câmera
- ✅ Suporte para Android e iOS
- ✅ Interface TypeScript
- ✅ Integração com TurboModules para performance otimizada

## Requisitos

- React Native 0.60+
- Android API 21+
- iOS 11.0+

## Suporte

Para suporte técnico, entre em contato com a equipe da Oiti.

## Licença

MIT
