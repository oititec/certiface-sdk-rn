package br.com.certiface.rn.sdk.factories

import android.content.Context
import br.com.certiface.designsystem.R
import br.com.certiface.designsystem.ui.builders.InstructionImageScale
import br.com.certiface.domain.model.facetec.FacetecButtonLocation
import br.com.certiface.domain.model.facetec.FacetecExitAnimationStyle
import br.com.certiface.domain.model.facetec.FacetecTheme
import android.util.Log
import br.com.certiface.manager.exports.FacetecDrawablesKey
import br.com.certiface.manager.exports.FacetecFontsKey
import br.com.certiface.manager.exports.FacetecTextKey
import br.com.certiface.rn.sdk.theme.FacetecFonts
import br.com.certiface.rn.sdk.theme.FontResolver
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
    val instructionsFlags = instructionsTheme?.getMap("flags")
    val showInstructionScreen = instructionsConfiguration?.getBoolean("showInstructionScreen") ?: true

    val permissionTheme = theme?.getMap("permission")
    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")
    val permissionFonts = permissionTheme?.getMap("fonts")
    val permissionFlags = permissionTheme?.getMap("flags")

    val facetecTheme = theme?.getMap("facetec")
    val facetecColors = facetecTheme?.getMap("colors")
    val facetecSizes = facetecTheme?.getMap("sizes")
    val facetecFlags = facetecTheme?.getMap("flags")
    val facetecConfiguration = facetecTheme?.getMap("configuration")
    val facetecTexts = facetecTheme?.getMap("texts")
    val facetecFontsMap = facetecTheme?.getMap("fonts")
    val instructionsSizes = instructionsTheme?.getMap("sizes")

    val facetecFonts: Map<FacetecFontsKey, Any> =
      if (facetecFontsMap != null || instructionsFonts != null || permissionFonts != null) {
        val rawFonts = FacetecFonts(instructionsFonts, permissionFonts, facetecFontsMap).apply()
        if (context != null) {
          rawFonts.mapValues { (_, path) -> FontResolver.resolveFromAssetPath(context, path) }
        } else {
          rawFonts
        }
      } else {
        hashMapOf(
          FacetecFontsKey.INSTRUCTIONS_TITLE_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.INSTRUCTIONS_CAPTION_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.INSTRUCTIONS_BUTTON_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.PERMISSION_TITLE_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.PERMISSION_CAPTION_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.PERMISSION_BUTTON_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.GUIDANCE_CUSTOMIZATION_HEADER_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.GUIDANCE_CUSTOMIZATION_SUBTEXT_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.GUIDANCE_CUSTOMIZATION_BUTTON_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_HEADER_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_SUBTEXT_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_HEADER_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_SUBTEXT_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.RESULT_SCREEN_CUSTOMIZATION_MESSAGE_FONT to FontResolver.defaultFontRes,
          FacetecFontsKey.FEEDBACK_CUSTOMIZATION_TEXT_FONT to FontResolver.defaultFontRes
        )
      }

    val facetecDrawablesRaw = AssetProcessor.processFacetecAssets(theme)
    val facetecDrawables = AssetProcessor.resolveFacetecDrawables(context, facetecDrawablesRaw)

    if (facetecDrawables.isNotEmpty()) {
      setFacetecDrawablesMap(facetecDrawables)
    } else {
      Log.d(TAG, "Nenhum drawable customizado encontrado, usando padrões")
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
    guidanceReadyScreenTextBackgroundCornerRadius(
      optInt(facetecSizes, "readyScreenTextBackgroundCornerRadius", 12)
    )

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
    guidanceButtonBorderWidth(optInt(facetecSizes, "guidanceButtonBorderWidth", 2))
    guidanceButtonCornerRadius(optInt(facetecSizes, "guidanceButtonCornerRadius", 12))

    // Retry Screen
    guidanceRetryScreenHeaderTextColor(facetecColors?.getString("retryScreenHeader") ?: "#FF5252")
    guidanceRetryScreenSubtextTextColor(facetecColors?.getString("retryScreenSubtext") ?: "#DD3333")
    guidanceRetryScreenOvalStrokeColor(facetecColors?.getString("retryScreenOvalStroke") ?: "#FFFFFF")
    guidanceRetryScreenImageBorderColor(facetecColors?.getString("retryScreenImageBorder") ?: "#417FB2")
    guidanceRetryScreenImageBorderWidth(optInt(facetecSizes, "guidanceRetryScreenImageBorderWidth", 3))
    guidanceRetryScreenImageCornerRadius(optInt(facetecSizes, "guidanceRetryScreenImageCornerRadius", 12))

    // Result Screen
    resultScreenForegroundColor(
      facetecColors?.getString("resultScreenForeground")
        ?: facetecColors?.getString("resultScreenMessage")
        ?: "#0F9D58"
    )
    resultScreenBackgroundColors(facetecColors?.getString("resultScreenBackground") ?: "#DFFFD6")
    resultScreenUploadProgressFillColor(
      facetecColors?.getString("resultScreenUploadProgressFill")
        ?: facetecColors?.getString("resultScreenUploadProgressBarFill")
        ?: "#0F9D58"
    )
    resultScreenUploadProgressTrackColor(
      facetecColors?.getString("resultScreenUploadProgressTrack")
        ?: facetecColors?.getString("resultScreenUploadProgressBarTrack")
        ?: "#66000000"
    )
    resultScreenActivityIndicatorColor(facetecColors?.getString("resultScreenActivityIndicator") ?: "#0F9D58")
    resultScreenResultAnimationBackgroundColor(facetecColors?.getString("resultScreenResultAnimationBackground") ?: "#417FB2")
    resultScreenResultAnimationForegroundColor(facetecColors?.getString("resultScreenResultAnimationForeground") ?: "#FFFFFF")
    facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ACTIVITY_INDICATOR_IMAGE]?.let {
      resultScreenCustomActivityIndicatorImage(it)
    }
    resultScreenCustomActivityIndicatorAnimation(
      facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ACTIVITY_INDICATOR_ANIMATION]
        ?: br.com.certiface.facetecsdk.R.drawable.animated_activity_indicator
    )
    resultScreenCustomActivityIndicatorRotationInterval(
      optInt(facetecSizes, "resultScreenCustomActivityIndicatorRotationInterval", 1000)
    )
    resultScreenAnimationRelativeScale(
      optFloat(facetecSizes, "resultScreenAnimationRelativeScale", 1f)
    )
    resultScreenShowUploadProgressBar(
      optBoolean(facetecFlags, "resultScreenShowUploadProgressBar", true)
    )
    val resultSuccessStaticIconId =
      facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_STATIC_RESULT_ANIMATION_SUCCESS]
        ?: R.drawable.success_icon
    val resultErrorStaticIconId =
      facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_STATIC_RESULT_ANIMATION_UNSUCCESS]
        ?: R.drawable.error_icon
    val resultSuccessAnimatedIconId =
      facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ANIMATION_SUCCESS]
        ?: resultSuccessStaticIconId
    val resultErrorAnimatedIconId =
      facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ANIMATION_UNSUCCESS]
        ?: resultErrorStaticIconId

    resultScreenCustomStaticResultAnimationUnSuccess(resultErrorStaticIconId)
    resultScreenCustomStaticResultAnimationSuccess(resultSuccessStaticIconId)
    resultScreenCustomResultAnimationUnSuccess(resultErrorAnimatedIconId)
    resultScreenCustomResultAnimationSuccess(resultSuccessAnimatedIconId)
    facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_ANIMATION_SUCCESS_BACKGROUND_IMAGE]?.let {
      resultScreenResultAnimationSuccessBackgroundImage(it)
    }
    facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_ANIMATION_UNSUCESS_BACKGROUND_IMAGE]?.let {
      resultScreenResultAnimationUnSuccessBackgroundImage(it)
    }
    facetecTexts?.getString("resultSuccessMessage")?.let { resultScreenOverrideSuccessMessage(it) }

    // Oval
    ovalCustomizationStrokeWidth(optInt(facetecSizes, "ovalStrokeWidth", 4))
    ovalCustomizationStrokeColor(facetecColors?.getString("ovalStroke") ?: "#00FF00")
    ovalCustomizationProgressStrokeWidth(optInt(facetecSizes, "ovalProgressStrokeWidth", 6))
    ovalCustomizationProgressColor1(facetecColors?.getString("ovalProgressFirst") ?: "#00FF00")
    ovalCustomizationProgressColor2(facetecColors?.getString("ovalProgressSecond") ?: "#FF0000")
    ovalCustomizationProgressRadialOffset(optInt(facetecSizes, "ovalProgressRadialOffset", 8))

    // Frame
    frameBackgroundColor(facetecColors?.getString("frameBackground") ?: "#121212")
    frameBorderColor(facetecColors?.getString("frameBorder") ?: "#FFFFFF")
    frameBorderWidth(optInt(facetecSizes, "frameBorderWidth", 2))
    frameCornerRadius(optInt(facetecSizes, "frameCornerRadius", 8))
    frameElevation(optInt(facetecSizes, "frameElevation", 5))

    // Overlay
    overlayBackgroundColor(facetecColors?.getString("overlayBackground") ?: "#80000000")
    overlayShowBrandingImage(optBoolean(facetecFlags, "overlayShowBrandingImage", true))

    // Feedback
    feedbackBackgroundColors(facetecColors?.getString("feedbackBarBackground") ?: "#FFFDE7")
    feedbackTextColor(facetecColors?.getString("feedbackMessage") ?: "#000000")
    feedbackCornerRadius(optInt(facetecSizes, "feedbackCornerRadius", 12))
    feedbackElevation(optInt(facetecSizes, "feedbackElevation", 8))
    feedbackEnablePulsatingText(optBoolean(facetecFlags, "feedbackEnablePulsatingText", true))

    // Cancel Button
    facetecDrawables[FacetecDrawablesKey.FACETEC_CANCEL_BUTTON_CUSTOM_IMAGE]?.let {
      cancelButtonCustomImage(it)
    }
    cancelButtonLocation(
      parseFacetecButtonLocation(facetecConfiguration, "cancelButtonLocation", FacetecButtonLocation.TOP_LEFT)
    )
    exitAnimationStyle(
      parseFacetecExitAnimationStyle(facetecConfiguration, "exitAnimationStyle", FacetecExitAnimationStyle.RIPPLE_IN)
    )

    setFacetecFontsMap(facetecFonts)
    setFacetecTextMap(customFacetecTexts)

    // Instructions Screen
    setInstructionsTheme {
      setShowInstructionScreen(showInstructionScreen)
      setTitleText(firstString(instructionsTexts, "title") ?: "Centralize seu rosto")
      setCaptionText(firstString(instructionsTexts, "caption") ?: "Mantenha-se dentro do círculo")
      setTitleColor(firstString(instructionsColors, "titleColor", "title") ?: "#FFFFFF")
      setCaptionColor(firstString(instructionsColors, "captionColor", "caption") ?: "#AAAAAA")
      setStatusBarColor(firstString(instructionsColors, "statusBarColor", "statusBar") ?: "#121212")
      setStatusBarIsDarkIcons(optBoolean(instructionsFlags, "statusBarIsDarkIcons", false))
      setBackgroundColor(firstString(instructionsColors, "backgroundColor", "background") ?: "#121212")
      setBottomSheetColor(firstString(instructionsColors, "bottomSheetColor", "bottomSheet") ?: "#333333")
      setBottomSheetCornerRadius(optFloat(instructionsSizes, "bottomSheetCornerRadius", 16f))
      setContinueButtonText(
        firstString(instructionsTexts, "continueButton", "continueButtonText") ?: "Começar"
      )
      setContinueButtonColor(
        firstString(instructionsColors, "continueButtonColor", "continueButtonBackground") ?: "#0F9D58"
      )
      setContinueButtonTextColor(
        firstString(instructionsColors, "continueButtonTextColor", "continueButtonText") ?: "#FFFFFF"
      )
      firstString(instructionsTexts, "firstInstruction")?.let { setFirstInstructionText(it) }
      firstString(instructionsTexts, "secondInstruction")?.let { setSecondInstructionText(it) }
      firstString(instructionsColors, "firstInstructionTitle")?.let { setFirstInstructionTextColor(it) }
      firstString(instructionsColors, "secondInstructionTitle")?.let { setSecondInstructionTextColor(it) }
      firstString(instructionsColors, "firstInstructionIconBackground")?.let { setFirstInstructionIconBackgroundColor(it) }
      firstString(instructionsColors, "firstInstructionIconBorder")?.let { setFirstInstructionIconBorderColor(it) }
      firstString(instructionsColors, "secondInstructionIconBackground")?.let { setSecondInstructionIconBackgroundColor(it) }
      firstString(instructionsColors, "secondInstructionIconBorder")?.let { setSecondInstructionIconBorderColor(it) }
      val backButtonDrawableId = facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG]
      backButtonDrawableId?.let { setBackButtonImg(it) }
      resolveInstructionsBackButtonTintColor(instructionsColors, hasCustomBackButtonImage = backButtonDrawableId != null)
        ?.let { setBackButtonColor(it) }
      facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE]?.let { setContextImage(it) }
      facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON]?.let { setFirstInstructionIcon(it) }
      facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON]?.let { setSecondInstructionIcon(it) }
      val instructionsAssets = instructionsTheme?.getMap("assets")
      instructionsAssets?.getString("contextImageScale")
        ?.let { setContextImageScale(InstructionImageScale.fromString(it)) }
      if (instructionsAssets?.hasKey("contextImageHeightFraction") == true)
        setContextImageHeightFraction(instructionsAssets.getDouble("contextImageHeightFraction").toFloat())
      instructionsAssets?.getString("instructionIconScale")
        ?.let { setInstructionIconScale(InstructionImageScale.fromString(it)) }
      if (instructionsAssets?.hasKey("instructionIconSize") == true)
        setInstructionIconSize(instructionsAssets.getDouble("instructionIconSize").toInt())
    }

    setPermissionTheme {
      setTitle(firstString(permissionTexts, "title") ?: "Permissão de Câmera")
      setTitleColor(firstString(permissionColors, "titleColor", "title") ?: "#FFFFFF")
      setSubTitle(firstString(permissionTexts, "caption"))
      setSubTitleColor(firstString(permissionColors, "captionColor", "caption") ?: "#FFFFFF")
      setBackgroundColor(firstString(permissionColors, "backgroundColor", "background") ?: "#1F1F1F")
      setStatusBarColor(firstString(permissionColors, "statusBarColor", "statusBar") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(optBoolean(permissionFlags, "statusBarIsDarkIcons", false))
      setPermissionButtonText(firstString(permissionTexts, "checkPermissionButton") ?: "Permitir acesso")
      setPermissionButtonColor(
        firstString(permissionColors, "checkPermissionButtonBackground", "checkPermissionButtonColor") ?: "#00FF00"
      )
      setPermissionButtonTextColor(firstString(permissionColors, "checkPermissionButtonText") ?: "#000000")
      facetecDrawables[FacetecDrawablesKey.PERMISSION_CAMERA_ICON]?.let { setCameraIcon(it) }
      facetecDrawables[FacetecDrawablesKey.PERMISSION_BACK_BUTTON_ICON]?.let { setBackButtonIcon(it) }
    }

    // Processing Screen
    val processingTheme = theme?.getMap("processing")
    val processingColors = processingTheme?.getMap("colors")
    val processingFlags = processingTheme?.getMap("flags")
    val processingSizes = processingTheme?.getMap("sizes")

    setProcessingTheme {
      setBackgroundColor(firstString(processingColors, "backgroundColor", "background") ?: "#000000")
      setLoadingDialogColor(firstString(processingColors, "loadingDialogColor", "loading") ?: "#FFFFFF")
      setStatusBarColor(firstString(processingColors, "statusBarColor", "statusBar") ?: "#000000")
      setStatusBarIsDarkIcons(optBoolean(processingFlags, "statusBarIsDarkIcons", false))
      setLoadingIndicatorSize(optInt(processingSizes, "loadingIndicatorSize", 80))
    }
  }

  fun create(isCustom: Boolean, theme: ReadableMap? = null, context: Context? = null): FacetecTheme =
    if (isCustom) buildCustom(theme, context) else buildDefault()
}
