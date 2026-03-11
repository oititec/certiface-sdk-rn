#!/usr/bin/env bash
set -e
cd "$(dirname "$0")/.."
if [[ ! -f .secrets ]]; then
  echo "Crie .secrets a partir de .secrets.example e preencha os valores."
  exit 1
fi
echo "Rodando pipeline hom (apenas Android; iOS não roda localmente no act)."
act push -e .github/events/push-hom.json -j deploy-android "$@"
