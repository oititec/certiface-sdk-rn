#!/usr/bin/env bash
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT/example"

BUNDLER_VER=""
if [[ -f Gemfile.lock ]]; then
  BUNDLER_VER=$(grep -A1 'BUNDLED WITH' Gemfile.lock 2>/dev/null | tail -1 | tr -d ' ')
fi
if [[ -n "$BUNDLER_VER" ]] && ! gem list bundler -i -v "$BUNDLER_VER" >/dev/null 2>&1; then
  gem install bundler -v "$BUNDLER_VER" --no-document
fi
if [[ -n "$BUNDLER_VER" ]]; then
  bundle _${BUNDLER_VER}_ install --quiet || bundle install --quiet
else
  bundle install --quiet
fi

run_pod_install() {
  bundle exec pod install --project-directory=ios "$@"
}

echo "pod install (oititec + CDN trunk)"
if ! run_pod_install; then
  echo "pod install falhou; sincronizando CertifaceSDK com RnSdk.podspec..."
  bundle exec pod update CertifaceSDK --project-directory=ios
fi
