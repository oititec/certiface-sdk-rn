#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

ANDROID_VERSION=""
IOS_VERSION=""

while [[ $# -gt 0 ]]; do
    case $1 in
        --android)
            ANDROID_VERSION="$2"
            shift 2
            ;;
        --ios)
            IOS_VERSION="$2"
            shift 2
            ;;
        -h|--help)
            echo "Uso: $0 --android <versão> --ios <versão>"
            echo ""
            echo "Opções:"
            echo "  --android <versão>   Versão do SDK Android (oitisdk)"
            echo "  --ios <versão>       Versão do SDK iOS (CertifaceSDK)"
            echo ""
            echo "Exemplo:"
            echo "  $0 --android 1.0.4 --ios 1.1.1"
            exit 0
            ;;
        *)
            echo "Opção desconhecida: $1"
            exit 1
            ;;
    esac
done

if [[ -z "$ANDROID_VERSION" && -z "$IOS_VERSION" ]]; then
    echo "Erro: Informe pelo menos uma versão (--android ou --ios)"
    echo "Use --help para ver as opções disponíveis"
    exit 1
fi

if [[ -n "$ANDROID_VERSION" ]]; then
    GRADLE_FILE="$PROJECT_ROOT/android/build.gradle"
    if [[ -f "$GRADLE_FILE" ]]; then
        sed -i '' "s/implementation 'br.com.oiti:oitisdk:[^']*'/implementation 'br.com.oiti:oitisdk:$ANDROID_VERSION'/" "$GRADLE_FILE"
        echo "✓ Android atualizado para versão $ANDROID_VERSION"
    else
        echo "✗ Arquivo não encontrado: $GRADLE_FILE"
        exit 1
    fi
fi

if [[ -n "$IOS_VERSION" ]]; then
    PODSPEC_FILE="$PROJECT_ROOT/RnSdk.podspec"
    if [[ -f "$PODSPEC_FILE" ]]; then
        sed -i '' "s/s.dependency 'CertifaceSDK', '[^']*'/s.dependency 'CertifaceSDK', '$IOS_VERSION'/" "$PODSPEC_FILE"
        echo "✓ iOS atualizado para versão $IOS_VERSION"
    else
        echo "✗ Arquivo não encontrado: $PODSPEC_FILE"
        exit 1
    fi
fi

echo ""
echo "Versões atualizadas com sucesso!"

