# Changelog

## [1.1.2] - 07/05/2026

### Adicionado

- Correção da tela de resultado do fluxo de iproov
- melhorias no fluxo de customização android/ios

## [1.1.1] - 05/05/2026

### Adicionado

- Packages android atualizados

## [1.1.0] - 08/04/2026

### Adicionado

- Versã CertifaceSDK iOS atualizada para 1.3.0

## [1.0.0] - 24/03/2026

### Adicionado

- Configuração `showInstructionScreen` no tema de instruções para controlar exibição da tela de instruções
- Suporte à configuração em iOS via `setShowInstructionsScreen` no `LivenessManagerOptions`
- Suporte à configuração em Android para FaceTec e iProov via `setShowInstructionScreen` no `setInstructionsTheme`
- Suporte completo a customização de temas para FaceTec
- Suporte completo a customização de temas para iProov
- Sistema de gerenciamento de assets para Android
- Sistema de gerenciamento de assets para iOS
- Processador de assets para facilitar integração de imagens e fontes customizadas
- Configuração de ambientes (HML/PRD) para melhor flexibilidade
- Lançamento inicial do SDK React Native da Certiface
- Integração com FaceTec para verificação de liveness
- Integração com iProov para verificação de liveness
- Função `startJourney` para iniciar processo de verificação biométrica
- Função `checkCameraPermission` para verificar permissões de câmera
- Função `requestCameraPermission` para solicitar permissões de câmera
- Suporte para Android (API 26+) e iOS (12.0+)
- Interface TypeScript com tipagem completa
- Integração com TurboModules para performance otimizada
- Documentação completa em português
- Exemplos de uso e integração
- Suporte a dois provedores de liveness: FaceTec e iProov
- Sistema de callbacks para sucesso e erro
- Compatibilidade com React Native 0.60+


### Alterado

- Estrutura do tipo `InstructionsTheme` agora inclui `configuration` com opções de configuração
- Correção na implementação do resultado de sucesso
- Correção nos tipos de importação TypeScript
- Correção na implementação Android do SDK
- Correção no theme factory do iProov
- Ajustes no provedor FaceTec
- Atualização das versões internas do SDK nativo
- Melhorias na implementação de resultados
- Otimizações no código para melhor performance

### Corrigido

- Correção na implementação do resultado de sucesso
- Correção nos tipos de importação TypeScript
- Correção na implementação Android do SDK
- Correção no theme factory do iProov
- Ajustes no provedor FaceTec

### Atualização dos Módulos

- **iOS**: CertifaceSDK `1.2.0`
- **Android**: oitisdk `1.1.0`
