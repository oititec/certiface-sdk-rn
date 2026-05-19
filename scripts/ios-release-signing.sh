#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TEAM_ID="${APPLE_TEAM_ID:-KRCZ6X7U8S}"
BUNDLE_ID="${IOS_BUNDLE_ID:-br.com.oititec.rncertifacesdk.example}"
PROFILE_ENV_FILE="$REPO_ROOT/example/ios/.provisioning-profile.env"
EXPORT_OPTIONS_PLIST="$REPO_ROOT/example/ios/ExportOptions.plist"

load_profile_env() {
  if [[ -f "$PROFILE_ENV_FILE" ]]; then
    set -a
    # shellcheck source=/dev/null
    source "$PROFILE_ENV_FILE"
    set +a
    return 0
  fi
  return 1
}

write_export_options_plist() {
  mkdir -p "$(dirname "$EXPORT_OPTIONS_PLIST")"
  if load_profile_env && [[ -n "${PROFILE_NAME:-}" ]]; then
    printf '%s\n' '<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <key>method</key>
  <string>app-store</string>
  <key>signingStyle</key>
  <string>manual</string>
  <key>teamID</key>
  <string>'"$TEAM_ID"'</string>
  <key>provisioningProfiles</key>
  <dict>
    <key>'"$BUNDLE_ID"'</key>
    <string>'"$PROFILE_NAME"'</string>
  </dict>
</dict>
</plist>' > "$EXPORT_OPTIONS_PLIST"
  else
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
</plist>' > "$EXPORT_OPTIONS_PLIST"
  fi
}

xcodebuild_signing_args() {
  local -a args=(
    "DEVELOPMENT_TEAM=$TEAM_ID"
    "CODE_SIGN_IDENTITY=Apple Distribution"
  )
  if load_profile_env && [[ -n "${PROFILE_NAME:-}" ]]; then
    args+=(
      "CODE_SIGN_STYLE=Manual"
      "PROVISIONING_PROFILE_SPECIFIER=$PROFILE_NAME"
    )
  else
    args+=("CODE_SIGN_STYLE=Automatic")
  fi
  printf '%s\n' "${args[@]}"
}

case "${1:-}" in
  export-plist) write_export_options_plist ;;
  args) xcodebuild_signing_args ;;
  *)
    echo "Uso: $0 export-plist|args"
    exit 1
    ;;
esac
