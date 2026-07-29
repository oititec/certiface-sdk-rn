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

export type FacetecButtonLocation = 'TOP_LEFT' | 'TOP_RIGHT';

export type FacetecExitAnimationStyle =
  | 'CIRCLE_FADE'
  | 'RIPPLE_OUT'
  | 'RIPPLE_IN'
  | 'NONE';

export interface FacetecSizes {
  guidanceButtonBorderWidth?: number;
  guidanceRetryScreenImageBorderWidth?: number;
  frameBorderWidth?: number;
  frameCornerRadius?: number;
  /** @platform android */
  frameElevation?: number;
  feedbackElevation?: number;
  feedbackCornerRadius?: number;
  guidanceButtonCornerRadius?: number;
  /** @platform android */
  guidanceRetryScreenImageCornerRadius?: number;
  /** @platform android */
  readyScreenTextBackgroundCornerRadius?: number;
  ovalStrokeWidth?: number;
  /** Android canônico; iOS também aceita via alias ovalProgressWidth */
  ovalProgressStrokeWidth?: number;
  /** Android canônico; iOS também aceita via alias ovalProgressOffset */
  ovalProgressRadialOffset?: number;
  /** Alias de ovalProgressStrokeWidth (ambos) */
  ovalProgressWidth?: number;
  /** Alias de ovalProgressRadialOffset (ambos) */
  ovalProgressOffset?: number;
  /** @platform android */
  resultScreenAnimationRelativeScale?: number;
  /** @platform android */
  resultScreenCustomActivityIndicatorRotationInterval?: number;
}

export interface FacetecConfiguration {
  cancelButtonLocation?: FacetecButtonLocation;
  /** @platform android */
  exitAnimationStyle?: FacetecExitAnimationStyle;
}

export interface FacetecFlags {
  overlayShowBrandingImage?: boolean;
  /** @platform android */
  feedbackEnablePulsatingText?: boolean;
  /** @platform android */
  resultScreenShowUploadProgressBar?: boolean;
}

export interface FacetecColors {
  readyScreenHeader?: string;
  readyScreenSubtext?: string;
  readyScreenTextBackground?: string;
  /** @platform android */
  readyScreenOvalFill?: string;
  resultScreenMessage?: string;
  resultScreenUploadProgressBarFill?: string;
  resultScreenUploadProgressBarTrack?: string;
  resultScreenForeground?: string;
  /** @platform android */
  resultScreenBackground?: string;
  resultScreenActivityIndicator?: string;
  resultScreenResultAnimationBackground?: string;
  resultScreenResultAnimationForeground?: string;
  retryScreenHeader?: string;
  retryScreenSubtext?: string;
  /** @platform android */
  retryScreenImageBorder?: string;
  /** @platform android */
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
  /** @platform android */
  guidanceForeground?: string;
  /** @platform android */
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
  /** @platform android */
  resultScreenCustomActivityIndicatorAnimation?: string;
  /** @platform android */
  resultScreenSuccessImage?: string;
  /** @platform android */
  resultScreenErrorImage?: string;
  /** @platform android */
  resultScreenSuccessBackgroundImage?: string;
  /** @platform android */
  resultScreenErrorBackgroundImage?: string;
  /** @platform android */
  resultScreenSuccessAnimation?: string;
  /** @platform android */
  resultScreenErrorAnimation?: string;
}

export interface FacetecTheme {
  colors?: FacetecColors;
  texts?: FacetecTexts;
  fonts?: FacetecFonts;
  assets?: FacetecAssets;
  sizes?: FacetecSizes;
  flags?: FacetecFlags;
  configuration?: FacetecConfiguration;
}

/**
 * IProov Theme
 */

export interface IProovColors {
  closeButtonIcon?: string;
  closeButtonColor?: string;
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

export type IProovOrientationGPA =
  | 'PORTRAIT'
  | 'REVERSE_PORTRAIT'
  | 'LANDSCAPE'
  | 'REVERSE_LANDSCAPE';

export type IProovOrientationLA = 'PORTRAIT' | 'REVERSE_PORTRAIT';

export type IProovFilterStyle = 'natural' | 'lineDrawing';

export type IProovNaturalStyle = 'clear' | 'blur';

export type IProovLineDrawingStyle = 'classic' | 'shaded' | 'vibrant';

export interface IProovConfiguration {
  timeoutSecs?: number;
  /** @platform android */
  orientationGpa?: IProovOrientationGPA;
  /** @platform android */
  orientationLa?: IProovOrientationLA;
  filterStyle?: IProovFilterStyle;
  naturalStyle?: IProovNaturalStyle;
  lineDrawingStyle?: IProovLineDrawingStyle;
}

export interface IProovFlags {
  /** @platform android */
  isEnabledScreenShots?: boolean;
  /** @platform android */
  disableExteriorEffects?: boolean;
  promptRoundedCorners?: boolean;
}

export interface IProovTheme {
  colors?: IProovColors;
  texts?: IProovTexts;
  assets?: IProovAssets;
  fonts?: IProovFonts;
  fontResource?: string;
  fontPath?: string;
  configuration?: IProovConfiguration;
  flags?: IProovFlags;
}

/**
 * Instructions Theme
 */

export interface InstructionsThemeColors {
  /** @platform android */
  statusBar?: string;
  background?: string;
  backButtonIcon?: string;
  /** Tint do ícone quando `assets.backButtonIcon` está definido. Android e iOS. */
  backButtonColor?: string;
  bottomSheet?: string;
  title?: string;
  caption?: string;
  firstInstructionTitle?: string;
  secondInstructionTitle?: string;
  firstInstructionIconBackground?: string;
  firstInstructionIconBorder?: string;
  secondInstructionIconBackground?: string;
  secondInstructionIconBorder?: string;
  continueButtonText?: string;
  continueButtonTextColor?: string;
  continueButtonBackground?: string;
  /** @platform ios */
  continueButtonBorder?: string;
}

export interface InstructionsThemeTexts {
  title?: string;
  caption?: string;
  firstInstruction?: string;
  secondInstruction?: string;
  continueButton?: string;
}

export type InstructionsImageScale =
  | 'fit'
  | 'fillBounds'
  | 'crop'
  | 'inside'
  | 'none'
  | 'fillWidth'
  | 'fillHeight';

export interface InstructionsThemeAssets {
  backButtonIcon?: string;
  contextImage?: string;
  /** @platform android */
  contextImageScale?: InstructionsImageScale;
  /** @platform android */
  contextImageHeightFraction?: number;
  firstInstructionIcon?: string;
  secondInstructionIcon?: string;
  instructionIconScale?: InstructionsImageScale;
  /** Tamanho do círculo em dp/pt. Android: layout nativo. iOS: composição no bridge. */
  instructionIconSize?: number;
}

export interface InstructionsThemeFonts {
  title?: string;
  caption?: string;
  firstInstructionTitle?: string;
  secondInstructionTitle?: string;
  continueButton?: string;
}

export interface InstructionsThemeSizes {
  bottomSheetCornerRadius?: number;
  titleFontSize?: number;
  captionFontSize?: number;
  firstInstructionTitleFontSize?: number;
  secondInstructionTitleFontSize?: number;
  continueButtonFontSize?: number;
}

export interface InstructionsConfiguration {
  showInstructionScreen?: boolean;
}

export interface InstructionsFlags {
  statusBarIsDarkIcons?: boolean;
}

export interface InstructionsTheme {
  configuration?: InstructionsConfiguration;
  flags?: InstructionsFlags;
  colors?: InstructionsThemeColors;
  texts?: InstructionsThemeTexts;
  assets?: InstructionsThemeAssets;
  fonts?: InstructionsThemeFonts;
  sizes?: InstructionsThemeSizes;
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
  openSettingsButton?: string;
  opentSettingsButton?: string;
  closeButton?: string;
}

export interface PermissionThemeFlags {
  statusBarIsDarkIcons?: boolean;
}

export interface PermissionThemeSizes {
  titleFontSize?: number;
  captionFontSize?: number;
  checkPermissionButtonFontSize?: number;
  bottomSheetTitleFontSize?: number;
  bottomSheetCaptionFontSize?: number;
  openSettingsButtonFontSize?: number;
  closeButtonFontSize?: number;
}

export interface PermissionTheme {
  colors?: PermissionThemeColors;
  texts?: PermissionThemeTexts;
  assets?: PermissionThemeAssets;
  fonts?: PermissionThemeFonts;
  flags?: PermissionThemeFlags;
  sizes?: PermissionThemeSizes;
}

/**
 * Processing Theme
 */

export interface ProcessingThemeColors {
  statusBar?: string;
  background?: string;
  loading?: string;
}

export interface ProcessingThemeFlags {
  statusBarIsDarkIcons?: boolean;
}

export interface ProcessingThemeSizes {
  loadingIndicatorSize?: number;
  /** @platform android iproov */
  loadingIndicatorWidth?: number;
  /** @platform ios */
  spinnerSize?: number;
  /** @platform ios */
  spinnerWidth?: number;
}

export interface ProcessingTheme {
  colors?: ProcessingThemeColors;
  flags?: ProcessingThemeFlags;
  sizes?: ProcessingThemeSizes;
}

/**
 * Result Theme
 */

export interface ResultThemeColors {
  /** @platform android */
  successStatusBar?: string;
  successBackground?: string;
  successText?: string;
  /** @platform android */
  errorStatusBar?: string;
  errorBackground?: string;
  errorText?: string;
  /** @platform ios Fundo da tela de retry (não usar como cor do botão). */
  retryBackground?: string;
  /** @platform ios Cor da mensagem de retry. */
  retryText?: string;
  retryButtonText?: string;
  retryButtonBackground?: string;
  /** @platform ios */
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
  /** @platform ios */
  retryImage?: string;
}

export interface ResultThemeFonts {
  text?: string;
  retryButton?: string;
}

export interface ResultThemeFlags {
  successStatusBarIsDarkIcons?: boolean;
  errorStatusBarIsDarkIcons?: boolean;
}

export interface ResultThemeSizes {
  textFontSize?: number;
  retryButtonFontSize?: number;
}

export interface ResultTheme {
  colors?: ResultThemeColors;
  texts?: ResultThemeTexts;
  assets?: ResultThemeAssets;
  fonts?: ResultThemeFonts;
  flags?: ResultThemeFlags;
  sizes?: ResultThemeSizes;
}

export interface CertifaceTheme {
  provider?: LivenessProvider;
  facetec?: FacetecTheme;
  iproov?: IProovTheme;
  instructions?: InstructionsTheme;
  permission?: PermissionTheme;
  processing?: ProcessingTheme;
  result?: ResultTheme;
}
