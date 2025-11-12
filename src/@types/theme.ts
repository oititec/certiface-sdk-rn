export enum LivenessProvider {
  FACETEC = 'FACETEC',
  IPROOV = 'IPROOV',
}

export enum Environment {
  HML = 'HML',
  PRD = 'PRD',
}

/**
 * Facetec Theme
 */

export interface FacetecColors {
  readyScreenHeader?: string;
  readyScreenSubtext?: string;
  readyScreenTextBackground?: string;
  readyScreenOvalFill?: string;
  resultScreenMessage?: string;
  resultScreenUploadProgressBarFill?: string;
  resultScreenUploadProgressBarTrack?: string;
  resultScreenForeground?: string;
  resultScreenBackground?: string;
  resultScreenActivityIndicator?: string;
  resultScreenResultAnimationBackground?: string;
  resultScreenResultAnimationForeground?: string;
  retryScreenHeader?: string;
  retryScreenSubtext?: string;
  retryScreenImageBorder?: string;
  retryScreenOvalStroke?: string;
  feedbackMessage?: string;
  feedbackBarBackground?: string;
  guidanceButtonTextNormal?: string;
  guidanceButtonTextHighlight?: string;
  guidanceButtonTextDisabled?: string;
  guidanceButtonBackgroundNormal?: string;
  guidanceButtonBackgroundHighlight?: string;
  guidanceButtonBackgroundDisabled?: string;
  guidanceButtonBorder?: string;
  guidanceForeground?: string;
  guidanceBackground?: string;
  frameBackground?: string;
  frameBorder?: string;
  ovalStroke?: string;
  ovalProgressFirst?: string;
  ovalProgressSecond?: string;
  overlayBackground?: string;
}

export interface FacetecTexts {
  // Ready Screen
  readyHeader1?: string;
  readyHeader2?: string;
  readyMessage1?: string;
  readyMessage2?: string;
  readyButton?: string;

  retryHeader?: string;
  retrySubheader?: string;
  retryMessageSmile?: string;
  retryMessageLighting?: string;
  retryMessageContrast?: string;
  retryYourPicture?: string;
  retryIdealPicture?: string;
  retryButton?: string;

  resultUploadMessage?: string;
  resultSuccessMessage?: string;

  feedbackLookStraightInOval?: string;
  feedbackCenterFace?: string;
  feedbackFaceNotFound?: string;
  feedbackFaceNotLookingStraightAhead?: string;
  feedbackFaceNotUpright?: string;
  feedbackHoldSteady?: string;
  feedbackMovePhoneAway?: string;
  feedbackMovePhoneCloser?: string;
  feedbackMovePhoneToEyeLevel?: string;
  feedbackUseEvenLighting?: string;
  feedbackFrameYourFace?: string;
  feedbackHoldSteady1?: string;
  feedbackHoldSteady2?: string;
  feedbackHoldSteady3?: string;
  feedbackRemoveDarkGlasses?: string;
  feedbackNeutralExpression?: string;
  feedbackConditionsTooBright?: string;
  feedbackBrightenYourEnvironment?: string;
}

export interface FacetecFonts {
  readyScreenHeader?: string;
  readyScreenSubtext?: string;
  resultScreenMessage?: string;
  retryScreenHeader?: string;
  retryScreenSubtext?: string;
  feedbackMessage?: string;
  guidanceHeader?: string;
  guidanceSubtext?: string;
  guidanceButton?: string;
}

export interface FacetecAssets {
  overlayBrandImage?: string;
  cancelButtonIcon?: string;
  resultScreenCustomActivityIndicatorImage?: string;
}

export interface FacetecTheme {
  colors?: FacetecColors;
  texts?: FacetecTexts;
  fonts?: FacetecFonts;
  assets?: FacetecAssets;
}

/**
 * IProov Theme
 */

export interface IProovColors {
  closeButtonIcon?: string;
  title?: string;
  titleBackground?: string;
  promptText?: string;
  promptBackground?: string;
  background?: string;
  ovalReady?: string;
  ovalNotReady?: string;
  ovalCapturing?: string;
  ovalCompleted?: string;
  filterLineDrawingForeground?: string;
  filterLineDrawingBackground?: string;
}

export interface IProovTexts {
  title?: string;
}

export interface IProovAssets {
  closeButtonIcon?: string;
  logoImage?: string;
}

export interface IProovFonts {
  instructionsTitleFont?: string;
  instructionsCaptionFont?: string;
  instructionsDocumentTypesInstructionsFont?: string;
  instructionsDocumentTipsInstructionsFont?: string;
  instructionsButtonFont?: string;
  permissionTitleFont?: string;
  permissionCaptionFont?: string;
  permissionButtonFont?: string;
  resultMessageFont?: string;
  resultRetryButtonFont?: string;
}

export interface IProovTheme {
  colors?: IProovColors;
  texts?: IProovTexts;
  assets?: IProovAssets;
  fonts?: IProovFonts;
}

/**
 * Instructions Theme
 */

export interface InstructionsThemeColors {
  statusBar?: string;
  background?: string;
  backButtonIcon?: string;
  backButtonBackground?: string;
  backButtonBorder?: string;
  bottomSheet?: string;
  title?: string;
  caption?: string;
  firstInstructionTitle?: string;
  secondInstructionTitle?: string;
  continueButtonText?: string;
  continueButtonBackground?: string;
  continueButtonBorder?: string;
}

export interface InstructionsThemeTexts {
  title?: string;
  caption?: string;
  firstInstruction?: string;
  secondInstruction?: string;
  continueButton?: string;
}

export interface InstructionsThemeAssets {
  backButtonIcon?: string;
  contextImage?: string;
  firstInstructionIcon?: string;
  secondInstructionIcon?: string;
}

export interface InstructionsThemeFonts {
  title?: string;
  caption?: string;
  firstInstructionTitle?: string;
  secondInstructionTitle?: string;
  continueButton?: string;
}

export interface InstructionsTheme {
  colors?: InstructionsThemeColors;
  texts?: InstructionsThemeTexts;
  assets?: InstructionsThemeAssets;
  fonts?: InstructionsThemeFonts;
}

/**
 * Permission Theme
 */

export interface PermissionThemeColors {
  statusBar?: string;
  background?: string;
  backButtonIcon?: string;
  backButtonBackground?: string;
  backButtonBorder?: string;
  cameraImage?: string;
  title?: string;
  caption?: string;
  checkPermissionButtonText?: string;
  checkPermissionButtonBackground?: string;
  checkPermissionButtonBorder?: string;
  bottomSheet?: string;
  bottomSheetTitle?: string;
  bottomSheetCaption?: string;
  openSettingsButtonText?: string;
  openSettingsButtonBackground?: string;
  openSettingsButtonBorder?: string;
  closeButtonText?: string;
  closeButtonBackground?: string;
  closeButtonBorder?: string;
}

export interface PermissionThemeTexts {
  title?: string;
  caption?: string;
  checkPermissionButton?: string;
  bottomSheetTitle?: string;
  bottomSheetCaption?: string;
  openSettingsButton?: string;
  closeButton?: string;
}

export interface PermissionThemeAssets {
  backButtonIcon?: string;
  cameraImage?: string;
}

export interface PermissionThemeFonts {
  title?: string;
  caption?: string;
  checkPermissionButton?: string;
  bottomSheetTitle?: string;
  bottomSheetCaption?: string;
  opentSettingsButton?: string;
  closeButton?: string;
}

export interface PermissionTheme {
  colors?: PermissionThemeColors;
  texts?: PermissionThemeTexts;
  assets?: PermissionThemeAssets;
  fonts?: PermissionThemeFonts;
}

/**
 * Processing Theme
 */

export interface ProcessingThemeColors {
  statusBar?: string;
  background?: string;
  loading?: string;
}

export interface ProcessingTheme {
  colors?: ProcessingThemeColors;
}

/**
 * Result Theme
 */

export interface ResultThemeColors {
  successStatusBar?: string;
  successBackground?: string;
  successText?: string;
  errorStatusBar?: string;
  errorBackground?: string;
  errorText?: string;
  retryBackground?: string;
  retryText?: string;
  retryButtonText?: string;
  retryButtonBackground?: string;
  retryButtonBorder?: string;
}

export interface ResultThemeTexts {
  success?: string;
  error?: string;
  retryButton?: string;
}

export interface ResultThemeAssets {
  successImage?: string;
  errorImage?: string;
  retryImage?: string;
}

export interface ResultThemeFonts {
  text?: string;
  retryButton?: string;
}

export interface ResultTheme {
  colors?: ResultThemeColors;
  texts?: ResultThemeTexts;
  assets?: ResultThemeAssets;
  fonts?: ResultThemeFonts;
}

/**
 * Oiti Theme
 */

export interface OitiTheme {
  provider?: LivenessProvider;
  facetec?: FacetecTheme;
  iproov?: IProovTheme;
  instructions?: InstructionsTheme;
  permission?: PermissionTheme;
  processing?: ProcessingTheme;
  result?: ResultTheme;
}
