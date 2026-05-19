#!/usr/bin/env bash
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT"
node "$REPO_ROOT/scripts/resolve-deploy-version.mjs" "$@"
if [[ -f example/deploy-version.env ]]; then
  set -a
  # shellcheck source=/dev/null
  source example/deploy-version.env
  set +a
fi
