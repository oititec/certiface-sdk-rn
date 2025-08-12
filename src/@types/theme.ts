export enum LivenessProvider {
  FACETEC = 'FACETEC',
  IPROOV = 'IPROOV',
}

export interface FacetecColors {
  guidanceBackgroundColors?: string;
  guidanceForegroundColor?: string;
  guidanceReadyScreenHeaderTextColor?: string;
  guidanceReadyScreenSubtextTextColor?: string;
  guidanceButtonBackgroundHighlightColor?: string;
  guidanceButtonTextHighlightColor?: string;
  guidanceButtonBorderColor?: string;
  guidanceReadyScreenOvalFillColor?: string;
  guidanceRetryScreenHeaderTextColor?: string;
  guidanceRetryScreenSubtextTextColor?: string;
  guidanceRetryScreenImageBorderColor?: string;
  guidanceRetryScreenOvalStrokeColor?: string;
  resultScreenForegroundColor?: string;
  resultScreenBackgroundColors?: string;
  resultScreenActivityIndicatorColor?: string;
  resultScreenUploadProgressFillColor?: string;
  resultScreenUploadProgressTrackColor?: string;
  resultScreenResultAnimationBackgroundColor?: string;
  resultScreenResultAnimationForegroundColor?: string;
  ovalCustomizationStrokeColor?: string;
  ovalCustomizationProgressColor1?: string;
  ovalCustomizationProgressColor2?: string;
  frameBorderColor?: string;
  frameBackgroundColor?: string;
  overlayBackgroundColor?: string;
  feedbackBackgroundColors?: string;
  feedbackTextColor?: string;
}

export interface FacetecTexts {
  // Ready Screen
  readyHeader1?: string;
  readyHeader2?: string;
  readyMessage1?: string;
  readyMessage2?: string;
  readyButton?: string;

  // Retry Screen
  retryHeader?: string;
  retrySubheader?: string;
  retryMessageSmile?: string;
  retryMessageLighting?: string;
  retryMessageContrast?: string;
  retryYourPicture?: string;
  retryIdealPicture?: string;
  retryButton?: string;

  // Result Screen
  resultUploadMessage?: string;
  resultSuccessMessage?: string;

  // Feedback Messages
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
  instructionsTitleFont?: string;
  instructionsCaptionFont?: string;
  instructionsDocumentTypesInstructionsFont?: string;
  instructionsDocumentTipsInstructionsFont?: string;
  instructionsButtonFont?: string;
  permissionTitleFont?: string;
  permissionCaptionFont?: string;
  permissionButtonFont?: string;
  guidanceCustomizationHeaderFont?: string;
  guidanceCustomizationSubtextFont?: string;
  guidanceCustomizationButtonFont?: string;
  guidanceCustomizationReadyScreenHeaderFont?: string;
  guidanceCustomizationReadyScreenSubtextFont?: string;
  guidanceCustomizationRetryScreenHeaderFont?: string;
  guidanceCustomizationRetryScreenSubtextFont?: string;
  resultScreenCustomizationMessageFont?: string;
  feedbackCustomizationTextFont?: string;
}

export interface FacetecTheme {
  colors?: FacetecColors;
  texts?: FacetecTexts;
  fonts?: FacetecFonts;
}

export interface IProovColors {
  titleColor?: string;
  headerBackgroundColor?: string;
  promptTextColor?: string;
  promptBackgroundColor?: string;
  surroundColor?: string;
  ovalReadyColor?: string;
  ovalNotReadyColor?: string;
  ovalStrokeColor?: string;
  ovalCompletedColor?: string;
}

export interface IProovTexts {
  title?: string;
  instructionsTitleText?: string;
  instructionsCaptionText?: string;
  documentTipsInstructionText?: string;
  documentTypesInstructionText?: string;
  continueButtonText?: string;
  permissionTitle?: string;
  checkPermissionButtonText?: string;
  successText?: string;
  errorText?: string;
  retryButtonText?: string;
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
  fonts?: IProovFonts;
}

export interface InstructionsThemeColors {
  titleColor?: string;
  captionColor?: string;
  backgroundColor?: string;
  statusBarColor?: string;
  bottomSheetColor?: string;
  continueButtonColor?: string;
  continueButtonTextColor?: string;
}

export interface InstructionsThemeTexts {
  titleText?: string;
  captionText?: string;
  documentTipsInstructionText?: string;
  documentTypesInstructionText?: string;
  continueButtonText?: string;
}

export interface InstructionsTheme {
  colors?: InstructionsThemeColors;
  texts?: InstructionsThemeTexts;
}

export interface PermissionThemeColors {
  titleColor?: string;
  backgroundColor?: string;
  statusBarColor?: string;
  checkPermissionButtonColor?: string;
  checkPermissionButtonTextColor?: string;
}

export interface PermissionThemeTexts {
  title?: string;
  checkPermissionButtonText?: string;
}

export interface PermissionTheme {
  colors?: PermissionThemeColors;
  texts?: PermissionThemeTexts;
}

export interface ProcessingThemeColors {
  backgroundColor?: string;
  loadingDialogColor?: string;
  statusBarColor?: string;
}

export interface ProcessingTheme {
  colors?: ProcessingThemeColors;
}

export interface ResultThemeColors {
  successBackgroundColor?: string;
  successTextColor?: string;
  errorBackgroundColor?: string;
  errorTextColor?: string;
  statusBarSuccessColor?: string;
  statusBarErrorColor?: string;
  retryButtonColor?: string;
  retryButtonTextColor?: string;
}

export interface ResultThemeTexts {
  successText?: string;
  errorText?: string;
  retryButtonText?: string;
}

export interface ResultTheme {
  colors?: ResultThemeColors;
  texts?: ResultThemeTexts;
}

export interface OitiTheme {
  provider: LivenessProvider;
  facetec?: FacetecTheme;
  iproov?: IProovTheme;
  instructions?: InstructionsTheme;
  permission?: PermissionTheme;
  processing?: ProcessingTheme;
  result?: ResultTheme;
}
