# @certiface/sdk - Aplicativo de Exemplo

Este é um aplicativo de exemplo que demonstra como usar o `@certiface/sdk` para implementar verificação de liveness (prova de vida) em aplicações React Native.

## 📱 Sobre o Aplicativo

O aplicativo de exemplo inclui:

- ✅ Gerenciamento de permissões de câmera
- ✅ Inicialização de jornada de verificação
- ✅ Customização de tema (FaceTec e iProov)
- ✅ Exibição de resultados em tempo real
- ✅ Navegação entre telas (Home e Session)

## 🚀 Como Executar

### Pré-requisitos

Certifique-se de ter completado o guia [Configurar o Ambiente](https://reactnative.dev/docs/set-up-your-environment) antes de prosseguir.

### Passo 1: Instalar Dependências

A partir da raiz do projeto principal:

```bash
yarn install
```

### Passo 2: Configurar iOS (somente macOS)

```bash
cd example/ios
bundle install
bundle exec pod install
cd ../..
```

### Passo 3: Iniciar Metro

```bash
yarn example start
```

### Passo 4: Executar o Aplicativo

Em um novo terminal:

**Android:**
```bash
yarn example android
```

**iOS:**
```bash
yarn example ios
```

## 🎯 Funcionalidades do Exemplo

### 1. Tela Home

- Verificar permissão da câmera
- Solicitar permissão da câmera
- Iniciar verificação de liveness
- Alternar tema customizado (ON/OFF)
- Visualizar resultados em tempo real

### 2. Tela Session

- Inserir dados do usuário (CPF, Nome, Data de Nascimento)
- Gerar App Key para testes
- Visualizar App Key atual

## 💻 Estrutura do Código

```
example/
├── src/
│   ├── App.tsx                     
│   ├── navigation/
│   │   └── AppNavigator.tsx        
│   ├── screens/
│   │   ├── HomeScreen.tsx          
│   │   └── SessionScreen.tsx       
│   └── store/
│       └── userStore.ts            
```

## 🎨 Exemplo de Uso no Código

### Verificação Simples

```typescript
import { CertifaceSDK, Environment, LivenessProvider } from '@certiface/sdk';

const handleVerification = async () => {
  try {
    const result = await CertifaceSDK.startJourney(
      appKey,
      Environment.HML,
      LivenessProvider.IPROOV
    );
    console.log('Resultado:', result);
  } catch (error) {
    console.error('Erro:', error);
  }
};
```

### SaaS

```typescript
const result = await CertifaceSDK.startSaasJourney(
  journeyToken,
  Environment.HML,
  false
);
```

### Com Tema Customizado

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
      frameBorder: '#FF6B35',
      ovalStroke: '#FF6B35',
    },
    texts: {
      readyHeader1: 'Prepare-se',
      readyButton: 'Iniciar',
    },
  },
};

const result = await CertifaceSDK.startJourney(
  appKey,
  Environment.HML,
  LivenessProvider.IPROOV,
  true,
  customTheme
);
```

## 🎨 Configuração de Assets e Fonts

Os assets e fonts referenciados no tema precisam ser cadastrados nativamente em cada plataforma:

### Android

#### Drawable Resources

Os assets devem ser adicionados em `android/app/src/main/res/drawable/`:

```
android/app/src/main/res/
└── drawable/
    ├── shell.png
    └── outros_assets.png
```

Formatos aceitos: `.png`, `.jpg`, `.xml` (vector drawables)

#### Fonts

As fontes devem ser adicionadas em `android/app/src/main/res/font/`:

```
android/app/src/main/res/
└── font/
    ├── sixty.ttf
    └── outras_fontes.ttf
```

Formatos aceitos: `.ttf`, `.otf`

### iOS

#### Assets

Os assets devem ser adicionados em um Asset Catalog (`.xcassets`):

```
ios/RnSdkExample/
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

#### Fonts

As fontes devem ser adicionadas ao projeto e registradas no `Info.plist`:

1. Adicione os arquivos de fonte ao projeto:

```
ios/RnSdkExample/
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

3. No Xcode: arraste os arquivos `.ttf` para o projeto e marque "Copy items if needed" e o target correto.

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

## 📊 Resultados Esperados

### Sucesso
```json
{
  "valid": true,
  "codID": "abc123def456",
  "cause": "Approved",
  "protocol": "20231105-001"
}
```

### Erro
```json
{
  "valid": false,
  "cause": "User cancelled",
  "protocol": null
}
```

## 🔧 Desenvolvimento

### Modo de Desenvolvimento

Para fazer alterações no SDK e testá-las no aplicativo de exemplo:

1. Faça alterações no código do SDK (pasta `src/` na raiz)
2. Execute `yarn prepare` na raiz para recompilar
3. O aplicativo de exemplo irá recarregar automaticamente

### Hot Reload

- **Android**: <kbd>R</kbd> + <kbd>R</kbd> ou <kbd>Ctrl</kbd> + <kbd>M</kbd> → Reload
- **iOS**: <kbd>Cmd ⌘</kbd> + <kbd>R</kbd> no Simulator

## 📝 Notas

- É necessário ter uma **App Key válida** fornecida pela Certiface para testar o SDK
- As permissões de câmera devem ser concedidas para o funcionamento correto
- O tema customizado é opcional e pode ser ativado/desativado na tela Home

## 🐛 Solução de Problemas

### Android

**Erro de compilação:**
```bash
cd example/android
./gradlew clean
cd ../..
yarn example android
```

**Erro de permissões:**
- Verifique se as permissões estão no `AndroidManifest.xml`
- Desinstale e reinstale o aplicativo

### iOS

**Erro no pod install:**
```bash
cd example/ios
bundle exec pod deintegrate
bundle exec pod install
cd ../..
```

**Erro de assinatura:**
- Abra o projeto no Xcode
- Configure sua equipe de desenvolvimento
- Reconstrua o projeto

## 📚 Recursos Úteis

- [Documentação do SDK](../../README.md)
- [React Native Docs](https://reactnative.dev/docs/getting-started)
- [Solução de Problemas](https://reactnative.dev/docs/troubleshooting)

## 📄 Licença

MIT © [Certiface](https://github.com/oititec)
