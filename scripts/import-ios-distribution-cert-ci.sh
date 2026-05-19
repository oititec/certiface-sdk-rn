#!/usr/bin/env bash
set -euo pipefail

KEYCHAIN_NAME="${KEYCHAIN_NAME:-signing_temp.keychain}"
KEYCHAIN_PASSWORD="${KEYCHAIN_PASSWORD:-$(openssl rand -base64 32 | tr -d '\n')}"

write_outputs() {
  if [[ -n "${GITHUB_OUTPUT:-}" ]]; then
    {
      echo "keychain-name=$KEYCHAIN_NAME"
      echo "keychain-password=$KEYCHAIN_PASSWORD"
      echo "signing-mode=$1"
    } >> "$GITHUB_OUTPUT"
  fi
}

has_api_key_signing() {
  [[ -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_ISSUER_ID:-}" && -n "${APPSTORE_PRIVATE_KEY:-}" ]]
}

verify_p12_file() {
  local p12_path="$1"
  export P12_PASSWORD="${P12_PASSWORD:-}"
  if openssl pkcs12 -in "$p12_path" -noout -passin env:P12_PASSWORD 2>/dev/null; then
    return 0
  fi
  if openssl pkcs12 -in "$p12_path" -noout -passin env:P12_PASSWORD -legacy 2>/dev/null; then
    return 0
  fi
  return 1
}

import_p12_to_keychain() {
  local p12_path="$1"
  security create-keychain -p "$KEYCHAIN_PASSWORD" "$KEYCHAIN_NAME"
  security set-keychain-settings -lut 21600 "$KEYCHAIN_NAME"
  security unlock-keychain -p "$KEYCHAIN_PASSWORD" "$KEYCHAIN_NAME"
  security import "$p12_path" -k "$KEYCHAIN_NAME" -P "${P12_PASSWORD:-}" -T /usr/bin/codesign -T /usr/bin/security -T /usr/bin/xcodebuild -A
  security set-key-partition-list -S apple-tool:,apple:,codesign: -s -k "$KEYCHAIN_PASSWORD" "$KEYCHAIN_NAME" || true
  security list-keychains -d user -s "$KEYCHAIN_NAME" "$HOME/Library/Keychains/login.keychain-db" 2>/dev/null \
    || security list-keychains -d user -s "$KEYCHAIN_NAME" login.keychain
  security default-keychain -s "$KEYCHAIN_NAME"
  security find-identity -v -p codesigning
  echo "Certificado Apple Distribution importado em $KEYCHAIN_NAME"
  write_outputs "p12"
}

if has_api_key_signing; then
  echo "Assinatura via API App Store Connect (APPSTORE_KEY_ID + ISSUER + PRIVATE_KEY)."
  echo "P12 ignorado na CI; para usar P12, remova os secrets APPSTORE_* ou esvazie IOS_CERTIFICATES_P12_BASE64."
  write_outputs "api-key"
  exit 0
fi

if [[ -z "${P12_BASE64:-}" ]]; then
  echo "::error::Configure APPSTORE_KEY_ID + APPSTORE_ISSUER_ID + APPSTORE_PRIVATE_KEY (recomendado) ou IOS_CERTIFICATES_P12_BASE64 + IOS_CERTIFICATES_P12_PASSWORD."
  exit 1
fi

P12_PATH="${RUNNER_TEMP:-/tmp}/ios_distribution.p12"
CLEAN_BASE64="$(printf '%s' "$P12_BASE64" | tr -d '[:space:]')"

if ! printf '%s' "$CLEAN_BASE64" | base64 -D > "$P12_PATH" 2>/dev/null; then
  echo "::error::IOS_CERTIFICATES_P12_BASE64 inválido (não é base64)."
  echo "Regenere: ./scripts/encode-ios-p12-secret.sh AppleDistribution.p12"
  exit 1
fi

if [[ ! -s "$P12_PATH" ]]; then
  echo "::error::Arquivo P12 decodificado está vazio."
  exit 1
fi

if ! verify_p12_file "$P12_PATH"; then
  echo "::error::P12 inválido ou IOS_CERTIFICATES_P12_PASSWORD incorreta."
  echo ""
  echo "Opção recomendada: remova o secret IOS_CERTIFICATES_P12_BASE64 e configure:"
  echo "  APPSTORE_KEY_ID, APPSTORE_ISSUER_ID, APPSTORE_PRIVATE_KEY"
  echo ""
  echo "Se preferir P12:"
  echo "  1. Acesso às Chaves → Apple Distribution + chave privada → Exportar 2 itens"
  echo "  2. ./scripts/encode-ios-p12-secret.sh AppleDistribution.p12"
  echo "  3. IOS_CERTIFICATES_P12_PASSWORD = senha exata da exportação (sem aspas)"
  exit 1
fi

import_p12_to_keychain "$P12_PATH"
