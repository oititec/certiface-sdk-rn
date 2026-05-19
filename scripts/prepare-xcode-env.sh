#!/usr/bin/env bash
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
IOS_DIR="$REPO_ROOT/example/ios"
XCODE_ENV_LOCAL="$IOS_DIR/.xcode.env.local"

resolve_node_binary() {
  if [[ -n "${NODE_BINARY:-}" && -x "${NODE_BINARY}" ]]; then
    printf '%s' "$NODE_BINARY"
    return 0
  fi
  local candidate
  candidate="$(command -v node 2>/dev/null || true)"
  if [[ -n "$candidate" && -x "$candidate" ]]; then
    printf '%s' "$candidate"
    return 0
  fi
  for candidate in /opt/homebrew/bin/node /usr/local/bin/node; do
    if [[ -x "$candidate" ]]; then
      printf '%s' "$candidate"
      return 0
    fi
  done
  local hosted
  for hosted in /Users/runner/hostedtoolcache/node/*/arm64/bin/node \
    /Users/runner/hostedtoolcache/node/*/x64/bin/node; do
    if [[ -x "$hosted" ]]; then
      printf '%s' "$hosted"
      return 0
    fi
  done
  return 1
}

NODE_BIN="$(resolve_node_binary)" || {
  echo "::error::node não encontrado para scripts do Xcode (Bundle React Native)."
  exit 1
}

RN_ABS="$(cd "$REPO_ROOT/example/node_modules/react-native" && pwd)"
{
  printf 'export NODE_BINARY=%s\n' "$NODE_BIN"
  printf 'export REACT_NATIVE_PATH=%s\n' "$RN_ABS"
} > "$XCODE_ENV_LOCAL"
echo "NODE_BINARY=$NODE_BIN"
echo "REACT_NATIVE_PATH=$RN_ABS"

node "$REPO_ROOT/example/scripts/patch-react-native-with-environment-sh.js"
node "$REPO_ROOT/example/scripts/fix-react-native-fmt-podspecs.js"
