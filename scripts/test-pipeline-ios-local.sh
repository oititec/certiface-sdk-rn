#!/usr/bin/env bash
# Replica localmente os passos do job deploy-ios (.github/workflows/deploy-hom.yml).
# Alternativa com act (workflow real): ./scripts/act-hom-ios.sh
#
# Variáveis de ambiente (opcionais):
#   APPLE_TEAM_ID          - Team ID (default: KRCZ6X7U8S)
#   APPSTORE_KEY_ID        - App Store Connect API Key ID
#   APPSTORE_ISSUER_ID     - App Store Connect Issuer ID
#   APPSTORE_PRIVATE_KEY   - Conteúdo do .p8 (permite archive/export e --upload-testflight)
#
# Uso: ./scripts/test-pipeline-ios-local.sh [--build-only] [--upload-testflight]
#   --build-only         para após o archive (não exporta IPA)
#   --upload-testflight  ao final envia o IPA para TestFlight (exige chave API)
set -e
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT"

if [[ "$(uname)" != "Darwin" ]]; then
  echo "Este script replica o job deploy-ios e precisa rodar em macOS (Xcode)."
  exit 1
fi

TEAM_ID="${APPLE_TEAM_ID:-KRCZ6X7U8S}"
BUILD_ONLY=false
UPLOAD_TESTFLIGHT=false
for arg in "$@"; do
  case "$arg" in
    --build-only) BUILD_ONLY=true ;;
    --upload-testflight) UPLOAD_TESTFLIGHT=true ;;
  esac
done

if [[ -z "${GITHUB_ACTIONS:-}" ]]; then
  echo "[1/7] Setup Node e dependências (espelho do .github/actions/setup)"
  if [[ -f .nvmrc ]]; then
    export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
    if [[ -s "$NVM_DIR/nvm.sh" ]]; then
      source "$NVM_DIR/nvm.sh"
      nvm use 2>/dev/null || true
    fi
  fi
  yarn install --immutable
else
  echo "[1/7] CI: pulando yarn install (já feito pelo Setup)."
fi

echo "[2/7] CocoaPods (example)"
cd "$REPO_ROOT/example"
bundle install
bundle exec pod repo update --verbose
bundle exec pod install --project-directory=ios
cd "$REPO_ROOT"

echo "[3/7] App Store Connect API key (opcional)"
APPSTORE_API_KEY_PATH=""
if [[ -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_PRIVATE_KEY:-}" ]]; then
  APPSTORE_API_KEY_PATH="${TMPDIR:-/tmp}/AuthKey_${APPSTORE_KEY_ID}.p8"
  printf '%s' "$APPSTORE_PRIVATE_KEY" > "$APPSTORE_API_KEY_PATH"
  chmod 600 "$APPSTORE_API_KEY_PATH"
  echo "Chave API gravada em $APPSTORE_API_KEY_PATH"
else
  echo "APPSTORE_KEY_ID/APPSTORE_PRIVATE_KEY não definidos; archive/export usarão conta do Xcode."
fi

echo "[4/7] ExportOptions.plist"
mkdir -p example/ios
printf '%s\n' '<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <key>method</key>
  <string>app-store</string>
  <key>signingStyle</key>
  <string>automatic</string>
  <key>teamID</key>
  <string>'"$TEAM_ID"'</string>
</dict>
</plist>' > example/ios/ExportOptions.plist

echo "[5/7] Build settings (Release)"
cd "$REPO_ROOT/example/ios"
xcodebuild -workspace RnSdkExample.xcworkspace \
  -scheme RnSdkExample \
  -configuration Release \
  -showBuildSettings \
  DEVELOPMENT_TEAM="$TEAM_ID" 2>/dev/null | grep -E 'PRODUCT_BUNDLE_IDENTIFIER|DEVELOPMENT_TEAM|CODE_SIGN|PROVISIONING_PROFILE|OTHER_CODE_SIGN_FLAGS' || true
cd "$REPO_ROOT"

echo "[6/7] Build and archive iOS"
cd "$REPO_ROOT/example/ios"
ARCHIVE_ARGS=(
  -workspace RnSdkExample.xcworkspace
  -scheme RnSdkExample
  -configuration Release
  -destination 'generic/platform=iOS'
  -archivePath build/RnSdkExample.xcarchive
  -allowProvisioningUpdates
  DEVELOPMENT_TEAM="$TEAM_ID"
)
if [[ -n "$APPSTORE_API_KEY_PATH" && -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_ISSUER_ID:-}" ]]; then
  ARCHIVE_ARGS+=(
    -authenticationKeyPath "$APPSTORE_API_KEY_PATH"
    -authenticationKeyID "$APPSTORE_KEY_ID"
    -authenticationKeyIssuerID "$APPSTORE_ISSUER_ID"
  )
fi
xcodebuild "${ARCHIVE_ARGS[@]}" archive
cd "$REPO_ROOT"

if [[ "$BUILD_ONLY" == "true" ]]; then
  echo "Parado após archive (--build-only). Archive: example/ios/build/RnSdkExample.xcarchive"
  exit 0
fi

echo "[7/7] Export IPA"
cd "$REPO_ROOT/example/ios"
EXPORT_ARGS=(
  -exportArchive
  -archivePath build/RnSdkExample.xcarchive
  -exportPath build
  -exportOptionsPlist ExportOptions.plist
  -allowProvisioningUpdates
)
if [[ -n "$APPSTORE_API_KEY_PATH" && -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_ISSUER_ID:-}" ]]; then
  EXPORT_ARGS+=(
    -authenticationKeyPath "$APPSTORE_API_KEY_PATH"
    -authenticationKeyID "$APPSTORE_KEY_ID"
    -authenticationKeyIssuerID "$APPSTORE_ISSUER_ID"
  )
fi
xcodebuild "${EXPORT_ARGS[@]}"
cd "$REPO_ROOT"

IPA_PATH="$REPO_ROOT/example/ios/build/RnSdkExample.ipa"
if [[ ! -f "$IPA_PATH" ]]; then
  IPA_OTHER=$(find "$REPO_ROOT/example/ios/build" -name "*.ipa" -type f 2>/dev/null | head -1)
  if [[ -n "$IPA_OTHER" ]]; then
    IPA_PATH="$IPA_OTHER"
  else
    echo "Nenhum .ipa encontrado em example/ios/build."
    exit 1
  fi
fi
echo "IPA gerado: $IPA_PATH"

if [[ "$UPLOAD_TESTFLIGHT" == "true" ]]; then
  if [[ -z "${APPSTORE_KEY_ID:-}" || -z "${APPSTORE_ISSUER_ID:-}" || -z "$APPSTORE_API_KEY_PATH" || ! -f "$APPSTORE_API_KEY_PATH" ]]; then
    echo "Erro: --upload-testflight exige APPSTORE_KEY_ID, APPSTORE_ISSUER_ID e APPSTORE_PRIVATE_KEY (e chave .p8 gerada no passo 3)."
    exit 1
  fi
  echo "[8/8] Upload para TestFlight"
  API_KEYS_DIR="$(dirname "$APPSTORE_API_KEY_PATH")"
  export API_PRIVATE_KEYS_DIR="$API_KEYS_DIR"
  xcrun altool --upload-app -f "$IPA_PATH" -t ios --apiKey "$APPSTORE_KEY_ID" --apiIssuer "$APPSTORE_ISSUER_ID"
  echo "Upload concluído. O build deve aparecer em TestFlight em alguns minutos."
fi
