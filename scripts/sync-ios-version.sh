#!/usr/bin/env bash
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT/example/ios"

VERSION_NAME="$(node -p "require('../package.json').version")"
VERSION_CODE="$(node -p "require('../package.json').versionCode")"

agvtool new-marketing-version "$VERSION_NAME" >/dev/null
agvtool new-version -all "$VERSION_CODE" >/dev/null

echo "iOS version: $VERSION_NAME ($VERSION_CODE)"
