package br.com.certiface.rn.sdk.factories

import android.content.Context
import br.com.certiface.designsystem.R
import br.com.certiface.designsystem.ui.builders.InstructionImageScale
import br.com.certiface.domain.model.fortface.FortfaceCancelPosition
import br.com.certiface.domain.model.fortface.FortfaceScreenMode
import br.com.certiface.domain.model.fortface.FortfaceScreenOrientation
import br.com.certiface.domain.model.fortface.FortfaceTheme
import br.com.certiface.manager.exports.FortfaceDrawablesKey
import br.com.certiface.manager.exports.FortfaceFontsKey
import br.com.certiface.manager.exports.FortfaceTextKey
import br.com.certiface.rn.sdk.processors.AssetProcessor
import br.com.certiface.rn.sdk.theme.FontResolver
import br.com.certiface.rn.sdk.theme.FortfaceFonts
import com.facebook.react.bridge.ReadableMap

object FortfaceThemeFactory {
  fun buildDefault(showInstructionScreen: Boolean = true): FortfaceTheme =
    FortfaceTheme.build {
      setInstructionsTheme {
        setShowInstructionScreen(showInstructionScreen)
      }
      cancelButtonEnable(false)
    }

  fun buildCustom(theme: ReadableMap?, context: Context? = null): FortfaceTheme {
    val fortfaceTheme = theme?.getMap("fortface")
    val colors = fortfaceTheme?.getMap("colors")
    val texts = fortfaceTheme?.getMap("texts")
    val fontsMap = fortfaceTheme?.getMap("fonts")
    val sizes = fortfaceTheme?.getMap("sizes")
    val flags = fortfaceTheme?.getMap("flags")
    val configuration = fortfaceTheme?.getMap("configuration")

    val instructionsTheme = theme?.getMap("instructions")
    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")
    val instructionsFonts = instructionsTheme?.getMap("fonts")
    val instructionsConfiguration = instructionsTheme?.getMap("configuration")
    val instructionsFlags = instructionsTheme?.getMap("flags")
    val instructionsSizes = instructionsTheme?.getMap("sizes")
    val showInstructionScreen =
      optBoolean(instructionsConfiguration, "showInstructionScreen", true)

    val permissionTheme = theme?.getMap("permission")
    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")
    val permissionFonts = permissionTheme?.getMap("fonts")
    val permissionFlags = permissionTheme?.getMap("flags")

    val processingTheme = theme?.getMap("processing")
    val processingColors = processingTheme?.getMap("colors")
    val processingFlags = processingTheme?.getMap("flags")
    val processingSizes = processingTheme?.getMap("sizes")
    val processingFonts = processingTheme?.getMap("fonts")
    val processingTexts = processingTheme?.getMap("texts")

    val resultTheme = theme?.getMap("result")
    val resultColors = resultTheme?.getMap("colors")
    val resultTexts = resultTheme?.getMap("texts")
    val resultFlags = resultTheme?.getMap("flags")
    val resultFonts = resultTheme?.getMap("fonts")

    val drawablesRaw = AssetProcessor.processFortfaceAssets(theme)
    val drawables = AssetProcessor.resolveFortfaceDrawables(context, drawablesRaw)
    val hasCustomBackButton =
      drawables.containsKey(FortfaceDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG)

    fun appDrawable(key: FortfaceDrawablesKey): Int? =
      AssetProcessor.resolveDrawableResourceId(context, drawables[key])
        ?: AssetProcessor.resolveDrawableResourceId(context, drawablesRaw[key])

    val resolvedFonts = resolveConfiguredFonts(
      context = context,
      instructionsFonts = instructionsFonts,
      permissionFonts = permissionFonts,
      fortfaceFonts = fontsMap,
      resultFonts = resultFonts,
      processingFonts = processingFonts
    )

    val textMap = buildTextMap(texts)

    return FortfaceTheme.build {
      if (resolvedFonts.isNotEmpty()) {
        setFontsKey(resolvedFonts)
      }
      if (drawables.isNotEmpty()) {
        setDrawablesKey(drawables)
      }
      if (textMap.isNotEmpty()) {
        setTextMap(textMap)
      }

      firstString(configuration, "customizationJsonFileName")?.let {
        setCustomizationJsonFileName(it)
      }

      cancelButtonEnable(optBoolean(flags, "cancelButtonEnable", true))
      cancelPosition(
        parseFortfaceCancelPosition(configuration, "cancelPosition", FortfaceCancelPosition.LEFT)
      )
      screenMode(
        parseFortfaceScreenMode(configuration, "screenMode", FortfaceScreenMode.FULL_SCREEN)
      )
      screenOrientation(
        parseFortfaceScreenOrientation(
          configuration,
          "screenOrientation",
          FortfaceScreenOrientation.AUTOMATIC
        )
      )

      cameraTimeout(optInt(sizes, "cameraTimeout", 30).coerceAtLeast(20))
      cameraMinStabilizationTime(optInt(sizes, "cameraMinStabilizationTime", 2))
      cameraMaxStabilizationTime(optInt(sizes, "cameraMaxStabilizationTime", 3))
      brightnessValidationTimeout(optInt(sizes, "brightnessValidationTimeout", 10))
      cameraFrameTextVisible(optBoolean(flags, "cameraFrameTextVisible", true))

      if (sizes?.hasKey("modalOverlayOpacity") == true) {
        modalOverlayOpacity(optFloatOrNull(sizes, "modalOverlayOpacity") ?: 0.5f)
      }

      firstString(colors, "modalOverlay")?.let { modalOverlayColor(it) }
      firstString(colors, "cameraBackground")?.let { cameraBackgroundColor(it) }
      firstString(colors, "cameraMessageText")?.let { cameraMessageTextColor(it) }
      firstString(colors, "cameraAlert")?.let { cameraAlertColor(it) }
      firstString(colors, "cameraNeutral")?.let { cameraNeutralColor(it) }
      firstString(colors, "cameraSuccess")?.let { cameraSuccessColor(it) }
      firstString(colors, "cameraBrightnessAlert")?.let { cameraBrightnessAlertColor(it) }
      firstString(colors, "cameraLoading")?.let { cameraLoadingColor(it) }
      firstString(colors, "cameraIconBackground")?.let { cameraIconBackgroundColor(it) }

      setInstructionsTheme {
        setShowInstructionScreen(showInstructionScreen)
        setTitleText(firstString(instructionsTexts, "title", "titleText") ?: "Centralize seu rosto")
        setTitleColor(firstString(instructionsColors, "titleColor", "title") ?: "#FFFFFF")
        setCaptionText(
          firstString(instructionsTexts, "caption", "captionText")
            ?: "Mantenha-se dentro do círculo"
        )
        setCaptionColor(firstString(instructionsColors, "captionColor", "caption") ?: "#AAAAAA")
        setBackgroundColor(
          firstString(instructionsColors, "backgroundColor", "background") ?: "#1F1F1F"
        )
        setStatusBarColor(
          firstString(instructionsColors, "statusBarColor", "statusBar") ?: "#1F1F1F"
        )
        setStatusBarIsDarkIcons(optBoolean(instructionsFlags, "statusBarIsDarkIcons", false))
        setBottomSheetColor(
          firstString(instructionsColors, "bottomSheetColor", "bottomSheet") ?: "#333333"
        )
        setBottomSheetCornerRadius(optFloat(instructionsSizes, "bottomSheetCornerRadius", 16f))
        setContinueButtonText(
          firstString(instructionsTexts, "continueButton", "continueButtonText") ?: "Começar"
        )
        setContinueButtonColor(
          firstString(instructionsColors, "continueButtonColor", "continueButtonBackground")
            ?: "#00FF00"
        )
        setContinueButtonTextColor(
          firstString(instructionsColors, "continueButtonTextColor", "continueButtonText")
            ?: "#000000"
        )
        firstString(instructionsTexts, "firstInstruction")?.let { setFirstInstructionText(it) }
        firstString(instructionsTexts, "secondInstruction")?.let { setSecondInstructionText(it) }
        firstString(instructionsColors, "firstInstructionTitle")?.let {
          setFirstInstructionTextColor(it)
        }
        firstString(instructionsColors, "secondInstructionTitle")?.let {
          setSecondInstructionTextColor(it)
        }
        firstString(instructionsColors, "firstInstructionIconBackground")?.let {
          setFirstInstructionIconBackgroundColor(it)
        }
        firstString(instructionsColors, "firstInstructionIconBorder")?.let {
          setFirstInstructionIconBorderColor(it)
        }
        firstString(instructionsColors, "secondInstructionIconBackground")?.let {
          setSecondInstructionIconBackgroundColor(it)
        }
        firstString(instructionsColors, "secondInstructionIconBorder")?.let {
          setSecondInstructionIconBorderColor(it)
        }

        appDrawable(FortfaceDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG)?.let { setBackButtonImg(it) }
        resolveInstructionsBackButtonTintColor(
          instructionsColors,
          hasCustomBackButtonImage = hasCustomBackButton
        )?.let { setBackButtonColor(it) }

        appDrawable(FortfaceDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE)?.let { setContextImage(it) }
        appDrawable(FortfaceDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON)?.let {
          setFirstInstructionIcon(it)
        }
        appDrawable(FortfaceDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON)?.let {
          setSecondInstructionIcon(it)
        }

        val instructionsAssets = instructionsTheme?.getMap("assets")
        instructionsAssets?.getString("contextImageScale")
          ?.let { setContextImageScale(InstructionImageScale.fromString(it)) }
        if (instructionsAssets?.hasKey("contextImageHeightFraction") == true) {
          setContextImageHeightFraction(
            optFloatOrNull(instructionsAssets, "contextImageHeightFraction") ?: 0.5f
          )
        }
        instructionsAssets?.getString("instructionIconScale")
          ?.let { setInstructionIconScale(InstructionImageScale.fromString(it)) }
        if (instructionsAssets?.hasKey("instructionIconSize") == true) {
          setInstructionIconSize(
            clampedThemeInt(instructionsAssets, "instructionIconSize", 16, 256) ?: 60
          )
        }
      }

      setPermissionTheme {
        setTitle(firstString(permissionTexts, "title") ?: "Permissões Necessárias")
        setTitleColor(firstString(permissionColors, "titleColor", "title") ?: "#FFFFFF")
        setSubTitle(firstString(permissionTexts, "caption"))
        setSubTitleColor(firstString(permissionColors, "captionColor", "caption") ?: "#FFFFFF")
        setBackgroundColor(
          firstString(permissionColors, "backgroundColor", "background") ?: "#1F1F1F"
        )
        setStatusBarColor(
          firstString(permissionColors, "statusBarColor", "statusBar") ?: "#1F1F1F"
        )
        setStatusBarIsDarkIcons(optBoolean(permissionFlags, "statusBarIsDarkIcons", false))
        setPermissionButtonText(
          firstString(permissionTexts, "checkPermissionButton") ?: "Permitir acesso"
        )
        setPermissionButtonColor(
          firstString(
            permissionColors,
            "checkPermissionButtonBackground",
            "checkPermissionButtonColor"
          ) ?: "#00FF00"
        )
        setPermissionButtonTextColor(
          firstString(permissionColors, "checkPermissionButtonText") ?: "#000000"
        )
        appDrawable(FortfaceDrawablesKey.PERMISSION_CAMERA_ICON)?.let { setCameraIcon(it) }
        appDrawable(FortfaceDrawablesKey.PERMISSION_BACK_BUTTON_ICON)?.let { setBackButtonIcon(it) }
      }

      setProcessingTheme {
        setBackgroundColor(
          firstString(processingColors, "backgroundColor", "background") ?: "#000000"
        )
        setLoadingDialogColor(
          firstString(processingColors, "loadingDialogColor", "loading") ?: "#FFFFFF"
        )
        setStatusBarColor(
          firstString(processingColors, "statusBarColor", "statusBar") ?: "#000000"
        )
        setStatusBarIsDarkIcons(optBoolean(processingFlags, "statusBarIsDarkIcons", true))
        setLoadingIndicatorSize(clampedInt(processingSizes, "loadingIndicatorSize", 100, 8, 512))
        setLoadingIndicatorWidth(clampedInt(processingSizes, "loadingIndicatorWidth", 10, 1, 64))
        firstString(processingTexts, "message")
          ?.let { setProcessingMessage(it) }
          ?: firstString(texts, "processingMessage")?.let { setProcessingMessage(it) }
      }

      val successIconId =
        appDrawable(FortfaceDrawablesKey.RESULT_SUCCESS_ICON)
          ?: R.drawable.success_icon
      val errorIconId =
        appDrawable(FortfaceDrawablesKey.RESULT_ERROR_ICON)
          ?: R.drawable.error_icon

      setResultTheme {
        setSuccessBackgroundColor(
          firstString(resultColors, "successBackgroundColor", "successBackground") ?: "#DFFFD6"
        )
        setSuccessIcon(successIconId)
        setSuccessText(
          firstString(resultTexts, "successText", "success")
            ?: "Verificação concluída com sucesso!"
        )
        setSuccessTextColor(
          firstString(resultColors, "successTextColor", "successText") ?: "#0F9D58"
        )
        setStatusBarSuccessColor(
          firstString(resultColors, "statusBarSuccessColor", "successStatusBar") ?: "#DFFFD6"
        )
        setStatusBarErrorColor(
          firstString(resultColors, "statusBarErrorColor", "errorStatusBar") ?: "#FFD6D6"
        )
        setStatusBarSuccessIsDarkIcons(
          optBoolean(resultFlags, "successStatusBarIsDarkIcons", true)
        )
        setStatusBarErrorIsDarkIcons(optBoolean(resultFlags, "errorStatusBarIsDarkIcons", true))
        setErrorBackgroundColor(
          firstString(resultColors, "errorBackgroundColor", "errorBackground") ?: "#FFD6D6"
        )
        setErrorIcon(errorIconId)
        setErrorText(
          firstString(resultTexts, "errorText", "error") ?: "Algo deu errado na verificação."
        )
        setErrorTextColor(firstString(resultColors, "errorTextColor", "errorText") ?: "#D93025")
        setRetryButtonColor(
          firstString(resultColors, "retryButtonColor", "retryButtonBackground") ?: "#0F9D58"
        )
        setRetryButtonText(
          firstString(resultTexts, "retryButtonText", "retryButton") ?: "Tentar novamente"
        )
        setRetryButtonTextColor(
          firstString(resultColors, "retryButtonTextColor", "retryButtonText") ?: "#FFFFFF"
        )
      }
    }
  }

  fun create(isCustom: Boolean, theme: ReadableMap? = null, context: Context? = null): FortfaceTheme {
    if (!isCustom) {
      val showInstructionScreen =
        optBoolean(
          theme?.getMap("instructions")?.getMap("configuration"),
          "showInstructionScreen",
          true
        )
      return buildDefault(showInstructionScreen)
    }
    ThemeColorValidator.validate(theme, "fortface")
    return buildCustom(theme, context)
  }

  private fun resolveConfiguredFonts(
    context: Context?,
    instructionsFonts: ReadableMap?,
    permissionFonts: ReadableMap?,
    fortfaceFonts: ReadableMap?,
    resultFonts: ReadableMap?,
    processingFonts: ReadableMap?
  ): Map<FortfaceFontsKey, Any> {
    if (
      instructionsFonts == null &&
      permissionFonts == null &&
      fortfaceFonts == null &&
      resultFonts == null &&
      processingFonts == null
    ) {
      return emptyMap()
    }
    val raw = FortfaceFonts(
      instructionsFonts,
      permissionFonts,
      fortfaceFonts,
      resultFonts,
      processingFonts
    ).apply()
    val cameraFontKeys = setOf(
      FortfaceFontsKey.SDK_CAMERA_MESSAGE_FONT,
      FortfaceFontsKey.SDK_CAMERA_FOOTER_FONT
    )
    return raw.mapNotNull { (key, path) ->
      val name = path
        .removePrefix("fonts/")
        .substringBeforeLast(".ttf")
        .substringBeforeLast(".otf")
        .trim()
      if (name.isEmpty() || name == "ubuntu_regular") {
        null
      } else if (key in cameraFontKeys) {
        val resId = FontResolver.resolveCameraFontResId(context, path)
        if (resId != null) {
          key to resId
        } else {
          key to path
        }
      } else {
        key to FontResolver.resolveFromAssetPath(context, path)
      }
    }.toMap()
  }

  private fun buildTextMap(texts: ReadableMap?): Map<FortfaceTextKey, String> {
    if (texts == null) return emptyMap()
    val map = linkedMapOf<FortfaceTextKey, String>()
    fun put(key: FortfaceTextKey, vararg aliases: String) {
      firstString(texts, *aliases)?.let { map[key] = it }
    }
    put(FortfaceTextKey.CAMERA_START_MESSAGE, "cameraStartMessage")
    put(FortfaceTextKey.CAMERA_FACE_NO_CENTER, "cameraFaceNoCenter")
    put(FortfaceTextKey.CAMERA_FACE_POSITIONED, "cameraFacePositioned")
    put(FortfaceTextKey.CAMERA_NO_FACE, "cameraNoFace")
    put(FortfaceTextKey.CAMERA_FACE_FAR, "cameraFaceFar")
    put(FortfaceTextKey.CAMERA_FACE_NEAR, "cameraFaceNear")
    put(FortfaceTextKey.CAMERA_FACE_CENTER_LEFT, "cameraFaceCenterLeft")
    put(FortfaceTextKey.CAMERA_FACE_CENTER_RIGHT, "cameraFaceCenterRight")
    put(FortfaceTextKey.CAMERA_FACE_CENTER_UP, "cameraFaceCenterUp")
    put(FortfaceTextKey.CAMERA_FACE_CENTER_DOWN, "cameraFaceCenterDown")
    put(FortfaceTextKey.CAMERA_FACE_PITCH_UP, "cameraFacePitchUp")
    put(FortfaceTextKey.CAMERA_FACE_PITCH_DOWN, "cameraFacePitchDown")
    put(FortfaceTextKey.CAMERA_NO_FACE_YAW, "cameraNoFaceYaw")
    put(FortfaceTextKey.CAMERA_NO_FACE_ROLL, "cameraNoFaceRoll")
    put(FortfaceTextKey.CAMERA_FACE_ROLL_LEFT, "cameraFaceRollLeft")
    put(FortfaceTextKey.CAMERA_FACE_ROLL_RIGHT, "cameraFaceRollRight")
    put(FortfaceTextKey.CAMERA_FACE_BRIGHTNESS_LOW, "cameraFaceBrightnessLow")
    put(FortfaceTextKey.CAMERA_FACE_BRIGHTNESS_HIGH, "cameraFaceBrightnessHigh")
    return map
  }
}
