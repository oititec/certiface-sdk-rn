#!/usr/bin/env bash
set -e
cd "$(dirname "$0")/.."
if [[ "$(uname)" != "Darwin" ]]; then
  echo "Pipeline iOS precisa rodar em macOS (Xcode, keychain, code signing)."
  exit 1
fi
if [[ ! -f .secrets ]]; then
  echo "Crie .secrets a partir de .secrets.example e preencha os valores (incluindo iOS/App Store)."
  exit 1
fi
echo "Rodando pipeline hom – job iOS (build + export IPA + upload TestFlight)."
act push -e .github/events/push-hom.json -j deploy-ios -P macos-14=-self-hosted "$@"
