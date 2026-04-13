package br.com.certiface.rn.sdk.factories

import android.content.Context
import br.com.certiface.designsystem.R
import br.com.certiface.domain.model.facetec.FacetecButtonLocation
import br.com.certiface.domain.model.facetec.FacetecExitAnimationStyle
import br.com.certiface.domain.model.facetec.FacetecTheme
import android.util.Log
import br.com.certiface.manager.exports.FacetecFontsKey
import br.com.certiface.manager.exports.FacetecTextKey
import br.com.certiface.rn.sdk.theme.FacetecFonts
import br.com.certiface.rn.sdk.processors.AssetProcessor
import com.facebook.react.bridge.ReadableMap

object FacetecThemeFactory {
  private const val TAG = "FacetecThemeFactory"

  fun buildDefault(): FacetecTheme = FacetecTheme.build {}

  fun buildCustom(theme: ReadableMap? = null, context: Context? = null): FacetecTheme = FacetecTheme.build {
    val instructionsTheme = theme?.getMap("instructions")
    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")
    val instructionsFonts = instructionsTheme?.getMap("fonts")
    val instructionsConfiguration = instructionsTheme?.getMap("configuration")
    val showInstructionScreen = instructionsConfiguration?.getBoolean("showInstructionScreen") ?: true

    val permissionTheme = theme?.getMap("permission")
    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")
    val permissionFonts = permissionTheme?.getMap("fonts")

    val facetecTheme = theme?.getMap("facetec")
    val facetecColors = facetecTheme?.getMap("colors")
    val facetecTexts = facetecTheme?.getMap("texts")
    val facetecFontsMap = facetecTheme?.getMap("fonts")

    val facetecFonts = if (facetecFontsMap != null) {
      FacetecFonts(instructionsFonts, permissionFonts, facetecFontsMap).apply()
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

    Log.d(TAG, "🏭 Iniciando construção do tema Facetec customizado...")
    val facetecDrawables = AssetProcessor.processFacetecAssets(theme)
    Log.d(TAG, "📦 Assets processados: ${facetecDrawables.size} encontrados")

    Log.d(TAG, "🎨 Assets encontrados para processamento: ${facetecDrawables.size}")
    facetecDrawables.forEach { (key, value) ->
      Log.d(TAG, "   📎 $key = '$value'")
    }

    if (facetecDrawables.isNotEmpty()) {
      Log.d(TAG, "🎨 Configurando drawables customizados: ${facetecDrawables.size} assets")
      setFacetecDrawablesMap(facetecDrawables)
    } else {
      Log.d(TAG, "📋 Nenhum drawable customizado encontrado, usando padrões")
    }

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

    // Ready Screen
    guidanceReadyScreenHeaderTextColor(facetecColors?.getString("readyScreenHeader") ?: "#FFFFFF")
    guidanceReadyScreenSubtextTextColor(facetecColors?.getString("readyScreenSubtext") ?: "#BBBBBB")
    guidanceReadyScreenTextBackgroundColor(facetecColors?.getString("readyScreenTextBackground") ?: "#BBBBBB")
    guidanceReadyScreenOvalFillColor(facetecColors?.getString("readyScreenOvalFill") ?: "#00FF00")
    guidanceReadyScreenTextBackgroundCornerRadius(12)

    // Guidance
    guidanceForegroundColor(facetecColors?.getString("guidanceForeground") ?: "#FFFFFF")
    guidanceBackgroundColors(facetecColors?.getString("guidanceBackground") ?: "#1F1F1F")
    guidanceButtonTextNormalColor(facetecColors?.getString("guidanceButtonTextNormal") ?: "#3d100c")
    guidanceButtonTextHighlightColor(facetecColors?.getString("guidanceButtonTextHighlight") ?: "#000000")
    guidanceButtonTextDisabledColor(facetecColors?.getString("guidanceButtonTextDisabled") ?: "#000000")
    guidanceButtonBackgroundNormalColor(facetecColors?.getString("guidanceButtonBackgroundNormal") ?: "#00ff00")
    guidanceButtonBackgroundHighlightColor(facetecColors?.getString("guidanceButtonBackgroundHighlight") ?: "#0F9D58")
    guidanceButtonBackgroundDisabledColor(facetecColors?.getString("guidanceButtonBackgroundDisabled") ?: "#ff0000")
    guidanceButtonBorderColor(facetecColors?.getString("guidanceButtonBorder") ?: "#0F9D58")
    guidanceButtonBorderWidth(2)
    guidanceButtonCornerRadius(12)

    // Retry Screen
    guidanceRetryScreenHeaderTextColor(facetecColors?.getString("retryScreenHeader") ?: "#FF5252")
    guidanceRetryScreenSubtextTextColor(facetecColors?.getString("retryScreenSubtext") ?: "#DD3333")
    guidanceRetryScreenOvalStrokeColor(facetecColors?.getString("retryScreenOvalStroke") ?: "#FFFFFF")
    guidanceRetryScreenImageBorderColor(facetecColors?.getString("retryScreenImageBorder") ?: "#417FB2")
    guidanceRetryScreenImageBorderWidth(3)
    guidanceRetryScreenImageCornerRadius(12)

    // Result Screen
    resultScreenForegroundColor(facetecColors?.getString("resultScreenForeground") ?: "#0F9D58")
    resultScreenBackgroundColors(facetecColors?.getString("resultScreenBackground") ?: "#DFFFD6")
    resultScreenUploadProgressFillColor(facetecColors?.getString("resultScreenUploadProgressFill") ?: "#0F9D58")
    resultScreenUploadProgressTrackColor(facetecColors?.getString("resultScreenUploadProgressTrack") ?: "#66000000")
    resultScreenActivityIndicatorColor(facetecColors?.getString("resultScreenActivityIndicator") ?: "#0F9D58")
    resultScreenResultAnimationBackgroundColor(facetecColors?.getString("resultScreenResultAnimationBackground") ?: "#417FB2")
    resultScreenResultAnimationForegroundColor(facetecColors?.getString("resultScreenResultAnimationForeground") ?: "#FFFFFF")
    resultScreenCustomActivityIndicatorAnimation(br.com.certiface.facetecsdk.R.drawable.animated_activity_indicator)
    resultScreenCustomActivityIndicatorRotationInterval(1000)
    resultScreenAnimationRelativeScale(1f)
    resultScreenShowUploadProgressBar(true)
    resultScreenCustomStaticResultAnimationUnSuccess(R.drawable.error_icon)
    resultScreenCustomStaticResultAnimationSuccess(R.drawable.success_icon)
    resultScreenCustomResultAnimationUnSuccess(R.drawable.error_icon)
    resultScreenCustomResultAnimationSuccess(R.drawable.success_icon)
    resultScreenResultAnimationUnSuccessBackgroundImage(R.drawable.error_icon)
    resultScreenResultAnimationSuccessBackgroundImage(R.drawable.success_icon)
    resultScreenOverrideSuccessMessage("Toque para reiniciar")

    // Oval
    ovalCustomizationStrokeWidth(4)
    ovalCustomizationStrokeColor(facetecColors?.getString("ovalStroke") ?: "#00FF00")
    ovalCustomizationProgressStrokeWidth(6)
    ovalCustomizationProgressColor1(facetecColors?.getString("ovalProgressFirst") ?: "#00FF00")
    ovalCustomizationProgressColor2(facetecColors?.getString("ovalProgressSecond") ?: "#FF0000")
    ovalCustomizationProgressRadialOffset(8)

    // Frame
    frameBackgroundColor(facetecColors?.getString("frameBackground") ?: "#121212")
    frameBorderColor(facetecColors?.getString("frameBorder") ?: "#FFFFFF")
    frameBorderWidth(2)
    frameCornerRadius(8)
    frameElevation(5)

    // Overlay
    overlayBackgroundColor(facetecColors?.getString("overlayBackground") ?: "#80000000")
    overlayShowBrandingImage(true)

    // Feedback
    feedbackBackgroundColors(facetecColors?.getString("feedbackBarBackground") ?: "#FFFDE7")
    feedbackTextColor(facetecColors?.getString("feedbackMessage") ?: "#000000")
    feedbackCornerRadius(12)
    feedbackElevation(8)
    feedbackEnablePulsatingText(true)

    // Cancel Button
    cancelButtonLocation(FacetecButtonLocation.TOP_RIGHT)
    exitAnimationStyle(FacetecExitAnimationStyle.RIPPLE_IN)

    setFacetecFontsMap(facetecFonts)
    setFacetecTextMap(customFacetecTexts)

    // Instructions Screen
    setInstructionsTheme {
      setShowInstructionScreen(showInstructionScreen)
      setTitleText(instructionsTexts?.getString("title") ?: "Centralize seu rosto")
      setCaptionText(instructionsTexts?.getString("caption") ?: "Mantenha-se dentro do círculo")
      setStatusBarColor(instructionsColors?.getString("statusBar") ?: "#121212")
      setStatusBarIsDarkIcons(false)
      setBackgroundColor(instructionsColors?.getString("background") ?: "#121212")
      setContinueButtonText(instructionsTexts?.getString("continueButtonText") ?: "Começar")
      setContinueButtonColor(instructionsColors?.getString("continueButtonBackground") ?: "#0F9D58")
    }

    // Permission Screen
    setPermissionTheme {
      setTitle(permissionTexts?.getString("title") ?: "Permissão de Câmera")
      setBackgroundColor(permissionColors?.getString("background") ?: "#1F1F1F")
      setStatusBarColor(permissionColors?.getString("statusBar") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(false)
    }

    // Processing Screen
    val processingTheme = theme?.getMap("processing")
    val processingColors = processingTheme?.getMap("colors")

    setProcessingTheme {
      setBackgroundColor(processingColors?.getString("background") ?: "#000000")
      setLoadingDialogColor(processingColors?.getString("loading") ?: "#FFFFFF")
      setStatusBarColor(processingColors?.getString("statusBar") ?: "#000000")
      setStatusBarIsDarkIcons(false)
      setLoadingIndicatorSize(80)
    }
  }

  fun create(isCustom: Boolean, theme: ReadableMap? = null, context: Context? = null): FacetecTheme =
    if (isCustom) buildCustom(theme, context) else buildDefault()
}
