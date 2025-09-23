package br.com.oititec.rn.sdk.factories

import android.content.Context
import br.com.oiti.designsystem.R
import br.com.oiti.domain.model.facetec.FacetecButtonLocation
import br.com.oiti.domain.model.facetec.FacetecExitAnimationStyle
import br.com.oiti.domain.model.facetec.FacetecTheme
import br.com.oiti.manager.exports.FacetecFontsKey
import br.com.oiti.manager.exports.FacetecTextKey
import br.com.oititec.rn.sdk.theme.FacetecFonts
import br.com.oititec.rn.sdk.managers.AssetManager
import com.facebook.react.bridge.ReadableMap

object FacetecThemeFactory {

  fun buildDefault(): FacetecTheme = FacetecTheme.build {}

  fun buildCustom(theme: ReadableMap? = null, context: Context? = null): FacetecTheme = FacetecTheme.build {

    val facetecTheme = theme?.getMap("facetec")
    val facetecColors = facetecTheme?.getMap("colors")
    val facetecTexts = facetecTheme?.getMap("texts")
    val facetecFontsMap = facetecTheme?.getMap("fonts")

    val facetecFonts = if (facetecFontsMap != null) {
      FacetecFonts(facetecFontsMap).apply()
    } else {
      hashMapOf(
        FacetecFontsKey.INSTRUCTIONS_TITLE_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.INSTRUCTIONS_CAPTION_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.INSTRUCTIONS_BUTTON_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.PERMISSION_TITLE_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.PERMISSION_CAPTION_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.PERMISSION_BUTTON_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.GUIDANCE_CUSTOMIZATION_HEADER_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.GUIDANCE_CUSTOMIZATION_SUBTEXT_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.GUIDANCE_CUSTOMIZATION_BUTTON_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_HEADER_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_SUBTEXT_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_HEADER_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_SUBTEXT_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.RESULT_SCREEN_CUSTOMIZATION_MESSAGE_FONT to R.font.ubuntu_regular,
        FacetecFontsKey.FEEDBACK_CUSTOMIZATION_TEXT_FONT to R.font.ubuntu_regular
      )
    }

    context?.let { ctx ->
      AssetManager.initialize(ctx, theme)
    }

    val overlayBrandingImageId = AssetManager.getProcessedAsset("facetec_overlay_branding")
    val cancelButtonImageId = AssetManager.getProcessedAsset("facetec_cancel_button")
    val activityIndicatorImageId = AssetManager.getProcessedAsset("facetec_activity_indicator")

    val customFacetecTexts = hashMapOf<FacetecTextKey, String>()

    facetecTexts?.getString("readyHeader1")?.let { customFacetecTexts[FacetecTextKey.READY_HEADER_1] = it }
    facetecTexts?.getString("readyHeader2")?.let { customFacetecTexts[FacetecTextKey.READY_HEADER_2] = it }
    facetecTexts?.getString("readyMessage1")?.let { customFacetecTexts[FacetecTextKey.READY_MESSAGE_1] = it }
    facetecTexts?.getString("readyMessage2")?.let { customFacetecTexts[FacetecTextKey.READY_MESSAGE_2] = it }
    facetecTexts?.getString("readyButton")?.let { customFacetecTexts[FacetecTextKey.READY_BUTTON] = it }

    facetecTexts?.getString("retryHeader")?.let { customFacetecTexts[FacetecTextKey.RETRY_HEADER] = it }
    facetecTexts?.getString("retrySubheader")?.let { customFacetecTexts[FacetecTextKey.RETRY_SUBHEADER] = it }
    facetecTexts?.getString("retryMessageSmile")?.let { customFacetecTexts[FacetecTextKey.RETRY_MESSAGE_SMILE] = it }
    facetecTexts?.getString("retryMessageLighting")?.let { customFacetecTexts[FacetecTextKey.RETRY_MESSAGE_LIGHTING] = it }
    facetecTexts?.getString("retryMessageContrast")?.let { customFacetecTexts[FacetecTextKey.RETRY_MESSAGE_CONTRAST] = it }
    facetecTexts?.getString("retryYourPicture")?.let { customFacetecTexts[FacetecTextKey.RETRY_YOUR_PICTURE] = it }
    facetecTexts?.getString("retryIdealPicture")?.let { customFacetecTexts[FacetecTextKey.RETRY_IDEAL_PICTURE] = it }
    facetecTexts?.getString("retryButton")?.let { customFacetecTexts[FacetecTextKey.RETRY_BUTTON] = it }

    facetecTexts?.getString("resultUploadMessage")?.let { customFacetecTexts[FacetecTextKey.RESULT_UPLOAD_MESSAGE] = it }
    facetecTexts?.getString("resultSuccessMessage")?.let { customFacetecTexts[FacetecTextKey.RESULT_SUCCESS_MESSAGE] = it }

    facetecTexts?.getString("feedbackLookStraightInOval")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_LOOK_STRAIGHT_IN_OVAL] = it }
    facetecTexts?.getString("feedbackCenterFace")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_CENTER_FACE] = it }
    facetecTexts?.getString("feedbackFaceNotFound")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_FACE_NOT_FOUND] = it }
    facetecTexts?.getString("feedbackFaceNotLookingStraightAhead")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_FACE_NOT_LOOKING_STRAIGHT_AHEAD] = it }
    facetecTexts?.getString("feedbackFaceNotUpright")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_FACE_NOT_UPRIGHT] = it }
    facetecTexts?.getString("feedbackHoldSteady")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_HOLD_STEADY] = it }
    facetecTexts?.getString("feedbackMovePhoneAway")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_MOVE_PHONE_AWAY] = it }
    facetecTexts?.getString("feedbackMovePhoneCloser")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_MOVE_PHONE_CLOSER] = it }
    facetecTexts?.getString("feedbackMovePhoneToEyeLevel")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_MOVE_PHONE_TO_EYE_LEVEL] = it }
    facetecTexts?.getString("feedbackUseEvenLighting")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_USE_EVEN_LIGHTING] = it }
    facetecTexts?.getString("feedbackFrameYourFace")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_FRAME_YOUR_FACE] = it }
    facetecTexts?.getString("feedbackHoldSteady1")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_HOLD_STEADY_1] = it }
    facetecTexts?.getString("feedbackHoldSteady2")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_HOLD_STEADY_2] = it }
    facetecTexts?.getString("feedbackHoldSteady3")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_HOLD_STEADY_3] = it }
    facetecTexts?.getString("feedbackRemoveDarkGlasses")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_REMOVE_DARK_GLASSES] = it }
    facetecTexts?.getString("feedbackNeutralExpression")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_NEUTRAL_EXPRESSION] = it }
    facetecTexts?.getString("feedbackConditionsTooBright")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_CONDITIONS_TOO_BRIGHT] = it }
    facetecTexts?.getString("feedbackBrightenYourEnvironment")?.let { customFacetecTexts[FacetecTextKey.FEEDBACK_BRIGHTEN_YOUR_ENVIRONMENT] = it }

    guidanceBackgroundColors(facetecColors?.getString("guidanceBackgroundColors") ?: "#1F1F1F")
    guidanceForegroundColor(facetecColors?.getString("guidanceForegroundColor") ?: "#FFFFFF")
    guidanceReadyScreenHeaderTextColor(facetecColors?.getString("guidanceReadyScreenHeaderTextColor") ?: "#FFFFFF")
    guidanceReadyScreenSubtextTextColor(facetecColors?.getString("guidanceReadyScreenSubtextTextColor") ?: "#BBBBBB")
    guidanceReadyScreenTextBackgroundColor(facetecColors?.getString("guidanceReadyScreenTextBackgroundColor") ?: "#BBBBBB")
    guidanceReadyScreenTextBackgroundCornerRadius(12)
    guidanceButtonBackgroundHighlightColor(facetecColors?.getString("guidanceButtonBackgroundHighlightColor") ?: "#0F9D58")
    guidanceButtonTextHighlightColor(facetecColors?.getString("guidanceButtonTextHighlightColor") ?: "#000000")
    guidanceButtonBorderColor(facetecColors?.getString("guidanceButtonBorderColor") ?: "#0F9D58")
    guidanceButtonBackgroundDisabledColor(facetecColors?.getString("guidanceButtonBackgroundDisabledColor") ?: "#ff0000")
    guidanceButtonTextDisabledColor(facetecColors?.getString("guidanceButtonTextDisabledColor") ?: "#000000")
    guidanceButtonBackgroundNormalColor(facetecColors?.getString("guidanceButtonBackgroundNormalColor") ?: "#00ff00")
    guidanceButtonTextNormalColor(facetecColors?.getString("guidanceButtonTextNormalColor") ?: "#3d100c")
    guidanceButtonBorderWidth(2)
    guidanceButtonCornerRadius(12)
    guidanceReadyScreenOvalFillColor(facetecColors?.getString("guidanceReadyScreenOvalFillColor") ?: "#00FF00")

    guidanceRetryScreenHeaderTextColor(facetecColors?.getString("guidanceRetryScreenHeaderTextColor") ?: "#FF5252")
    guidanceRetryScreenSubtextTextColor(facetecColors?.getString("guidanceRetryScreenSubtextTextColor") ?: "#DD3333")
    guidanceRetryScreenImageBorderColor(facetecColors?.getString("guidanceRetryScreenImageBorderColor") ?: "#417FB2")
    guidanceRetryScreenImageBorderWidth(3)
    guidanceRetryScreenOvalStrokeColor(facetecColors?.getString("guidanceRetryScreenOvalStrokeColor") ?: "#FFFFFF")
    guidanceRetryScreenImageCornerRadius(12)

    resultScreenForegroundColor(facetecColors?.getString("resultScreenForegroundColor") ?: "#0F9D58")
    resultScreenBackgroundColors(facetecColors?.getString("resultScreenBackgroundColors") ?: "#DFFFD6")
    resultScreenAnimationRelativeScale(1f)
    resultScreenActivityIndicatorColor(facetecColors?.getString("resultScreenActivityIndicatorColor") ?: "#0F9D58")
    resultScreenUploadProgressFillColor(facetecColors?.getString("resultScreenUploadProgressFillColor") ?: "#0F9D58")
    resultScreenShowUploadProgressBar(true)
    resultScreenCustomActivityIndicatorAnimation(br.com.oiti.facetecsdk.R.drawable.animated_activity_indicator)
    resultScreenCustomActivityIndicatorRotationInterval(1000)
    resultScreenUploadProgressTrackColor(facetecColors?.getString("resultScreenUploadProgressTrackColor") ?: "#66000000")
    resultScreenResultAnimationBackgroundColor(facetecColors?.getString("resultScreenResultAnimationBackgroundColor") ?: "#417FB2")
    resultScreenResultAnimationForegroundColor(facetecColors?.getString("resultScreenResultAnimationForegroundColor") ?: "#FFFFFF")
    activityIndicatorImageId?.let {
      resultScreenCustomActivityIndicatorImage(it)
    } ?: run {
      resultScreenCustomActivityIndicatorImage(R.drawable.success_icon)
    }
    resultScreenCustomStaticResultAnimationUnSuccess(R.drawable.error_icon)
    resultScreenCustomStaticResultAnimationSuccess(R.drawable.success_icon)
    resultScreenCustomResultAnimationUnSuccess(R.drawable.error_icon)
    resultScreenCustomResultAnimationSuccess(R.drawable.success_icon)
    resultScreenResultAnimationUnSuccessBackgroundImage(R.drawable.error_icon)
    resultScreenResultAnimationSuccessBackgroundImage(R.drawable.success_icon)

    ovalCustomizationStrokeWidth(4)
    ovalCustomizationStrokeColor(facetecColors?.getString("ovalCustomizationStrokeColor") ?: "#00FF00")
    ovalCustomizationProgressStrokeWidth(6)
    ovalCustomizationProgressColor1(facetecColors?.getString("ovalCustomizationProgressColor1") ?: "#00FF00")
    ovalCustomizationProgressColor2(facetecColors?.getString("ovalCustomizationProgressColor2") ?: "#FF0000")
    ovalCustomizationProgressRadialOffset(8)

    frameBorderWidth(2)
    frameBorderColor(facetecColors?.getString("frameBorderColor") ?: "#FFFFFF")
    frameCornerRadius(8)
    frameBackgroundColor(facetecColors?.getString("frameBackgroundColor") ?: "#121212")
    frameElevation(5)

    overlayBackgroundColor(facetecColors?.getString("overlayBackgroundColor") ?: "#80000000")
    overlayBrandingImageId?.let {
      overlayBrandingImage(it)
    } ?: run {
      overlayBrandingImage(R.drawable.neutral_face)
    }
    overlayShowBrandingImage(true)

    feedbackCornerRadius(12)
    feedbackBackgroundColors(facetecColors?.getString("feedbackBackgroundColors") ?: "#FFFDE7")
    feedbackTextColor(facetecColors?.getString("feedbackTextColor") ?: "#000000")
    feedbackEnablePulsatingText(true)
    feedbackElevation(8)

    cancelButtonImageId?.let {
      cancelButtonCustomImage(it)
    } ?: run {
      cancelButtonCustomImage(R.drawable.close_icon)
    }
    cancelButtonLocation(FacetecButtonLocation.TOP_RIGHT)
    exitAnimationStyle(FacetecExitAnimationStyle.RIPPLE_IN)

    setFacetecFontsMap(facetecFonts)
    setFacetecTextMap(customFacetecTexts)

    resultScreenOverrideSuccessMessage("Toque para reiniciar")

    val instructionsTheme = theme?.getMap("instructions")
    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")

    setInstructionsTheme {
      setTitleText(instructionsTexts?.getString("titleText") ?: "Centralize seu rosto")
      setCaptionText(instructionsTexts?.getString("captionText") ?: "Mantenha-se dentro do círculo")
      setStatusBarColor(instructionsColors?.getString("statusBarColor") ?: "#121212")
      setStatusBarIsDarkIcons(false)
      setBackgroundColor(instructionsColors?.getString("backgroundColor") ?: "#121212")
      setContinueButtonText(instructionsTexts?.getString("continueButtonText") ?: "Começar")
      setContinueButtonColor(instructionsColors?.getString("continueButtonColor") ?: "#0F9D58")
    }

    val permissionTheme = theme?.getMap("permission")
    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")

    setPermissionTheme {
      setTitle(permissionTexts?.getString("title") ?: "Permissão de Câmera")
      setBackgroundColor(permissionColors?.getString("backgroundColor") ?: "#1F1F1F")
      setStatusBarColor(permissionColors?.getString("statusBarColor") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(false)
      setCheckPermissionButtonText(permissionTexts?.getString("checkPermissionButtonText") ?: "Permitir")
      setCheckPermissionButtonStyle(permissionColors?.getString("checkPermissionButtonColor") ?: "#0F9D58")
    }

    val processingTheme = theme?.getMap("processing")
    val processingColors = processingTheme?.getMap("colors")

    setProcessingTheme {
      setBackgroundColor(processingColors?.getString("backgroundColor") ?: "#000000")
      setLoadingDialogColor(processingColors?.getString("loadingDialogColor") ?: "#FFFFFF")
      setStatusBarColor(processingColors?.getString("statusBarColor") ?: "#000000")
      setStatusBarIsDarkIcons(false)
      setLoadingIndicatorSize(80)
    }
  }

  fun create(isCustom: Boolean, theme: ReadableMap? = null, context: Context? = null): FacetecTheme =
    if (isCustom) buildCustom(theme, context) else buildDefault()
}
