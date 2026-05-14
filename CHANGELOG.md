# Changelog

## [1.1.2] - 07/05/2026

### Adicionado

- Nova propriedade `iproov.fontResource` para definir fonte base do iProov por nome de recurso (Android e iOS)
- Nova propriedade `iproov.fontPath` para fallback de fonte base por caminho do arquivo (Android e iOS)
- Nova propriedade `iproov.colors.closeButtonColor` para personalizar cor do botão de fechar (Android e iOS)
- Nova propriedade `instructions.colors.continueButtonTextColor` para personalizar cor do texto do botão de continuar (Android e iOS)
- Nova propriedade `instructions.flags.statusBarIsDarkIcons` para controlar ícones escuros da status bar na tela de instruções (Android)
- Novo grupo `facetec.sizes` para customizações de dimensões no FaceTec (Android)
- Novo grupo `facetec.flags` para customizações booleanas do FaceTec (Android)

### Alterado

- Aplicação de fonte base no iProov Android com `setFontResource(...)` e fallback automático para `setFontPath(...)`
- Resolução de fonte base no iProov iOS priorizando `fontResource` e fallback por `fontPath`
- `instructions.configuration.showInstructionScreen` validado para Android e iOS
- Compatibilidade no Android para `instructions.texts.continueButton` com fallback para `continueButtonText`
- Fallback de `instructions.colors.continueButtonText` para `instructions.colors.continueButtonTextColor`
- Compatibilidade no Android e iOS para aliases de chaves do iProov (`title/titleColor`, `titleBackground/headerBackgroundColor`, `promptText/promptTextColor`, `promptBackground/promptBackgroundColor`, `oval*` e `oval*Color`)
- Compatibilidade no Android para `facetec.colors.resultScreenUploadProgressBarFill/Track` com fallback para `resultScreenUploadProgressFill/Track`
- Compatibilidade no iOS para `permission.fonts.openSettingsButton` com fallback para `opentSettingsButton`
- Exemplo atualizado para demonstrar `iproov.fontResource`, `iproov.fontPath`, `instructions.configuration.showInstructionScreen` e `instructions.flags.statusBarIsDarkIcons`

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
