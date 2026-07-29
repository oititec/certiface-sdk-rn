import type { CertifaceTheme } from '@certiface/sdk';

export const customTheme: CertifaceTheme = {
  // O provider efetivo vem do parâmetro `provider` em `CertifaceSDK.startJourney`.
  facetec: {
    // Cores da jornada FaceTec. Algumas chaves são específicas de Android/iOS; consulte a documentação para paridade visual.
    colors: {
      // Ready Screen: controla a tela inicial nativa do FaceTec antes da captura.
      readyScreenHeader: '#FFD166',
      readyScreenSubtext: '#9CA3AF',
      readyScreenOvalFill: '#FF6B35',
      readyScreenTextBackground: '#444444',
      // Result Screen: use `resultScreenMessage` para cor da mensagem final com melhor paridade entre plataformas.
      resultScreenMessage: '#EEF6F8',
      resultScreenForeground: '#EEF6F8',
      resultScreenBackground: '#1A1A1A',
      resultScreenActivityIndicator: '#EEF6F8',
      resultScreenUploadProgressBarFill: '#EEF6F8',
      resultScreenUploadProgressBarTrack: '#444444',
      resultScreenResultAnimationBackground: '#2E2E2E',
      resultScreenResultAnimationForeground: '#EEF6F8',
      // Oval/frame: muda a borda e progresso do enquadramento facial.
      ovalStroke: '#FF6B35',
      ovalProgressFirst: '#FF6B35',
      ovalProgressSecond: '#FFD700',
      overlayBackground: '#80000000',
      frameBorder: '#FF6B35',
      frameBackground: '#1A1A1A',
      // Feedback/guidance: muda barra de instruções exibida durante a captura.
      feedbackBarBackground: '#FFF8DC',
      feedbackMessage: '#333333',
      guidanceBackground: '#2E2E2E',
      guidanceForeground: '#FACC15',
      // Retry Screen: textos e imagem quando o FaceTec pede nova tentativa.
      retryScreenHeader: '#FF5252',
      retryScreenSubtext: '#DD3333',
      retryScreenOvalStroke: '#FFFFFF',
      retryScreenImageBorder: '#FFFFFF',
      // Botões de guidance: defina explicitamente para evitar diferença visual entre Android e iOS.
      guidanceButtonTextNormal: '#111827',
      guidanceButtonTextHighlight: '#111827',
      guidanceButtonTextDisabled: '#AAAAAA',
      guidanceButtonBackgroundNormal: '#FF6B35',
      guidanceButtonBackgroundHighlight: '#FF6B35',
      guidanceButtonBackgroundDisabled: '#666666',
      guidanceButtonBorder: '#FF6B35',
    },
    // Textos exibidos pelas telas nativas do FaceTec.
    texts: {
      // Ready Screen: textos antes de iniciar a captura.
      readyHeader1: 'Prepare-se',
      readyHeader2: 'para verificação',
      readyMessage1: 'Posicione seu rosto',
      readyMessage2: 'dentro do círculo',
      readyButton: 'Iniciar',
      // Retry Screen: mensagens quando a captura precisa ser repetida.
      retryHeader: 'Vamos tentar novamente',
      retrySubheader: 'Ajustes necessários',
      retryMessageSmile: 'Mantenha uma expressão neutra',
      retryMessageLighting: 'Procure um ambiente bem iluminado',
      retryMessageContrast: 'Evite fundos muito claros ou muito escuros',
      retryYourPicture: 'Sua foto',
      retryIdealPicture: 'Foto ideal',
      retryButton: 'Tentar Novamente',
      // Result Screen: mensagem durante envio e sucesso final.
      resultUploadMessage: 'Enviando validação',
      resultSuccessMessage: 'Verificação concluída!',
      // Feedback: mensagens dinâmicas exibidas durante o posicionamento do rosto.
      feedbackLookStraightInOval: 'Olhe diretamente para a câmera',
      feedbackCenterFace: 'Centralize seu rosto',
      feedbackFaceNotFound: 'Rosto não encontrado',
      feedbackFaceNotLookingStraightAhead: 'Olhe para frente',
      feedbackFaceNotUpright: 'Mantenha o rosto alinhado',
      feedbackHoldSteady: 'Mantenha-se parado',
      feedbackMovePhoneAway: 'Afaste o dispositivo',
      feedbackMovePhoneCloser: 'Aproxime o dispositivo',
      feedbackMovePhoneToEyeLevel: 'Mova o dispositivo para a altura dos olhos',
      feedbackUseEvenLighting: 'Use iluminação uniforme',
      feedbackFrameYourFace: 'Enquadre seu rosto',
      feedbackHoldSteady1: 'Mantenha-se parado',
      feedbackHoldSteady2: 'Quase lá',
      feedbackHoldSteady3: 'Continue parado',
      feedbackRemoveDarkGlasses: 'Remova óculos escuros',
      feedbackNeutralExpression: 'Mantenha expressão neutra',
      feedbackConditionsTooBright: 'Ambiente muito claro',
      feedbackBrightenYourEnvironment: 'Melhore a iluminação do ambiente',
    },
    // Assets Android devem estar em res/drawable ou res/anim; no iOS, em Images.xcassets quando aplicável.
    assets: {
      // Branding no overlay. Só aparece quando `overlayShowBrandingImage` estiver true.
      overlayBrandImage: 'people',
      // Ícone do botão cancelar/fechar da tela FaceTec.
      cancelButtonIcon: 'cancel_button',
      // Assets de resultado abaixo são Android-only no SDK atual.
      resultScreenCustomActivityIndicatorImage:
        'animated_activity_indicator_offwhite',
      resultScreenCustomActivityIndicatorAnimation:
        'animated_activity_indicator_offwhite',
      resultScreenSuccessImage: 'success_icon',
      resultScreenErrorImage: 'error_icon',
      resultScreenSuccessBackgroundImage: 'people',
      resultScreenErrorBackgroundImage: 'env',
      resultScreenSuccessAnimation: 'animated_success_offwhite',
      resultScreenErrorAnimation: 'animated_unsuccess_offwhite',
    },
    fonts: {
      readyScreenHeader: 'sixty',
      readyScreenSubtext: 'sixty',
      resultScreenMessage: 'sixty',
      retryScreenHeader: 'sixty',
      retryScreenSubtext: 'sixty',
      feedbackMessage: 'sixty',
      guidanceHeader: 'sixty',
      guidanceSubtext: 'sixty',
      guidanceButton: 'sixty',
    },
    sizes: {
      // Guidance button: largura da borda e raio do botão nativo.
      guidanceButtonBorderWidth: 2,
      guidanceRetryScreenImageBorderWidth: 3,
      // Frame/feedback: iOS clampa frameBorderWidth em mínimo 4.
      frameBorderWidth: 4,
      frameCornerRadius: 8,
      frameElevation: 5,
      feedbackElevation: 8,
      feedbackCornerRadius: 12,
      guidanceButtonCornerRadius: 12,
      guidanceRetryScreenImageCornerRadius: 12,
      readyScreenTextBackgroundCornerRadius: 12,
      // Oval/progresso: espessura e offset do indicador de progresso da captura.
      ovalStrokeWidth: 4,
      ovalProgressStrokeWidth: 6,
      ovalProgressRadialOffset: 0,
      ovalProgressWidth: 6,
      ovalProgressOffset: 0,
      // Result Screen: escala da animação e velocidade do loading customizado.
      resultScreenAnimationRelativeScale: 1,
      resultScreenCustomActivityIndicatorRotationInterval: 1000,
    },
    flags: {
      // false remove imagem de marca no overlay; true exige `overlayBrandImage`.
      overlayShowBrandingImage: false,
      // Android-only: deixa o texto de feedback com efeito pulsante.
      feedbackEnablePulsatingText: true,
      // Android-only: mostra barra de upload no resultado.
      resultScreenShowUploadProgressBar: true,
    },
    configuration: {
      // Posição do botão cancelar dentro da tela nativa FaceTec.
      cancelButtonLocation: 'TOP_LEFT',
      // Android-only: animação ao sair da jornada FaceTec.
      exitAnimationStyle: 'CIRCLE_FADE',
    },
  },
  iproov: {
    // Fonte base do iProov. `fontResource` busca recurso nativo; `fontPath` é fallback em assets/fonts.
    fontResource: 'sixty',
    fontPath: 'fonts/sixty.ttf',
    colors: {
      closeButtonColor: '#FFFFFF',
      // Alias legado de closeButtonColor. Prefira closeButtonColor quando houver ícone custom.
      // closeButtonIcon: '#FFFFFF',
      title: '#F59E0B',
      titleBackground: '#2E2E2E',
      promptText: '#60A5FA',
      promptBackground: '#1A1A1A',
      background: '#FF6B35',
      ovalReady: '#FF6B35',
      ovalNotReady: '#FF3030',
      ovalCapturing: '#22D3EE',
      ovalCompleted: '#FF6B35',
      // Só aplica com filterStyle: 'lineDrawing'. Com Natural CLEAR deixe omitido/comentado.
      // filterLineDrawingForeground: '#FFFFFF',
      // filterLineDrawingBackground: '#000000',
    },
    texts: {
      title: 'Verificação Biométrica',
    },
    assets: {
      // Glifo monocromático distinto do default (seta). Evite cancel_button (círculo + tint = bola).
      closeButtonIcon: 'fc_arrow_left',
      logoImage: 'people',
    },
    fonts: {
      instructionsTitleFont: 'sixty',
      instructionsCaptionFont: 'sixty',
      instructionsDocumentTypesInstructionsFont: 'sixty',
      instructionsDocumentTipsInstructionsFont: 'sixty',
      instructionsButtonFont: 'sixty',
      permissionTitleFont: 'sixty',
      permissionCaptionFont: 'sixty',
      permissionButtonFont: 'sixty',
      resultMessageFont: 'sixty',
      resultRetryButtonFont: 'sixty',
    },
    configuration: {
      timeoutSecs: 60,
      // Android-only: orientação GPA/LA.
      orientationGpa: 'PORTRAIT',
      orientationLa: 'PORTRAIT',
      filterStyle: 'natural',
      naturalStyle: 'clear',
      // Só usa com filterStyle: 'lineDrawing'. Valores: 'classic' | 'shaded' | 'vibrant'.
      // lineDrawingStyle: 'classic',
    },
    flags: {
      // Android-only: permite screenshots durante a jornada.
      isEnabledScreenShots: true,
      // Android-only: remove efeitos visuais externos do iProov.
      disableExteriorEffects: false,
      // Arredonda os cantos do prompt de instruções (Android e iOS).
      promptRoundedCorners: true,
    },
  },
  // Tela de instruções compartilhada por FaceTec e iProov antes da captura.
  instructions: {
    configuration: {
      // false pula a tela de instruções e inicia direto a captura (Android e iOS).
      showInstructionScreen: true,
    },
    flags: {
      // Android-only: true usa ícones escuros na status bar.
      statusBarIsDarkIcons: false,
    },
    colors: {
      // statusBar é Android-only; background/bottomSheet valem para a tela de instruções.
      statusBar: '#9bd4ce',
      background: '#9bd4ce',
      // `backButtonColor` aplica tint no ícone customizado de voltar.
      backButtonIcon: '#00A89C',
      backButtonColor: '#00A89C',
      bottomSheet: '#FFFFFF',
      title: '#00A89C',
      caption: '#6B7280',
      firstInstructionTitle: '#374151',
      secondInstructionTitle: '#374151',
      firstInstructionIconBackground: '#00A89C',
      firstInstructionIconBorder: '#00A89C',
      secondInstructionIconBackground: '#00A89C',
      secondInstructionIconBorder: '#00A89C',
      continueButtonText: '#FFFFFF',
      continueButtonTextColor: '#FFFFFF',
      continueButtonBackground: '#00A89C',
      continueButtonBorder: '#00A89C',
    },
    texts: {
      title: 'Verificação de Identidade',
      caption: 'Siga as instruções para completar o processo',
      firstInstruction: 'Escolha um ambiente bem iluminado',
      secondInstruction: 'Não use bonés, máscaras ou óculos escuros',
      continueButton: 'Continuar',
    },
    assets: {
      // Use apenas o nome base do asset, sem extensão. Prefira glifo monocromático ~24dp.
      backButtonIcon: 'fc_arrow_left',
      contextImage: 'woman_liveness_example',
      // fillBounds preenche a área superior; fit preserva proporção sem cortar.
      contextImageScale: 'fillBounds', // 'fillBounds' | 'fillWidth' | 'fillHeight' | 'fit' | 'crop' | 'inside' | 'none'
      // 1 ocupa toda a área disponível acima do bottom sheet no Android.
      contextImageHeightFraction: 1,
      firstInstructionIcon: 'lamp_example',
      secondInstructionIcon: 'face',
      // fillBounds faz o ícone preencher o círculo; fit mantém padding interno.
      instructionIconScale: 'fillBounds',
      instructionIconSize: 60,
    },
    fonts: {
      title: 'sixty',
      caption: 'sixty',
      firstInstructionTitle: 'sixty',
      secondInstructionTitle: 'sixty',
      continueButton: 'sixty',
    },
    sizes: {
      // Ajusta altura visual do bottom sheet e hierarquia dos textos.
      bottomSheetCornerRadius: 16,
      titleFontSize: 20,
      captionFontSize: 16,
      firstInstructionTitleFontSize: 14,
      secondInstructionTitleFontSize: 14,
      continueButtonFontSize: 16,
    },
  },
  // Tela de permissão. Bottom sheet, abrir configurações e fechar são customizações aplicadas no iOS.
  permission: {
    flags: {
      // Android-only: true usa ícones escuros na status bar da tela de permissão.
      statusBarIsDarkIcons: false,
    },
    colors: {
      // No Android, a tela é simplificada; bottomSheet/openSettings/closeButton são iOS.
      statusBar: '#9bd4ce',
      background: '#9bd4ce',
      backButtonIcon: '#374151',
      backButtonBackground: '#FFFFFF',
      backButtonBorder: '#00A89C',
      cameraImage: '#00A89C',
      title: '#00A89C',
      caption: '#6B7280',
      checkPermissionButtonText: '#FFFFFF',
      checkPermissionButtonBackground: '#00A89C',
      checkPermissionButtonBorder: '#00A89C',
      bottomSheet: '#FFFFFF',
      bottomSheetTitle: '#00A89C',
      bottomSheetCaption: '#6B7280',
      openSettingsButtonText: '#FFFFFF',
      openSettingsButtonBackground: '#00A89C',
      openSettingsButtonBorder: '#00A89C',
      closeButtonText: '#00A89C',
      closeButtonBackground: '#FFFFFF',
      closeButtonBorder: '#00A89C',
    },
    texts: {
      title: 'Permissões Necessárias',
      caption: 'Precisamos acessar sua câmera para a verificação biométrica',
      checkPermissionButton: 'Permitir Acesso',
      bottomSheetTitle: 'Acesso à câmera negado',
      bottomSheetCaption:
        'Abra as configurações do dispositivo e permita o uso da câmera',
      openSettingsButton: 'Abrir Configurações',
      closeButton: 'Fechar',
    },
    assets: {
      // Ícones usados na tela de permissão.
      backButtonIcon: 'close_icon',
      cameraImage: 'camera_permission',
    },
    fonts: {
      title: 'sixty',
      caption: 'sixty',
      checkPermissionButton: 'sixty',
      bottomSheetTitle: 'sixty',
      bottomSheetCaption: 'sixty',
      openSettingsButton: 'sixty',
      opentSettingsButton: 'sixty',
      closeButton: 'sixty',
    },
    sizes: {
      // Tamanhos de fonte da permissão; campos de bottom sheet são aplicados no iOS.
      titleFontSize: 20,
      captionFontSize: 16,
      checkPermissionButtonFontSize: 16,
      bottomSheetTitleFontSize: 20,
      bottomSheetCaptionFontSize: 16,
      openSettingsButtonFontSize: 16,
      closeButtonFontSize: 16,
    },
  },
  // Tela de processamento entre captura e resultado.
  processing: {
    flags: {
      // Android-only: controla contraste da status bar enquanto processa.
      statusBarIsDarkIcons: true,
    },
    colors: {
      // Tela exibida entre a captura e a resposta final.
      statusBar: '#1A1A1A',
      background: '#1A1A1A',
      loading: '#FFFFFF',
    },
    sizes: {
      // Android: loadingIndicatorSize 1-300 (px-ish). iOS converte para scale 1-10 (100 → ~5).
      // Para forçar scale iOS explícito: spinnerSize (1-10) e spinnerWidth.
      loadingIndicatorSize: 100,
      loadingIndicatorWidth: 10,
    },
  },
  // Tela de sucesso/erro. No iOS: `retryBackground`/`retryText` pintam a tela de retry; no botão use retryButton*.
  result: {
    colors: {
      // successStatusBar/errorStatusBar são Android-only.
      successStatusBar: '#E8F5E8',
      successBackground: '#E8F5E8',
      successText: '#2E7D32',
      errorStatusBar: '#1f7365',
      errorBackground: '#1f7365',
      errorText: '#423c3c',
      // iOS: fundo da tela de retry (não usa como cor do botão no Android).
      retryBackground: '#dbabab',
      // iOS: cor da mensagem de retry.
      retryText: '#1f7365',
      // Use retryButton* para o botão com paridade Android/iOS.
      retryButtonText: '#FF6B35',
      retryButtonBackground: '#FFFFFF',
      retryButtonBorder: '#FFFFFF',
    },
    texts: {
      success: 'Verificação concluída com sucesso!',
      error: 'Houve um erro na verificação. Tente novamente.',
      retryButton: 'Tentar Novamente',
    },
    assets: {
      // retryImage é iOS; Android usa successImage/errorImage.
      // error_icon no example é X vermelho sem círculo branco.
      successImage: 'success_icon',
      errorImage: 'error_icon',
      retryImage: 'return_button',
    },
    fonts: {
      text: 'sixty',
      retryButton: 'sixty',
    },
    flags: {
      // Android-only: contraste dos ícones da status bar em sucesso/erro.
      successStatusBarIsDarkIcons: true,
      errorStatusBarIsDarkIcons: true,
    },
    sizes: {
      textFontSize: 20,
      retryButtonFontSize: 16,
    },
  },
};
