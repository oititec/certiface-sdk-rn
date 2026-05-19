#!/usr/bin/env bash
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT/example"

BUNDLER_VER=""
if [[ -f Gemfile.lock ]]; then
  BUNDLER_VER=$(grep -A1 'BUNDLED WITH' Gemfile.lock 2>/dev/null | tail -1 | tr -d ' ')
fi
if [[ -n "$BUNDLER_VER" ]]; then
  bundle _${BUNDLER_VER}_ install --quiet || bundle install --quiet
else
  bundle install --quiet
fi

if [[ -n "${GITHUB_ACTIONS:-}" ]]; then
  echo "CI: pod install (CDN + cache; sem pod repo update do Specs.git)"
  bundle exec pod install --project-directory=ios
else
  echo "Local: pod install"
  bundle exec pod install --project-directory=ios
fi
