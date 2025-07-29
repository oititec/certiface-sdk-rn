package br.com.oititec.rn.sdk.factories

//import br.com.oiti.designsystem.R
//import br.com.oiti.domain.model.facetec.FacetecButtonLocation
//import br.com.oiti.domain.model.facetec.FacetecExitAnimationStyle
//import br.com.oiti.domain.model.facetec.FacetecTheme
//import br.com.oiti.manager.exports.FacetecFontsKey
//import br.com.oiti.manager.exports.FacetecTextKey
//
//object FacetecThemeFactory {
//
//  fun buildDefault(): FacetecTheme = FacetecTheme.build {}
//
//  fun buildCustom(): FacetecTheme = FacetecTheme.build {
//    val facetecFonts = hashMapOf(
//      FacetecFontsKey.INSTRUCTIONS_TITLE_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.INSTRUCTIONS_CAPTION_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.INSTRUCTIONS_BUTTON_FONT to R.font.ubuntu_regular,
//
//      FacetecFontsKey.PERMISSION_TITLE_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.PERMISSION_CAPTION_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.PERMISSION_BUTTON_FONT to R.font.ubuntu_regular,
//
//      FacetecFontsKey.GUIDANCE_CUSTOMIZATION_HEADER_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.GUIDANCE_CUSTOMIZATION_SUBTEXT_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.GUIDANCE_CUSTOMIZATION_BUTTON_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_HEADER_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_SUBTEXT_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_HEADER_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_SUBTEXT_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.RESULT_SCREEN_CUSTOMIZATION_MESSAGE_FONT to R.font.ubuntu_regular,
//      FacetecFontsKey.FEEDBACK_CUSTOMIZATION_TEXT_FONT to R.font.ubuntu_regular
//    )
//
//    val facetecTexts = hashMapOf(
//      FacetecTextKey.READY_HEADER_1 to "READY_HEADER_1",
//      FacetecTextKey.READY_HEADER_2 to "READY_HEADER_2",
//      FacetecTextKey.READY_MESSAGE_1 to "READY_MESSAGE_1",
//      FacetecTextKey.READY_MESSAGE_2 to "READY_MESSAGE_2",
//      FacetecTextKey.READY_BUTTON to "READY_BUTTON",
//
//      FacetecTextKey.RETRY_HEADER to "RETRY_HEADER",
//      FacetecTextKey.RETRY_SUBHEADER to "RETRY_SUBHEADER",
//      FacetecTextKey.RETRY_MESSAGE_SMILE to "RETRY_MESSAGE_SMILE",
//      FacetecTextKey.RETRY_MESSAGE_LIGHTING to "RETRY_MESSAGE_LIGHTING",
//      FacetecTextKey.RETRY_MESSAGE_CONTRAST to "RETRY_MESSAGE_CONTRAST",
//      FacetecTextKey.RETRY_YOUR_PICTURE to "RETRY_YOUR_PICTURE",
//      FacetecTextKey.RETRY_IDEAL_PICTURE to "RETRY_IDEAL_PICTURE",
//      FacetecTextKey.RETRY_BUTTON to "RETRY_BUTTON",
//
//      FacetecTextKey.RESULT_UPLOAD_MESSAGE to "RESULT_UPLOAD_MESSAGE",
//      FacetecTextKey.RESULT_SUCCESS_MESSAGE to "RESULT_SUCCESS_MESSAGE",
//
//      FacetecTextKey.FEEDBACK_LOOK_STRAIGHT_IN_OVAL to "FEEDBACK_LOOK_STRAIGHT_IN_OVAL",
//
//      FacetecTextKey.FEEDBACK_CENTER_FACE to "FEEDBACK_CENTER_FACE",
//      FacetecTextKey.FEEDBACK_FACE_NOT_FOUND to "FEEDBACK_FACE_NOT_FOUND",
//      FacetecTextKey.FEEDBACK_FACE_NOT_LOOKING_STRAIGHT_AHEAD to "FEEDBACK_FACE_NOT_LOOKING_STRAIGHT_AHEAD",
//      FacetecTextKey.FEEDBACK_FACE_NOT_UPRIGHT to "FEEDBACK_FACE_NOT_UPRIGHT",
//      FacetecTextKey.FEEDBACK_HOLD_STEADY to "FEEDBACK_HOLD_STEADY",
//      FacetecTextKey.FEEDBACK_MOVE_PHONE_AWAY to "FEEDBACK_MOVE_PHONE_AWAY",
//      FacetecTextKey.FEEDBACK_MOVE_PHONE_CLOSER to "FEEDBACK_MOVE_PHONE_CLOSER",
//      FacetecTextKey.FEEDBACK_MOVE_PHONE_TO_EYE_LEVEL to "FEEDBACK_MOVE_PHONE_TO_EYE_LEVEL",
//      FacetecTextKey.FEEDBACK_USE_EVEN_LIGHTING to "FEEDBACK_USE_EVEN_LIGHTING",
//
//      FacetecTextKey.FEEDBACK_FRAME_YOUR_FACE to "FEEDBACK_FRAME_YOUR_FACE",
//      FacetecTextKey.FEEDBACK_HOLD_STEADY_1 to "FEEDBACK_HOLD_STEADY_1",
//      FacetecTextKey.FEEDBACK_HOLD_STEADY_2 to "FEEDBACK_HOLD_STEADY_2",
//      FacetecTextKey.FEEDBACK_HOLD_STEADY_3 to "FEEDBACK_HOLD_STEADY_3",
//      FacetecTextKey.FEEDBACK_REMOVE_DARK_GLASSES to "FEEDBACK_REMOVE_DARK_GLASSES",
//      FacetecTextKey.FEEDBACK_NEUTRAL_EXPRESSION to "FEEDBACK_NEUTRAL_EXPRESSION",
//      FacetecTextKey.FEEDBACK_CONDITIONS_TOO_BRIGHT to "FEEDBACK_CONDITIONS_TOO_BRIGHT",
//      FacetecTextKey.FEEDBACK_BRIGHTEN_YOUR_ENVIRONMENT to "FEEDBACK_BRIGHTEN_YOUR_ENVIRONMENT",
//    )
//
//    guidanceBackgroundColors("#1F1F1F")
//    guidanceForegroundColor("#FFFFFF")
//    guidanceReadyScreenHeaderTextColor("#FFFFFF")
//    guidanceReadyScreenSubtextTextColor("#BBBBBB")
//    guidanceButtonBackgroundHighlightColor("#0F9D58")
//    guidanceButtonTextHighlightColor("#000000")
//    guidanceButtonBorderColor("#0F9D58")
//    guidanceButtonBorderWidth(2)
//    guidanceButtonCornerRadius(12)
//    guidanceReadyScreenOvalFillColor("#00FF00")
//
//    guidanceRetryScreenHeaderTextColor("#FF5252")
//    guidanceRetryScreenSubtextTextColor("#DD3333")
//    guidanceRetryScreenImageBorderColor("#417FB2")
//    guidanceRetryScreenImageBorderWidth(3)
//    guidanceRetryScreenOvalStrokeColor("#FFFFFF")
//
//    resultScreenForegroundColor("#0F9D58")
//    resultScreenBackgroundColors("#DFFFD6")
//    resultScreenActivityIndicatorColor("#0F9D58")
//    resultScreenUploadProgressFillColor("#0F9D58")
//    resultScreenUploadProgressTrackColor("#66000000")
//    resultScreenResultAnimationBackgroundColor("#417FB2")
//    resultScreenResultAnimationForegroundColor("#FFFFFF")
//    resultScreenCustomActivityIndicatorImage(R.drawable.success_icon)
//
//    ovalCustomizationStrokeWidth(4)
//    ovalCustomizationStrokeColor("#00FF00")
//    ovalCustomizationProgressStrokeWidth(6)
//    ovalCustomizationProgressColor1("#00FF00")
//    ovalCustomizationProgressColor2("#FF0000")
//    ovalCustomizationProgressRadialOffset(8)
//
//    frameBorderWidth(2)
//    frameBorderColor("#FFFFFF")
//    frameCornerRadius(8)
//    frameBackgroundColor("#121212")
//
//    overlayBackgroundColor("#80000000")
//    overlayBrandingImage(R.drawable.neutral_face)
//
//    feedbackBackgroundColors("#FFFDE7")
//    feedbackTextColor("#000000")
//    feedbackEnablePulsatingText(true)
//    feedbackElevation(8)
//
//    cancelButtonCustomImage(R.drawable.close_icon)
//    cancelButtonLocation(FacetecButtonLocation.TOP_RIGHT)
//    exitAnimationStyle(FacetecExitAnimationStyle.RIPPLE_IN)
//
//    setFacetecFontsMap(facetecFonts)
//    setFacetecTextMap(facetecTexts)
//
//    resultScreenOverrideSuccessMessage("Toque para reiniciar")
//
//    setInstructionsTheme {
//      setTitleText("Centralize seu rosto")
//      setCaptionText("Mantenha-se dentro do círculo")
//      setStatusBarColor("#121212")
//      setStatusBarIsDarkIcons(false)
//      setBackgroundColor("#121212")
//      setContinueButtonText("Começar")
//      setContinueButtonColor("#0F9D58")
//    }
//
//    setPermissionTheme {
//      setTitle("Permissão de Câmera")
//      setBackgroundColor("#1F1F1F")
//      setStatusBarColor("#1F1F1F")
//      setStatusBarIsDarkIcons(false)
//      setCheckPermissionButtonText("Permitir")
//      setCheckPermissionButtonStyle("#0F9D58")
//    }
//
//    setProcessingTheme {
//      setBackgroundColor("#000000")
//      setLoadingDialogColor("#FFFFFF")
//      setStatusBarColor("#FFFFFF")
//      setStatusBarIsDarkIcons(false)
//      setStatusBarColor("#000000")
//      setLoadingIndicatorSize(80)
//    }
//  }
//
//  fun create(isCustom: Boolean): FacetecTheme =
//    if (isCustom) buildCustom() else buildDefault()
//}
