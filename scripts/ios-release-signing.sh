#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TEAM_ID="${APPLE_TEAM_ID:-KRCZ6X7U8S}"
BUNDLE_ID="${IOS_BUNDLE_ID:-br.com.oititec.rncertifacesdk.example}"
PROFILE_JSON_FILE="$REPO_ROOT/example/ios/.provisioning-profile.json"
EXPORT_OPTIONS_PLIST="$REPO_ROOT/example/ios/ExportOptions.plist"

read_profile_json_field() {
  local field="$1"
  if [[ ! -f "$PROFILE_JSON_FILE" ]]; then
    return 1
  fi
  node -p "require($(printf '%q' "$PROFILE_JSON_FILE")).$field"
}

load_profile_name() {
  PROFILE_NAME="$(read_profile_json_field name 2>/dev/null || true)"
  [[ -n "$PROFILE_NAME" ]]
}

write_export_options_plist() {
  mkdir -p "$(dirname "$EXPORT_OPTIONS_PLIST")"
  if load_profile_name; then
    node -e "
const fs = require('fs');
const teamId = process.env.TEAM_ID;
const bundleId = process.env.BUNDLE_ID;
const profileName = process.env.PROFILE_NAME;
const plist = \`<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">
<plist version=\"1.0\">
<dict>
  <key>method</key>
  <string>app-store</string>
  <key>signingStyle</key>
  <string>manual</string>
  <key>teamID</key>
  <string>\${teamId}</string>
  <key>provisioningProfiles</key>
  <dict>
    <key>\${bundleId}</key>
    <string>\${profileName.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')}</string>
  </dict>
</dict>
</plist>\`;
fs.writeFileSync(process.env.EXPORT_OPTIONS_PLIST, plist);
" TEAM_ID="$TEAM_ID" BUNDLE_ID="$BUNDLE_ID" PROFILE_NAME="$PROFILE_NAME" EXPORT_OPTIONS_PLIST="$EXPORT_OPTIONS_PLIST"
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
  printf '%s\n' "DEVELOPMENT_TEAM=$TEAM_ID"
  if load_profile_name; then
    printf '%s\n' "CODE_SIGN_STYLE=Manual"
    printf '%s\n' "CODE_SIGN_IDENTITY=Apple Distribution"
    printf '%s\n' "PROVISIONING_PROFILE_SPECIFIER=$PROFILE_NAME"
  else
    printf '%s\n' "CODE_SIGN_STYLE=Automatic"
  fi
}

case "${1:-}" in
  export-plist) write_export_options_plist ;;
  args) xcodebuild_signing_args ;;
  *)
    echo "Uso: $0 export-plist|args"
    exit 1
    ;;
esac
