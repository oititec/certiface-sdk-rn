#!/usr/bin/env bash
set -e
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
PROVISIONING_DEST="$HOME/Library/MobileDevice/Provisioning Profiles"
PROFILE_ENV_FILE="$REPO_ROOT/example/ios/.provisioning-profile.env"
mkdir -p "$PROVISIONING_DEST"

if [[ "$(uname)" != "Darwin" ]]; then
  echo "Só funciona em macOS."
  exit 1
fi

read_profile_metadata() {
  local profile_path="$1"
  local plist
  plist=$(security cms -D -i "$profile_path" 2>/dev/null) || return 1
  PROFILE_UUID=$(printf '%s' "$plist" | plutil -extract UUID raw - 2>/dev/null || true)
  PROFILE_NAME=$(printf '%s' "$plist" | plutil -extract Name raw - 2>/dev/null || true)
  [[ -n "$PROFILE_UUID" && -n "$PROFILE_NAME" ]]
}

install_profile_file() {
  local source_path="$1"
  read_profile_metadata "$source_path" || return 1
  local dest="$PROVISIONING_DEST/${PROFILE_UUID}.mobileprovision"
  cp "$source_path" "$dest"
  printf '%s\n' \
    "PROFILE_UUID=$PROFILE_UUID" \
    "PROFILE_NAME=$PROFILE_NAME" \
    "PROFILE_PATH=$dest" > "$PROFILE_ENV_FILE"
  echo "  Instalado: $PROFILE_NAME ($PROFILE_UUID)"
}

count=0

BASE64_PROFILE="${APPLE_MOBILE_PROFILE_CERTIFICATE_BASE64:-${IOS_PROVISIONING_PROFILE_APPSTORE_BASE64:-}}"
if [[ -n "$BASE64_PROFILE" ]]; then
  echo "Instalando perfil App Store a partir da secret (CI)."
  tmp=$(mktemp).mobileprovision
  echo "$BASE64_PROFILE" | base64 -D > "$tmp" 2>/dev/null || echo "$BASE64_PROFILE" | base64 -d > "$tmp"
  install_profile_file "$tmp"
  rm -f "$tmp"
  count=1
fi

PROFILES_DIR="${1:-$HOME/Downloads}"
if [[ -d "$PROFILES_DIR" ]]; then
  echo "Procurando .mobileprovision em: $PROFILES_DIR"
  while IFS= read -r -d '' f; do
    install_profile_file "$f" || continue
    ((count++)) || true
    break
  done < <(find "$PROFILES_DIR" -maxdepth 1 -name "*.mobileprovision" -print0 2>/dev/null)
fi

if [[ $count -eq 0 ]]; then
  if [[ -n "${GITHUB_ACTIONS:-}" ]]; then
    echo "Nenhum perfil em APPLE_MOBILE_PROFILE_CERTIFICATE_BASE64; archive usará assinatura automática (API key)."
    exit 0
  fi
  echo "Nenhum perfil instalado. Passe uma pasta com .mobileprovision ou defina APPLE_MOBILE_PROFILE_CERTIFICATE_BASE64 (CI)."
  exit 1
fi

echo "Total: $count perfil(is) em $PROVISIONING_DEST"

if [[ -z "${GITHUB_ACTIONS:-}" ]]; then
  echo ""
  echo "--- Certificado no Keychain (para CI / secret IOS_CERTIFICATES_P12_BASE64) ---"
  echo "1. Abra Acesso às Chaves (Keychain Access)."
  echo "2. Em 'Categoria', selecione 'Certificados'."
  echo "3. Localize o certificado 'Apple Distribution' do seu time."
  echo "4. Expanda e selecione a chave privada → Exportar 2 itens... → .p12 e senha."
  echo "5. ./scripts/encode-ios-p12-secret.sh seu.p12"
  echo "   Cole a linha em IOS_CERTIFICATES_P12_BASE64 (sem quebra de linha no final)."
  echo "   Crie IOS_CERTIFICATES_P12_PASSWORD com a senha do .p12."
  echo ""
fi
