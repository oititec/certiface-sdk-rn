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

# shellcheck source=load-secrets.sh
source "$REPO_ROOT/scripts/load-secrets.sh"

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
  if ! load_secrets "$REPO_ROOT"; then
    if [[ "$UPLOAD_TESTFLIGHT" == "true" ]]; then
      print_appstore_credentials_help "$REPO_ROOT"
      exit 1
    fi
  elif [[ "$UPLOAD_TESTFLIGHT" == "true" ]] && ! has_appstore_upload_credentials; then
    echo "Arquivo .secrets encontrado, mas faltam credenciais de upload."
    print_appstore_credentials_help "$REPO_ROOT"
    exit 1
  fi
fi

if [[ -z "${GITHUB_ACTIONS:-}" ]]; then
  echo "[1/8] Setup Node e dependências (espelho do .github/actions/setup)"
  if [[ -f .nvmrc ]]; then
    export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
    if [[ -s "$NVM_DIR/nvm.sh" ]]; then
      source "$NVM_DIR/nvm.sh"
      nvm use 2>/dev/null || true
    fi
  fi
  yarn install --immutable
else
  echo "[1/8] CI: pulando yarn install (já feito pelo Setup)."
fi

echo "[2/8] Versão iOS (package.json → Xcode)"
"$REPO_ROOT/scripts/sync-ios-version.sh"

echo "[3/8] CocoaPods (example)"
cd "$REPO_ROOT/example"
BUNDLER_VER=""
if [[ -f Gemfile.lock ]]; then
  BUNDLER_VER=$(grep -A1 'BUNDLED WITH' Gemfile.lock 2>/dev/null | tail -1 | tr -d ' ')
fi
if [[ -n "$BUNDLER_VER" ]]; then
  bundle _${BUNDLER_VER}_ install || bundle install
else
  bundle install
fi
bundle exec pod repo update --verbose
bundle exec pod install --project-directory=ios
cd "$REPO_ROOT"

echo "[4/8] App Store Connect API key (opcional)"
APPSTORE_API_KEY_PATH=""
if [[ -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_PRIVATE_KEY:-}${APPSTORE_PRIVATE_KEY_BASE64:-}" ]]; then
  APPSTORE_API_KEY_PATH="${TMPDIR:-/tmp}/AuthKey_${APPSTORE_KEY_ID}.p8"
  if [[ -n "${APPSTORE_PRIVATE_KEY_BASE64:-}" ]]; then
    echo "$APPSTORE_PRIVATE_KEY_BASE64" | base64 -d > "$APPSTORE_API_KEY_PATH"
  elif [[ "$APPSTORE_PRIVATE_KEY" == *"BEGIN PRIVATE KEY"* ]]; then
    printf '%s\n' "$APPSTORE_PRIVATE_KEY" > "$APPSTORE_API_KEY_PATH"
  else
    printf '%b' "$APPSTORE_PRIVATE_KEY" > "$APPSTORE_API_KEY_PATH"
  fi
  chmod 600 "$APPSTORE_API_KEY_PATH"
  echo "Chave API gravada em $APPSTORE_API_KEY_PATH"
else
  echo "APPSTORE_KEY_ID/APPSTORE_PRIVATE_KEY não definidos; archive/export usarão conta do Xcode."
fi

echo "[5/8] ExportOptions.plist"
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

echo "[6/8] Build settings (Release)"
cd "$REPO_ROOT/example/ios"
xcodebuild -workspace RnSdkExample.xcworkspace \
  -scheme RnSdkExample \
  -configuration Release \
  -showBuildSettings \
  DEVELOPMENT_TEAM="$TEAM_ID" 2>/dev/null | grep -E 'PRODUCT_BUNDLE_IDENTIFIER|DEVELOPMENT_TEAM|CODE_SIGN|PROVISIONING_PROFILE|OTHER_CODE_SIGN_FLAGS' || true
cd "$REPO_ROOT"

echo "[7/8] Build and archive iOS"
cd "$REPO_ROOT/example/ios"
if [[ -n "$APPSTORE_API_KEY_PATH" && -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_ISSUER_ID:-}" ]]; then
  xcodebuild -workspace RnSdkExample.xcworkspace -scheme RnSdkExample -configuration Release \
    -destination 'generic/platform=iOS' -archivePath build/RnSdkExample.xcarchive \
    -allowProvisioningUpdates \
    -authenticationKeyPath "$APPSTORE_API_KEY_PATH" \
    -authenticationKeyID "$APPSTORE_KEY_ID" \
    -authenticationKeyIssuerID "$APPSTORE_ISSUER_ID" \
    DEVELOPMENT_TEAM="$TEAM_ID" \
    archive
else
  xcodebuild -workspace RnSdkExample.xcworkspace -scheme RnSdkExample -configuration Release \
    -destination 'generic/platform=iOS' -archivePath build/RnSdkExample.xcarchive \
    -allowProvisioningUpdates DEVELOPMENT_TEAM="$TEAM_ID" archive
fi
cd "$REPO_ROOT"

if [[ "$BUILD_ONLY" == "true" ]]; then
  echo "Parado após archive (--build-only). Archive: example/ios/build/RnSdkExample.xcarchive"
  exit 0
fi

echo "[8/8] Export IPA"
cd "$REPO_ROOT/example/ios"
if [[ -n "$APPSTORE_API_KEY_PATH" && -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_ISSUER_ID:-}" ]]; then
  xcodebuild -exportArchive -archivePath build/RnSdkExample.xcarchive -exportPath build \
    -exportOptionsPlist ExportOptions.plist -allowProvisioningUpdates \
    -authenticationKeyPath "$APPSTORE_API_KEY_PATH" \
    -authenticationKeyID "$APPSTORE_KEY_ID" \
    -authenticationKeyIssuerID "$APPSTORE_ISSUER_ID"
else
  xcodebuild -exportArchive -archivePath build/RnSdkExample.xcarchive -exportPath build \
    -exportOptionsPlist ExportOptions.plist -allowProvisioningUpdates
fi
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
  if ! has_appstore_upload_credentials; then
    print_appstore_credentials_help "$REPO_ROOT"
    exit 1
  fi
  "$REPO_ROOT/scripts/upload-testflight.sh" "$IPA_PATH"
fi
