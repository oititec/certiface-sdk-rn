#!/usr/bin/env bash
set -euo pipefail
P12_FILE="${1:?Uso: ./scripts/encode-ios-p12-secret.sh caminho/AppleDistribution.p12}"

if [[ ! -f "$P12_FILE" ]]; then
  echo "Arquivo não encontrado: $P12_FILE"
  exit 1
fi

if ! openssl pkcs12 -in "$P12_FILE" -noout 2>/dev/null; then
  echo "Arquivo não é um P12 válido (ou exige senha; teste com: openssl pkcs12 -in \"$P12_FILE\" -noout)"
  exit 1
fi

echo "Cole em IOS_CERTIFICATES_P12_BASE64 (uma linha, sem quebra no final):"
base64 -i "$P12_FILE" | tr -d '\n'
echo ""
