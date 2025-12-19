# Changelog

## [1.2.0] - 19/12/2025

### Adicionado

- Configuração `showInstructionScreen` no tema de instruções para controlar exibição da tela de instruções
- Suporte à configuração em iOS via `setShowInstructionsScreen` no `LivenessManagerOptions`
- Suporte à configuração em Android para FaceTec e iProov via `setShowInstructionScreen` no `setInstructionsTheme`

### Alterado

- Estrutura do tipo `InstructionsTheme` agora inclui `configuration` com opções de configuração

### Atualização dos Módulos

- **iOS**: CertifaceSDK `1.2.0`
- **Android**: oitisdk `1.1.0`

## [1.1.0] - 19/11/2025

### Adicionado

- Suporte completo a customização de temas para FaceTec
- Suporte completo a customização de temas para iProov
- Sistema de gerenciamento de assets para Android
- Sistema de gerenciamento de assets para iOS
- Processador de assets para facilitar integração de imagens e fontes customizadas
- Configuração de ambientes (HML/PRD) para melhor flexibilidade

### Corrigido

- Correção na implementação do resultado de sucesso
- Correção nos tipos de importação TypeScript
- Correção na implementação Android do SDK
- Correção no theme factory do iProov
- Ajustes no provedor FaceTec

### Alterado

- Atualização das versões internas do SDK nativo
- Melhorias na implementação de resultados
- Otimizações no código para melhor performance

### Atualização dos Módulos

- **iOS**: CertifaceSDK `1.1.0`
- **Android**: oitisdk `1.0.3`

## [1.0.0] - 10/11/2025

### Adicionado

- Lançamento inicial do SDK React Native da Oiti
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
