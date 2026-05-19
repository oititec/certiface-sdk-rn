#!/usr/bin/env bash
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT"

if [[ -f example/deploy-version.json ]]; then
  VERSION_NAME="$(node -p "require('./example/deploy-version.json').versionName")"
  VERSION_CODE="$(node -p "require('./example/deploy-version.json').versionCode")"
  VERSION_LABEL="$(node -p "require('./example/deploy-version.json').versionLabel")"
elif [[ -f example/deploy-version.env ]]; then
  set -a
  # shellcheck source=/dev/null
  source example/deploy-version.env
  set +a
else
  VERSION_NAME="$(node -p "require('./example/package.json').version")"
  VERSION_CODE="$(node -p "require('./example/package.json').versionCode")"
fi

cd "$REPO_ROOT/example/ios"
agvtool new-marketing-version "$VERSION_NAME" >/dev/null
agvtool new-version -all "$VERSION_CODE" >/dev/null

echo "iOS: ${VERSION_LABEL:-$VERSION_NAME ($VERSION_CODE)} → build $VERSION_CODE"
