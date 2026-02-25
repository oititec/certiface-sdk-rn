# Tela Inicial/Instruções

Explicação pré-liveness.

![Instructions Screen](https://files.readme.io/9695f8ffa10b279df33e2c692f29e5b114636275d3d0af9d2022a86d316933f7-instructions_screen.png)

## Propriedades de Cores (`InstructionsThemeColors`)

| Nº  | Propriedade                | Descrição                                      | Tipo     | Exemplo     |
| --- | -------------------------- | ---------------------------------------------- | -------- | ----------- |
| 1   | `statusBar`                | Cor da barra de status do sistema              | `string` | `"#2E2E2E"` |
| 2   | `background`               | Cor de fundo da tela                           | `string` | `"#2E2E2E"` |
| 3   | `backButtonIcon`           | Cor do ícone do botão voltar                   | `string` | `"#FFFFFF"` |
| 4   | `backButtonBackground`     | Cor de fundo do botão voltar                   | `string` | `"#2E2E2E"` |
| 5   | `backButtonBorder`         | Cor da borda do botão voltar                   | `string` | `"#2E2E2E"` |
| 6   | `bottomSheet`              | Cor de fundo do painel inferior (bottom sheet) | `string` | `"#1A1A1A"` |
| 7   | `title`                    | Cor do texto do título principal               | `string` | `"#FFFFFF"` |
| 8   | `caption`                  | Cor do texto da legenda/descrição              | `string` | `"#CCCCCC"` |
| 9   | `firstInstructionTitle`    | Cor do texto da primeira instrução             | `string` | `"#FFFFFF"` |
| 10  | `secondInstructionTitle`   | Cor do texto da segunda instrução              | `string` | `"#FFFFFF"` |
| 11  | `continueButtonText`       | Cor do texto do botão continuar                | `string` | `"#FFFFFF"` |
| 12  | `continueButtonBackground` | Cor de fundo do botão continuar                | `string` | `"#FF6B35"` |
| 13  | `continueButtonBorder`     | Cor da borda do botão continuar                | `string` | `"#FF6B35"` |

## Propriedades de Textos (`InstructionsThemeTexts`)

| Nº  | Propriedade         | Descrição                                   | Tipo     | Exemplo                                          |
| --- | ------------------- | ------------------------------------------- | -------- | ------------------------------------------------ |
| 1   | `title`             | Texto do título principal da tela           | `string` | `"Verificação de Identidade"`                    |
| 2   | `caption`           | Texto da legenda/descrição abaixo do título | `string` | `"Siga as instruções para completar o processo"` |
| 3   | `firstInstruction`  | Texto da primeira instrução                 | `string` | `"Mantenha o documento bem iluminado"`           |
| 4   | `secondInstruction` | Texto da segunda instrução                  | `string` | `"Use um documento oficial com foto"`            |
| 5   | `continueButton`    | Texto do botão para continuar               | `string` | `"Continuar"`                                    |

## Propriedades de Assets (`InstructionsThemeAssets`)

| Nº  | Propriedade             | Descrição                                          | Tipo     | Exemplo           |
| --- | ----------------------- | -------------------------------------------------- | -------- | ----------------- |
| 1   | `backButtonIcon`        | Nome do asset para o ícone do botão voltar         | `string` | `"close_icon"`    |
| 2   | `contextImage`          | Nome do asset para a imagem de contexto/ilustração | `string` | `"neutral_face"`  |
| 3   | `firstInstructionIcon`  | Nome do asset para o ícone da primeira instrução   | `string` | `"backhand_left"` |
| 4   | `secondInstructionIcon` | Nome do asset para o ícone da segunda instrução    | `string` | `"camera_icon"`   |

## Propriedades de Fontes (`InstructionsThemeFonts`)

| Nº  | Propriedade              | Descrição                               | Tipo     | Exemplo   |
| --- | ------------------------ | --------------------------------------- | -------- | --------- |
| 1   | `title`                  | Nome da fonte para o título             | `string` | `"sixty"` |
| 2   | `caption`                | Nome da fonte para a legenda            | `string` | `"sixty"` |
| 3   | `firstInstructionTitle`  | Nome da fonte para a primeira instrução | `string` | `"sixty"` |
| 4   | `secondInstructionTitle` | Nome da fonte para a segunda instrução  | `string` | `"sixty"` |
| 5   | `continueButton`         | Nome da fonte para o botão continuar    | `string` | `"sixty"` |

## Configuração (`InstructionsConfiguration`)

| Nº  | Propriedade             | Descrição                            | Tipo      | Exemplo |
| --- | ----------------------- | ------------------------------------ | --------- | ------- |
| 1   | `showInstructionScreen` | Exibe ou oculta a tela de instruções | `boolean` | `true`  |

---

# Tela de Permissões

Tela para solicitar permissão de câmera ao usuário.

![Permission Screen](https://files.readme.io/eb65258c07f7047db2a5b69a6b173fa7f5272948a682d436cf064763c46d7b86-permission_screen.png)

## Propriedades de Cores (`PermissionThemeColors`)

| Nº  | Propriedade                      | Descrição                                               | Tipo     | Exemplo     |
| --- | -------------------------------- | ------------------------------------------------------- | -------- | ----------- |
| 1   | `statusBar`                      | Cor da barra de status do sistema                       | `string` | `"#2E2E2E"` |
| 2   | `background`                     | Cor de fundo da tela                                    | `string` | `"#2E2E2E"` |
| 3   | `backButtonIcon`                 | Cor do ícone do botão voltar                            | `string` | `"#2E2E2E"` |
| 4   | `backButtonBackground`           | Cor de fundo do botão voltar                            | `string` | `"#2E2E2E"` |
| 5   | `backButtonBorder`               | Cor da borda do botão voltar                            | `string` | `"#2E2E2E"` |
| 6   | `cameraImage`                    | Cor do ícone/imagem da câmera                           | `string` | `"#FFFFFF"` |
| 7   | `title`                          | Cor do texto do título principal                        | `string` | `"#FFFFFF"` |
| 8   | `caption`                        | Cor do texto da legenda/descrição                       | `string` | `"#FFFFFF"` |
| 9   | `checkPermissionButtonText`      | Cor do texto do botão de verificar permissão            | `string` | `"#FFFFFF"` |
| 10  | `checkPermissionButtonBackground`| Cor de fundo do botão de verificar permissão            | `string` | `"#FF6B35"` |
| 11  | `checkPermissionButtonBorder`    | Cor da borda do botão de verificar permissão            | `string` | `"#FF6B35"` |
| 12  | `bottomSheet`                    | Cor de fundo do painel inferior (bottom sheet)          | `string` | `"#FF6B35"` |
| 13  | `bottomSheetTitle`               | Cor do título do painel inferior                        | `string` | `"#FF6B35"` |
| 14  | `bottomSheetCaption`             | Cor da legenda do painel inferior                       | `string` | `"#FF6B35"` |
| 15  | `openSettingsButtonText`         | Cor do texto do botão abrir configurações               | `string` | `"#FF6B35"` |
| 16  | `openSettingsButtonBackground`   | Cor de fundo do botão abrir configurações               | `string` | `"#FF6B35"` |
| 17  | `openSettingsButtonBorder`       | Cor da borda do botão abrir configurações               | `string` | `"#FF6B35"` |
| 18  | `closeButtonText`                | Cor do texto do botão fechar                            | `string` | `"#FF6B35"` |
| 19  | `closeButtonBackground`          | Cor de fundo do botão fechar                            | `string` | `"#FF6B35"` |
| 20  | `closeButtonBorder`              | Cor da borda do botão fechar                            | `string` | `"#FF6B35"` |

## Propriedades de Textos (`PermissionThemeTexts`)

| Nº  | Propriedade             | Descrição                                          | Tipo     | Exemplo               |
| --- | ----------------------- | -------------------------------------------------- | -------- | --------------------- |
| 1   | `title`                 | Texto do título principal da tela                  | `string` | `"Permissões Necessárias"` |
| 2   | `caption`               | Texto da legenda/descrição abaixo do título        | `string` | `"Permissões Necessárias"` |
| 3   | `checkPermissionButton` | Texto do botão para verificar/solicitar permissão  | `string` | `"Permitir Acesso"`   |
| 4   | `bottomSheetTitle`      | Texto do título do painel inferior                 | `string` | `"Permitir Acesso"`   |
| 5   | `bottomSheetCaption`    | Texto da legenda do painel inferior                | `string` | `"Permitir Acesso"`   |
| 6   | `openSettingsButton`    | Texto do botão para abrir configurações do sistema | `string` | `"Permitir Acesso"`   |
| 7   | `closeButton`           | Texto do botão para fechar                         | `string` | `"Permitir Acesso"`   |

## Propriedades de Assets (`PermissionThemeAssets`)

| Nº  | Propriedade      | Descrição                                  | Tipo     | Exemplo        |
| --- | ---------------- | ------------------------------------------ | -------- | -------------- |
| 1   | `backButtonIcon` | Nome do asset para o ícone do botão voltar | `string` | `"close_icon"` |
| 2   | `cameraImage`    | Nome do asset para a imagem da câmera      | `string` | `"camera_icon"`|

## Propriedades de Fontes (`PermissionThemeFonts`)

| Nº  | Propriedade           | Descrição                                      | Tipo     | Exemplo   |
| --- | --------------------- | ---------------------------------------------- | -------- | --------- |
| 1   | `title`               | Nome da fonte para o título                    | `string` | `"sixty"` |
| 2   | `caption`             | Nome da fonte para a legenda                   | `string` | `"sixty"` |
| 3   | `checkPermissionButton`| Nome da fonte para o botão de verificar       | `string` | `"sixty"` |
| 4   | `bottomSheetTitle`    | Nome da fonte para o título do painel inferior | `string` | `"sixty"` |
| 5   | `bottomSheetCaption`  | Nome da fonte para a legenda do painel inferior| `string` | `"sixty"` |
| 6   | `opentSettingsButton` | Nome da fonte para o botão abrir configurações | `string` | `"sixty"` |
| 7   | `closeButton`         | Nome da fonte para o botão fechar              | `string` | `"sixty"` |

---

# Tela de Carregamento (Processing)

Tela exibida durante o processamento do liveness.

![Processing Screen](https://files.readme.io/9ba13986031a2e13232fee6e7c3a7d4cb12fb0e32ab9de87eef63205e59dba0d-processing_screen.png)

## Propriedades de Cores (`ProcessingThemeColors`)

| Nº  | Propriedade | Descrição                         | Tipo     | Exemplo     |
| --- | ----------- | --------------------------------- | -------- | ----------- |
| 1   | `statusBar` | Cor da barra de status do sistema | `string` | `"#1A1A1A"` |
| 2   | `background`| Cor de fundo da tela              | `string` | `"#1A1A1A"` |
| 3   | `loading`   | Cor do indicador de carregamento  | `string` | `"#FFFFFF"` |

---

# Tela do iProov

Tela de captura biométrica utilizando o provider iProov.

![iProov Screen](https://files.readme.io/12cef739d33d1f1d800e26b0df862fc80e48fcd1790912dbb33b671f48c4e4e7-iproov_screen.png)

## Propriedades de Cores (`IProovColors`)

| Nº  | Propriedade                    | Descrição                                          | Tipo     | Exemplo     |
| --- | ------------------------------ | -------------------------------------------------- | -------- | ----------- |
| 1   | `closeButtonIcon`              | Cor do ícone do botão fechar                       | `string` | `"#FFFFFF"` |
| 2   | `title`                        | Cor do texto do título                             | `string` | `"#FFFFFF"` |
| 3   | `titleBackground`              | Cor de fundo do título                             | `string` | `"#2E2E2E"` |
| 4   | `promptText`                   | Cor do texto de instrução/prompt                   | `string` | `"#FFFFFF"` |
| 5   | `promptBackground`             | Cor de fundo do prompt                             | `string` | `"#1A1A1A"` |
| 6   | `background`                   | Cor de fundo da tela                               | `string` | `"#FF6B35"` |
| 7   | `ovalReady`                    | Cor do oval quando pronto para captura             | `string` | `"#FF6B35"` |
| 8   | `ovalNotReady`                 | Cor do oval quando não está pronto                 | `string` | `"#FF3030"` |
| 9   | `ovalCapturing`                | Cor do oval durante a captura                      | `string` | `"#FFFFFF"` |
| 10  | `ovalCompleted`                | Cor do oval quando a captura é concluída           | `string` | `"#FF6B35"` |
| 11  | `filterLineDrawingForeground`  | Cor do primeiro plano do filtro de desenho de linha| `string` | `"#FFFFFF"` |
| 12  | `filterLineDrawingBackground`  | Cor de fundo do filtro de desenho de linha         | `string` | `"#000000"` |

## Propriedades de Textos (`IProovTexts`)

| Nº  | Propriedade | Descrição                         | Tipo     | Exemplo                   |
| --- | ----------- | --------------------------------- | -------- | ------------------------- |
| 1   | `title`     | Texto do título da tela de captura| `string` | `"Verificação Biométrica"`|

## Propriedades de Assets (`IProovAssets`)

| Nº  | Propriedade      | Descrição                                   | Tipo     | Exemplo        |
| --- | ---------------- | ------------------------------------------- | -------- | -------------- |
| 1   | `closeButtonIcon`| Nome do asset para o ícone do botão fechar  | `string` | `"close_icon"` |
| 2   | `logoImage`      | Nome do asset para a imagem do logo         | `string` | `"logo"`       |

## Propriedades de Fontes (`IProovFonts`)

| Nº  | Propriedade                             | Descrição                                                   | Tipo     | Exemplo   |
| --- | --------------------------------------- | ----------------------------------------------------------- | -------- | --------- |
| 1   | `instructionsTitleFont`                 | Nome da fonte para o título das instruções                  | `string` | `"sixty"` |
| 2   | `instructionsCaptionFont`               | Nome da fonte para a legenda das instruções                 | `string` | `"sixty"` |
| 3   | `instructionsDocumentTypesInstructionsFont` | Nome da fonte para instruções de tipos de documento     | `string` | `"sixty"` |
| 4   | `instructionsDocumentTipsInstructionsFont`  | Nome da fonte para dicas de documento                   | `string` | `"sixty"` |
| 5   | `instructionsButtonFont`                | Nome da fonte para o botão das instruções                   | `string` | `"sixty"` |
| 6   | `permissionTitleFont`                   | Nome da fonte para o título de permissão                    | `string` | `"sixty"` |
| 7   | `permissionCaptionFont`                 | Nome da fonte para a legenda de permissão                   | `string` | `"sixty"` |
| 8   | `permissionButtonFont`                  | Nome da fonte para o botão de permissão                     | `string` | `"sixty"` |
| 9   | `resultMessageFont`                     | Nome da fonte para a mensagem de resultado                  | `string` | `"sixty"` |
| 10  | `resultRetryButtonFont`                 | Nome da fonte para o botão de retry do resultado            | `string` | `"sixty"` |

---

# Tela de Resultado

Tela exibida após a conclusão do processo de liveness, mostrando sucesso ou erro.

![Result Screen](https://files.readme.io/bd2fe336fe8d85b21344347c476bc0498b3b447da91a72d8af28382052da261f-Frame_39.png)

## Propriedades de Cores (`ResultThemeColors`)

| Nº  | Propriedade             | Descrição                                           | Tipo     | Exemplo     |
| --- | ----------------------- | --------------------------------------------------- | -------- | ----------- |
| 1   | `successStatusBar`      | Cor da barra de status na tela de sucesso           | `string` | `"#E8F5E8"` |
| 2   | `successBackground`     | Cor de fundo da tela de sucesso                     | `string` | `"#E8F5E8"` |
| 3   | `successText`           | Cor do texto na tela de sucesso                     | `string` | `"#2E7D32"` |
| 4   | `errorStatusBar`        | Cor da barra de status na tela de erro              | `string` | `"#FFEBEE"` |
| 5   | `errorBackground`       | Cor de fundo da tela de erro                        | `string` | `"#FFEBEE"` |
| 6   | `errorText`             | Cor do texto na tela de erro                        | `string` | `"#C62828"` |
| 7   | `retryBackground`       | Cor de fundo do container de retry                  | `string` | `"#C62828"` |
| 8   | `retryText`             | Cor do texto de retry                               | `string` | `"#FFEBEE"` |
| 9   | `retryButtonText`       | Cor do texto do botão de tentar novamente           | `string` | `"#FF6B35"` |
| 10  | `retryButtonBackground` | Cor de fundo do botão de tentar novamente           | `string` | `"#FFFFFF"` |
| 11  | `retryButtonBorder`     | Cor da borda do botão de tentar novamente           | `string` | `"#FFFFFF"` |

## Propriedades de Textos (`ResultThemeTexts`)

| Nº  | Propriedade   | Descrição                                        | Tipo     | Exemplo                                          |
| --- | ------------- | ------------------------------------------------ | -------- | ------------------------------------------------ |
| 1   | `success`     | Texto exibido na tela de sucesso                 | `string` | `"Verificação concluída com sucesso!"`           |
| 2   | `error`       | Texto exibido na tela de erro                    | `string` | `"Houve um erro na verificação. Tente novamente."` |
| 3   | `retryButton` | Texto do botão para tentar novamente             | `string` | `"Tentar Novamente"`                             |

## Propriedades de Assets (`ResultThemeAssets`)

| Nº  | Propriedade   | Descrição                                    | Tipo     | Exemplo   |
| --- | ------------- | -------------------------------------------- | -------- | --------- |
| 1   | `successImage`| Nome do asset para a imagem de sucesso       | `string` | `"shell"` |
| 2   | `errorImage`  | Nome do asset para a imagem de erro          | `string` | `"shell"` |
| 3   | `retryImage`  | Nome do asset para a imagem de retry         | `string` | `"shell"` |

## Propriedades de Fontes (`ResultThemeFonts`)

| Nº  | Propriedade   | Descrição                                    | Tipo     | Exemplo   |
| --- | ------------- | -------------------------------------------- | -------- | --------- |
| 1   | `text`        | Nome da fonte para o texto de resultado      | `string` | `"sixty"` |
| 2   | `retryButton` | Nome da fonte para o botão de retry          | `string` | `"sixty"` |
