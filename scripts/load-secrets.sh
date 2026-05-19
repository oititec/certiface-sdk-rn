#!/usr/bin/env bash
load_secrets() {
  local root="${1:?}"
  if [[ -n "${GITHUB_ACTIONS:-}" ]]; then
    return 0
  fi
  if [[ ! -f "$root/.secrets" ]]; then
    return 1
  fi
  local _cur_key="" _cur_val="" _v=""
  while IFS= read -r line || [[ -n "$line" ]]; do
    line="${line%$'\r'}"
    [[ "$line" =~ ^[[:space:]]*# ]] && continue
    [[ -z "${line// }" ]] && continue
    if [[ "$line" =~ ^([A-Za-z_][A-Za-z0-9_]*)=(.*)$ ]]; then
      [[ -n "$_cur_key" ]] && { _v="${_cur_val#\"}"; _v="${_v%\"}"; export "${_cur_key}=${_v}"; }
      _cur_key="${BASH_REMATCH[1]}"
      _cur_val="${BASH_REMATCH[2]}"
    elif [[ -n "$_cur_key" ]]; then
      _cur_val="${_cur_val}"$'\n'"${line}"
    fi
  done < "$root/.secrets"
  [[ -n "$_cur_key" ]] && { _v="${_cur_val#\"}"; _v="${_v%\"}"; export "${_cur_key}=${_v}"; }
  unset _cur_key _cur_val _v
  return 0
}

resolve_appstore_api_key_path() {
  if [[ -n "${APPSTORE_API_KEY_PATH:-}" && -f "${APPSTORE_API_KEY_PATH}" ]]; then
    return 0
  fi
  if [[ -z "${APPSTORE_KEY_ID:-}" ]]; then
    return 1
  fi
  local candidate
  for candidate in \
    "${APPSTORE_API_KEY_PATH:-}" \
    "${HOME}/.appstoreconnect/private_keys/AuthKey_${APPSTORE_KEY_ID}.p8" \
    "${HOME}/.private_keys/AuthKey_${APPSTORE_KEY_ID}.p8" \
    "${TMPDIR:-/tmp}/AuthKey_${APPSTORE_KEY_ID}.p8"; do
    if [[ -n "$candidate" && -f "$candidate" ]]; then
      export APPSTORE_API_KEY_PATH="$candidate"
      return 0
    fi
  done
  return 1
}

has_appstore_upload_credentials() {
  [[ -n "${APPSTORE_KEY_ID:-}" && -n "${APPSTORE_ISSUER_ID:-}" ]] || return 1
  if [[ -n "${APPSTORE_PRIVATE_KEY:-}${APPSTORE_PRIVATE_KEY_BASE64:-}" ]]; then
    return 0
  fi
  resolve_appstore_api_key_path
}

print_appstore_credentials_help() {
  local root="${1:?}"
  cat <<EOF
Credenciais App Store Connect ausentes para upload no TestFlight.

Opção A — arquivo .secrets na raiz do repo (não versionado):
  cp .secrets.example .secrets

  Preencha no .secrets:
    APPSTORE_KEY_ID=XXXXXXXXXX
    APPSTORE_ISSUER_ID=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    APPSTORE_PRIVATE_KEY_BASE64=<saída de: base64 -i AuthKey_XXXXX.p8 | tr -d '\\n'>

  Ou cole a chave multilinha em APPSTORE_PRIVATE_KEY entre aspas.

Opção B — exportar no terminal antes de rodar:
  export APPSTORE_KEY_ID=...
  export APPSTORE_ISSUER_ID=...
  export APPSTORE_API_KEY_PATH=~/caminho/AuthKey_XXXXX.p8

Opção C — só reenviar o IPA já gerado:
  export APPSTORE_KEY_ID=... APPSTORE_ISSUER_ID=... APPSTORE_API_KEY_PATH=...
  ./scripts/upload-testflight.sh "$root/example/ios/build/RnSdkExample.ipa"

Chaves em: App Store Connect → Users and Access → Integrations → App Store Connect API.
EOF
}
