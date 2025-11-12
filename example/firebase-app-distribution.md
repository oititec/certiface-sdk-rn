# Firebase App Distribution - Configuração

## Comandos Disponíveis

### Scripts NPM

```bash
# Build apenas (sem distribuir)
npm run build:android:debug    # Build debug APK
npm run build:android:release  # Build release APK

# Build + Distribuição para App Tester
npm run distribute:android:debug    # Build e distribui versão debug
npm run distribute:android:release  # Build e distribui versão release
npm run distribute:android          # Script automatizado (debug por padrão)
```

### Script Automatizado

```bash
# Usar debug (padrão)
./scripts/distribute-android.sh

# Usar release
./scripts/distribute-android.sh release

# Com notas de release customizadas
./scripts/distribute-android.sh debug "Nova funcionalidade X implementada"
./scripts/distribute-android.sh release "Versão 1.2.0 - Correções de bugs"
```

## Configuração Inicial

### 1. Autenticação Firebase CLI

```bash
# Instalar Firebase CLI (se não tiver)
npm install -g firebase-tools

# Fazer login
firebase login

# Configurar o projeto (executar na pasta example/)
firebase use rn-certiface-sdk
```

### 2. Configurar Grupos de Testadores

No Firebase Console:
1. Acesse App Distribution
2. Vá em "Testers & Groups"
3. Crie um grupo chamado "testers"
4. Adicione os emails dos testadores

### 3. Variáveis de Ambiente (Opcional)

Criar arquivo `.env` na pasta example/:

```
FIREBASE_APP_ID=1:506835227883:android:d174ede2854db1d04c1aae
FIREBASE_PROJECT_ID=rn-certiface-sdk
```

## Arquivos Configurados

- `android/build.gradle` - Plugins do Firebase
- `android/app/build.gradle` - Configuração do App Distribution
- `android/app/google-services.json` - Configuração do projeto Firebase
- `package.json` - Scripts NPM
- `scripts/distribute-android.sh` - Script automatizado

## Troubleshooting

### Erro de autenticação
```bash
firebase login --reauth
```

### Erro de permissões
```bash
chmod +x scripts/distribute-android.sh
```

### Verificar configuração
```bash
firebase projects:list
firebase apps:list android
```

