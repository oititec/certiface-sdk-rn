#!/usr/bin/env bash
# Instala perfis de provisionamento (.mobileprovision) no sistema.
# Na pipeline (CI): usa secret APPLE_MOBILE_PROFILE_CERTIFICATE_BASE64.
# Local: usa pasta com .mobileprovision (default ~/Downloads) e imprime instruções do Keychain.
#
# Uso: ./scripts/install-ios-provisioning-and-keychain.sh [pasta]
set -e
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
PROVISIONING_DEST="$HOME/Library/MobileDevice/Provisioning Profiles"
mkdir -p "$PROVISIONING_DEST"

if [[ "$(uname)" != "Darwin" ]]; then
  echo "Só funciona em macOS."
  exit 1
fi

count=0

BASE64_PROFILE="${APPLE_MOBILE_PROFILE_CERTIFICATE_BASE64:-${IOS_PROVISIONING_PROFILE_APPSTORE_BASE64:-}}"
if [[ -n "$BASE64_PROFILE" ]]; then
  echo "Instalando perfil App Store a partir da secret (CI)."
  tmp=$(mktemp).mobileprovision
  echo "$BASE64_PROFILE" | base64 -d > "$tmp"
  dest="$PROVISIONING_DEST/Certiface_SDK_RN_Apple_Store.mobileprovision"
  cp "$tmp" "$dest"
  rm -f "$tmp"
  echo "  Instalado: Certiface_SDK_RN_Apple_Store.mobileprovision"
  count=1
fi

PROFILES_DIR="${1:-$HOME/Downloads}"
if [[ -d "$PROFILES_DIR" ]]; then
  echo "Procurando .mobileprovision em: $PROFILES_DIR"
  while IFS= read -r -d '' f; do
    name=$(basename "$f")
    dest="$PROVISIONING_DEST/$name"
    cp "$f" "$dest"
    echo "  Instalado: $name"
    ((count++)) || true
  done < <(find "$PROFILES_DIR" -maxdepth 1 -name "*.mobileprovision" -print0 2>/dev/null)
fi

if [[ $count -eq 0 ]]; then
  if [[ -n "${GITHUB_ACTIONS:-}" ]]; then
    echo "Nenhum perfil em APPLE_MOBILE_PROFILE_CERTIFICATE_BASE64; Xcode pode usar -allowProvisioningUpdates."
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
  echo "5. Secret no GitHub: base64 -i seu.p12 | pbcopy → IOS_CERTIFICATES_P12_BASE64"
  echo "   E crie IOS_CERTIFICATES_P12_PASSWORD com a senha do .p12."
  echo ""
fi
