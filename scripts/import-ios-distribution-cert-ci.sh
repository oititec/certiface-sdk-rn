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

if [[ -z "${P12_BASE64:-}" ]]; then
  if [[ -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_ISSUER_ID:-}" && -n "${APPSTORE_PRIVATE_KEY:-}" ]]; then
    echo "IOS_CERTIFICATES_P12_BASE64 vazio; assinatura automática via API App Store Connect."
    write_outputs "api-key"
    exit 0
  fi
  echo "::error::Configure IOS_CERTIFICATES_P12_BASE64 + IOS_CERTIFICATES_P12_PASSWORD ou APPSTORE_KEY_ID + APPSTORE_ISSUER_ID + APPSTORE_PRIVATE_KEY."
  exit 1
fi

P12_PATH="${RUNNER_TEMP:-/tmp}/ios_distribution.p12"
CLEAN_BASE64="$(printf '%s' "$P12_BASE64" | tr -d '[:space:]')"

if ! printf '%s' "$CLEAN_BASE64" | base64 -D > "$P12_PATH" 2>/dev/null; then
  echo "::error::IOS_CERTIFICATES_P12_BASE64 inválido (não é base64). Regenere com: base64 -i AppleDistribution.p12 | tr -d '\\n' | pbcopy"
  exit 1
fi

if [[ ! -s "$P12_PATH" ]]; then
  echo "::error::Arquivo P12 decodificado está vazio."
  exit 1
fi

if ! openssl pkcs12 -in "$P12_PATH" -noout -passin "pass:${P12_PASSWORD:-}" 2>/dev/null; then
  echo "::error::P12 inválido ou IOS_CERTIFICATES_P12_PASSWORD incorreta."
  echo "Exporte de novo no Acesso às Chaves: certificado Apple Distribution + chave privada → Exportar 2 itens → .p12"
  echo "Base64: base64 -i AppleDistribution.p12 | tr -d '\\n' | pbcopy"
  exit 1
fi

security create-keychain -p "$KEYCHAIN_PASSWORD" "$KEYCHAIN_NAME"
security set-keychain-settings -lut 21600 "$KEYCHAIN_NAME"
security unlock-keychain -p "$KEYCHAIN_PASSWORD" "$KEYCHAIN_NAME"
security import "$P12_PATH" -k "$KEYCHAIN_NAME" -P "${P12_PASSWORD:-}" -T /usr/bin/codesign -T /usr/bin/security -T /usr/bin/xcodebuild -A
security set-key-partition-list -S apple-tool:,apple:,codesign: -s -k "$KEYCHAIN_PASSWORD" "$KEYCHAIN_NAME" || true
security list-keychains -d user -s "$KEYCHAIN_NAME" "$HOME/Library/Keychains/login.keychain-db" 2>/dev/null || security list-keychains -d user -s "$KEYCHAIN_NAME" login.keychain
security default-keychain -s "$KEYCHAIN_NAME"
security find-identity -v -p codesigning

echo "Certificado Apple Distribution importado em $KEYCHAIN_NAME"
write_outputs "p12"
