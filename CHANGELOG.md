# Changelog

## [2.0.0] - 01/08/2026

### Adicionado

- Android SaaS: `startSaasJourney` com providers FaceTec e Fortface (`journeyToken`)
- Theme Fortface (Android): cores, textos, assets, fontes de câmera, processing/result compartilhados
- `processing.texts.message` canônico (com fallback legado por provider)

### Corrigido

- Bridge Android: `setProcessingMessage` no iProov; drawables Fortface com resolução app+SDK
- Fontes parciais FaceTec/iProov não forçam mais `ubuntu_regular` nas keys omitidas
- `startJourney(FACETEC)` legado rejeita a Promise com mensagem clara (usar SaaS)

### Alterado

- Dependência Android `certifacesdk` `2.0.0` (repositório production)
- Dependência iOS `CertifaceSDK` `2.0.0` via CocoaPods/artifactory (sem branches git)
- Types: Fortface / SaaS disponíveis; typo `opentSettingsButton` deprecated
- Example `customTheme` como referência completa de customização
- Example iOS: pods git (`feature/30418-fortface` / `ios-certiface-fortface`) comentados; pin em `CertifaceSDK` `2.0.0`

## [1.1.5] - 29/07/2026

### Adicionado

- iProov: `configuration.filterStyle` (`natural` | `lineDrawing`), `naturalStyle` (`clear` | `blur`) e `lineDrawingStyle` (`classic` | `shaded` | `vibrant`)

### Corrigido

- iOS iProov: `setFilterStyle` aplicado (paridade com Android Natural/LineDrawing)
- iOS iProov: `timeoutSecs` e `promptRoundedCorners` bridged
- iOS iProov: close button aplica ícone + tint juntos (`.alwaysTemplate`)
- iOS: tint do back button nas instruções (composer com template)
- iOS: escala do spinner — `loadingIndicatorSize` (1–300) convertido para scale 1–10 (não mais usado como scale bruto)
- Android iProov: botão retry sem fallback enganoso de `retryBackground` / `retryText`
- Android FaceTec: prioridade `resultScreenMessage` antes de `resultScreenForeground`
- Android FaceTec: aliases `ovalProgressWidth` / `ovalProgressOffset`

### Alterado

- Types: `@platform` alinhados (orientation, screenshots, exterior effects, props FaceTec Android-only, retry iOS-only)
- Example `customTheme`: Natural CLEAR por padrão; comentários de plataforma e assets de close/error ajustados

## [1.1.4] - 27/07/2026

### Alterado

- Dependência Android `certifacesdk` atualizada para `1.2.2`
- Dependência iOS `CertifaceSDK` atualizada para `1.8.0` (`OILiveness3D_FT` `9.7.130`)

### Corrigido

- iOS: validação de theme FaceTec não exige mais `resultScreenCustomActivityIndicatorImage` (asset exclusivo Android)

## [1.1.3] - 24/06/2026

### Adicionado

- `instructions.colors.backButtonColor` para tint do ícone de voltar (Android e iOS)
- `instructions.assets.instructionIconScale` e `instructionIconSize` para ícones circulares de instrução (iOS)
- Cores de fundo e borda dos ícones de instrução no Android (`firstInstructionIcon*`, `secondInstructionIcon*`)
- Assets padrão incluídos no pacote iOS do SDK para facilitar customização inicial
- Customização de ícones da tela de resultado via `result.assets.successImage` e `result.assets.errorImage` (Android e iOS); `result.assets.retryImage` (iOS)

### Corrigido

- iOS: aliases de cores e textos na tela de instruções alinhados ao Android
- iOS: ícone de voltar nas instruções sem distorção
- iOS: ícones de instrução com escala, tamanho e cores de fundo/borda
- iOS: customização FaceTec (botão cancelar, tela de retry, frame/oval de captura e mensagem de resultado)
- Android FaceTec: `facetec.assets.cancelButtonIcon` e `facetec.texts.resultSuccessMessage`
- iOS: `facetec.colors.resultScreenForeground` via alias `resultScreenMessage`
- Android e iOS: `iproov.assets.closeButtonIcon` com `iproov.colors.closeButtonColor` na captura iProov
- iOS: fundos das telas de erro e retry no iProov (`result.colors.errorBackground`, `result.colors.retryBackground`)

### Alterado

- Dependência Android `certifacesdk` atualizada para `1.2.0`
- Dependência iOS `CertifaceSDK` atualizada para `1.7.0`
- Documentação de `result.colors.retryBackground` (semântica distinta por plataforma); prefira `retryButtonBackground` para o botão de tentar novamente

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

- FaceTec Android: resolução de fontes via `res/font` antes de `assets/fonts`, evitando `Parâmetros de customização inválidos` quando a fonte está só em `res/font`
- Leitura de `permission.fonts` no iProov Android com fallback para `iproov.fonts.permission*`
- Prioridade de fontes individuais no iProov Android quando o asset existe em `fonts/`
- Suporte a `facetec.sizes` no iOS (FaceTec)
- Documentação de tema atualizada com coluna de plataforma (Android / iOS)
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
