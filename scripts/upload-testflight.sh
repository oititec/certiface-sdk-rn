#!/usr/bin/env bash
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
# shellcheck source=load-secrets.sh
source "$REPO_ROOT/scripts/load-secrets.sh"
IPA_PATH="${1:?Informe o caminho do .ipa}"

if [[ -z "${GITHUB_ACTIONS:-}" ]]; then
  load_secrets "$REPO_ROOT" || true
fi

if ! has_appstore_upload_credentials; then
  print_appstore_credentials_help "$REPO_ROOT"
  exit 1
fi

if [[ -n "${APPSTORE_PRIVATE_KEY:-}${APPSTORE_PRIVATE_KEY_BASE64:-}" ]]; then
  KEYS_DIR="${API_PRIVATE_KEYS_DIR:-${TMPDIR:-/tmp}/appstore_keys}"
  mkdir -p "$KEYS_DIR"
  KEY_FILE="$KEYS_DIR/AuthKey_${APPSTORE_KEY_ID}.p8"
  if [[ -n "${APPSTORE_PRIVATE_KEY_BASE64:-}" ]]; then
    echo "$APPSTORE_PRIVATE_KEY_BASE64" | base64 -d > "$KEY_FILE"
  elif [[ "${APPSTORE_PRIVATE_KEY:-}" == *"BEGIN PRIVATE KEY"* ]]; then
    printf '%s\n' "$APPSTORE_PRIVATE_KEY" > "$KEY_FILE"
  else
    printf '%b' "$APPSTORE_PRIVATE_KEY" > "$KEY_FILE"
  fi
  chmod 600 "$KEY_FILE"
  export API_PRIVATE_KEYS_DIR="$KEYS_DIR"
elif resolve_appstore_api_key_path; then
  KEYS_DIR="$(dirname "$APPSTORE_API_KEY_PATH")"
  export API_PRIVATE_KEYS_DIR="$KEYS_DIR"
  if [[ "$(basename "$APPSTORE_API_KEY_PATH")" =~ ^AuthKey_([A-Za-z0-9]+)\.p8$ ]]; then
    export APPSTORE_KEY_ID="${APPSTORE_KEY_ID:-${BASH_REMATCH[1]}}"
  fi
fi

if ! xcrun --find iTMSTransporter >/dev/null 2>&1; then
  echo "iTMSTransporter não encontrado. Use a pipeline GitHub (apple-actions/upload-testflight-build) ou instale o Transporter."
  exit 1
fi

echo "Enviando $IPA_PATH para TestFlight..."
xcrun iTMSTransporter -m upload \
  -assetFile "$IPA_PATH" \
  -apiKey "$APPSTORE_KEY_ID" \
  -apiIssuer "$APPSTORE_ISSUER_ID" \
  -v informational

echo "Upload concluído. O build deve aparecer no TestFlight em alguns minutos."
