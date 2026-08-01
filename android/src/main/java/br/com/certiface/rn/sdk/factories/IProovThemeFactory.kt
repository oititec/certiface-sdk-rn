package br.com.certiface.rn.sdk.factories

import android.content.Context
import android.util.Log
import br.com.certiface.designsystem.R
import br.com.certiface.designsystem.ui.builders.InstructionImageScale
import br.com.certiface.domain.model.iproov.OrientationGPA
import br.com.certiface.domain.model.iproov.OrientationLA
import br.com.certiface.domain.model.iproov.DomainFilterTheme
import br.com.certiface.manager.exports.FilterTheme
import br.com.certiface.manager.exports.IProovDrawablesKey
import br.com.certiface.manager.exports.IProovFontsKey
import br.com.certiface.manager.exports.IProovTheme
import br.com.certiface.manager.exports.LineDrawingStyle
import br.com.certiface.manager.exports.NaturalStyle
import br.com.certiface.rn.sdk.exceptions.CustomThemeException
import br.com.certiface.rn.sdk.theme.IProovFonts
import br.com.certiface.rn.sdk.theme.FontResolver
import br.com.certiface.rn.sdk.theme.fontAssetPath
import br.com.certiface.rn.sdk.processors.AssetProcessor
import com.facebook.react.bridge.ReadableMap

object IProovThemeFactory {
  private const val TAG = "IProovThemeFactory"
  private val DEFAULT_IPROOV_FONT_RES = FontResolver.defaultFontRes

  fun create(isCustom: Boolean, theme: ReadableMap? = null, context: Context? = null): IProovTheme =
    if (isCustom) {
      ThemeColorValidator.validate(theme, "iproov")
      buildCustom(theme, context)
    } else {
      buildDefault()
    }

  private fun buildDefault() = IProovTheme.build {
    setIsEnabledScreenShots(true)
  }

  private fun buildCustom(theme: ReadableMap? = null, context: Context? = null) = IProovTheme.build {

    val iproovTheme = theme?.getMap("iproov")
    val colors = iproovTheme?.getMap("colors")
    val texts = iproovTheme?.getMap("texts")
    val iproovAssets = iproovTheme?.getMap("assets")
    val iproovConfiguration = iproovTheme?.getMap("configuration")
    val iproovFlags = iproovTheme?.getMap("flags")
    val instructionsTheme = theme?.getMap("instructions")
    val instructionsFontsMap = instructionsTheme?.getMap("fonts")
    val instructionsSizes = instructionsTheme?.getMap("sizes")
    val permissionTheme = theme?.getMap("permission")
    val permissionFontsMap = permissionTheme?.getMap("fonts")
    val resultTheme = theme?.getMap("result")
    val resultFontsMap = resultTheme?.getMap("fonts")
    val processingTheme = theme?.getMap("processing")
    val processingFontsMap = processingTheme?.getMap("fonts")
    val iproovFontsMap = iproovTheme?.getMap("fonts")
    val iproovFontResource = iproovTheme?.getString("fontResource")
    val iproovFontPath = iproovTheme?.getString("fontPath")
    val resolvedIProovFontResource = resolveFontResource(context, iproovFontResource)

    val iProovFonts = resolveIProovFonts(
      context = context,
      iproovFontsMap = iproovFontsMap,
      instructionsFontsMap = instructionsFontsMap,
      permissionFontsMap = permissionFontsMap,
      resultFontsMap = resultFontsMap,
      processingFontsMap = processingFontsMap,
      resolvedFontResource = resolvedIProovFontResource,
      fontResource = iproovFontResource,
      fontPath = iproovFontPath
    )

    setTitle(firstString(texts, "title") ?: "Verificação Facial")
    setTitleColor(firstString(colors, "titleColor", "title") ?: "#FFFFFF")
    IProovCloseButtonApplier.apply(
      context = context,
      iproovAssets = iproovAssets,
      colors = colors,
      setCloseButton = { setCloseButton(it) },
      setCloseButtonColor = { setCloseButtonColor(it) }
    )
    setHeaderBackgroundColor(firstString(colors, "headerBackgroundColor", "titleBackground") ?: "#121212")
    setPromptTextColor(firstString(colors, "promptTextColor", "promptText") ?: "#FFFFFF")
    setPromptBackgroundColor(firstString(colors, "promptBackgroundColor", "promptBackground") ?: "#1F1F1F")
    setSurroundColor(firstString(colors, "surroundColor", "background") ?: "#00FF00")
    setFontResource(resolvedIProovFontResource)
    if (resolvedIProovFontResource == DEFAULT_IPROOV_FONT_RES) {
      applyIProovBaseFont(
        target = this,
        fontResource = iproovFontResource,
        fontPath = iproovFontPath,
        resolvedFontResource = resolvedIProovFontResource
      )
    }
    setIsEnabledScreenShots(optBoolean(iproovFlags, "isEnabledScreenShots", true))
    setDisableExteriorEffects(optBoolean(iproovFlags, "disableExteriorEffects", false))
    setTimeoutSecs(optInt(iproovConfiguration, "timeoutSecs", 60))
    setPromptRoundedCorners(optBoolean(iproovFlags, "promptRoundedCorners", true))
    if (iProovFonts.isNotEmpty()) {
      setFontsKey(iProovFonts)
    }
    val filterForeground = firstString(colors, "filterLineDrawingForeground")
    val filterBackground = firstString(colors, "filterLineDrawingBackground")
    setFilter(resolveIProovFilter(iproovConfiguration, filterForeground, filterBackground))

    setOrientation(
      gpa = parseOrientationGpa(iproovConfiguration, "orientationGpa", OrientationGPA.PORTRAIT),
      la = parseOrientationLa(iproovConfiguration, "orientationLa", OrientationLA.PORTRAIT)
    )

    setOvalColors(
      ready = ThemeColorValidator.parseColorOrThrow(
        firstString(colors, "ovalReadyColor", "ovalReady") ?: "#00FF00",
        "ovalReady"
      ),
      notReady = ThemeColorValidator.parseColorOrThrow(
        firstString(colors, "ovalNotReadyColor", "ovalNotReady") ?: "#FF0000",
        "ovalNotReady"
      ),
      stroke = ThemeColorValidator.parseColorOrThrow(
        firstString(colors, "ovalStrokeColor", "ovalCapturing") ?: "#FFFFFF",
        "ovalCapturing"
      ),
      completed = ThemeColorValidator.parseColorOrThrow(
        firstString(colors, "ovalCompletedColor", "ovalCompleted") ?: "#00FF00",
        "ovalCompleted"
      )
    )

    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")
    val instructionsConfiguration = instructionsTheme?.getMap("configuration")
    val instructionsFlags = instructionsTheme?.getMap("flags")
    val showInstructionScreen = instructionsConfiguration?.getBoolean("showInstructionScreen") ?: true
    val instructionStatusBarDarkIcons = instructionsFlags?.getBoolean("statusBarIsDarkIcons") ?: false

    val iproovDrawablesRaw = AssetProcessor.processIProovAssets(theme)
    val iproovDrawables = resolveIProovDrawables(context, iproovDrawablesRaw)

    resolveRequiredDrawable(
      context = context,
      value = iproovAssets?.getString("logoImage"),
      invalidParam = IProovDrawablesKey.IPROOV_LOGO.name
    )?.let { setLogo(it) }

    if (iproovDrawables.isNotEmpty()) {
      setDrawablesKey(iproovDrawables)
    } else {
      Log.d(TAG, "Nenhum drawable customizado encontrado, usando padrões")
    }

    fun drawableRes(key: IProovDrawablesKey): Int? = iproovDrawables[key] as? Int

    setInstructionsTheme {
      setShowInstructionScreen(showInstructionScreen)
      setTitleText(
        firstString(instructionsTexts, "title", "titleText")
          ?: firstString(texts, "instructionsTitleText")
          ?: "Centralize seu rosto"
      )
      setTitleColor(firstString(instructionsColors, "titleColor", "title") ?: "#FFFFFF")
      setCaptionText(
        firstString(instructionsTexts, "caption", "captionText")
          ?: firstString(texts, "instructionsCaptionText")
          ?: "Mantenha-se dentro do círculo"
      )
      setCaptionColor(firstString(instructionsColors, "captionColor", "caption") ?: "#AAAAAA")
      setBackgroundColor(firstString(instructionsColors, "backgroundColor", "background") ?: "#1F1F1F")
      setStatusBarColor(firstString(instructionsColors, "statusBarColor", "statusBar") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(instructionStatusBarDarkIcons)
      setBottomSheetColor(firstString(instructionsColors, "bottomSheetColor", "bottomSheet") ?: "#333333")
      setBottomSheetCornerRadius(optFloat(instructionsSizes, "bottomSheetCornerRadius", 16f))
      setContinueButtonText(
        instructionsTexts?.getString("continueButton")
          ?: instructionsTexts?.getString("continueButtonText")
          ?: texts?.getString("continueButton")
          ?: texts?.getString("continueButtonText")
          ?: "Começar"
      )
      setContinueButtonColor(
        firstString(instructionsColors, "continueButtonColor", "continueButtonBackground") ?: "#00FF00"
      )
      setContinueButtonTextColor(
        firstString(instructionsColors, "continueButtonTextColor", "continueButtonText")
          ?: "#000000"
      )
      firstString(instructionsTexts, "firstInstruction")?.let { setFirstInstructionText(it) }
      firstString(instructionsTexts, "secondInstruction")?.let { setSecondInstructionText(it) }
      firstString(instructionsColors, "firstInstructionTitle")?.let { setFirstInstructionTextColor(it) }
      firstString(instructionsColors, "secondInstructionTitle")?.let { setSecondInstructionTextColor(it) }
      firstString(instructionsColors, "firstInstructionIconBackground")?.let { setFirstInstructionIconBackgroundColor(it) }
      firstString(instructionsColors, "firstInstructionIconBorder")?.let { setFirstInstructionIconBorderColor(it) }
      firstString(instructionsColors, "secondInstructionIconBackground")?.let { setSecondInstructionIconBackgroundColor(it) }
      firstString(instructionsColors, "secondInstructionIconBorder")?.let { setSecondInstructionIconBorderColor(it) }

      val hasCustomBackButton = iproovDrawables.containsKey(IProovDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG)
      drawableRes(IProovDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG)?.let { setBackButtonImg(it) }
      resolveInstructionsBackButtonTintColor(instructionsColors, hasCustomBackButtonImage = hasCustomBackButton)
        ?.let { setBackButtonColor(it) }

      drawableRes(IProovDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE)?.let { setContextImage(it) }
      drawableRes(IProovDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON)?.let { setFirstInstructionIcon(it) }
      drawableRes(IProovDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON)?.let { setSecondInstructionIcon(it) }
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

    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")
    val permissionFlags = permissionTheme?.getMap("flags")

    setPermissionTheme {
      setTitle(firstString(permissionTexts, "title") ?: firstString(texts, "permissionTitle") ?: "Permissões Necessárias")
      setTitleColor(firstString(permissionColors, "titleColor", "title") ?: "#FFFFFF")
      setSubTitle(firstString(permissionTexts, "caption") ?: firstString(texts, "permissionCaption"))
      setSubTitleColor(firstString(permissionColors, "captionColor", "caption") ?: "#FFFFFF")
      setBackgroundColor(firstString(permissionColors, "backgroundColor", "background") ?: "#1F1F1F")
      setStatusBarColor(firstString(permissionColors, "statusBarColor", "statusBar") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(optBoolean(permissionFlags, "statusBarIsDarkIcons", false))
      setPermissionButtonText(
        firstString(permissionTexts, "checkPermissionButton") ?: "Permitir acesso"
      )
      setPermissionButtonColor(
        firstString(permissionColors, "checkPermissionButtonBackground", "checkPermissionButtonColor") ?: "#00FF00"
      )
      setPermissionButtonTextColor(
        firstString(permissionColors, "checkPermissionButtonText") ?: "#000000"
      )
      drawableRes(IProovDrawablesKey.PERMISSION_CAMERA_ICON)?.let { setCameraIcon(it) }
      drawableRes(IProovDrawablesKey.PERMISSION_BACK_BUTTON_ICON)?.let { setBackButtonIcon(it) }
    }

    val processingColors = processingTheme?.getMap("colors")
    val processingFlags = processingTheme?.getMap("flags")
    val processingSizes = processingTheme?.getMap("sizes")
    val processingTexts = processingTheme?.getMap("texts")
    val iproovTexts = iproovTheme?.getMap("texts")

    setProcessingTheme {
      setBackgroundColor(firstString(processingColors, "backgroundColor", "background") ?: "#000000")
      setLoadingDialogColor(firstString(processingColors, "loadingDialogColor", "loading") ?: "#FFFFFF")
      setStatusBarColor(firstString(processingColors, "statusBarColor", "statusBar") ?: "#000000")
      setStatusBarIsDarkIcons(optBoolean(processingFlags, "statusBarIsDarkIcons", true))
      setLoadingIndicatorSize(optInt(processingSizes, "loadingIndicatorSize", 100))
      setLoadingIndicatorWidth(optInt(processingSizes, "loadingIndicatorWidth", 10))
      (
        firstString(processingTexts, "message")
          ?: firstString(iproovTexts, "processingMessage")
      )?.let { setProcessingMessage(it) }
    }

    val resultColors = resultTheme?.getMap("colors")
    val resultTexts = resultTheme?.getMap("texts")
    val resultFlags = resultTheme?.getMap("flags")

    val successIconId = drawableRes(IProovDrawablesKey.RESULT_SUCCESS_ICON) ?: R.drawable.success_icon
    val errorIconId = drawableRes(IProovDrawablesKey.RESULT_ERROR_ICON) ?: R.drawable.error_icon

    setResultTheme {
      setSuccessBackgroundColor(firstString(resultColors, "successBackgroundColor", "successBackground") ?: "#DFFFD6")
      setSuccessIcon(successIconId)
      setSuccessText(firstString(resultTexts, "successText", "success") ?: firstString(texts, "successText") ?: "Verificação concluída com sucesso!")
      setSuccessTextColor(firstString(resultColors, "successTextColor", "successText") ?: "#0F9D58")

      setStatusBarSuccessColor(firstString(resultColors, "statusBarSuccessColor", "successStatusBar") ?: "#DFFFD6")
      setStatusBarErrorColor(firstString(resultColors, "statusBarErrorColor", "errorStatusBar") ?: "#FFD6D6")
      setStatusBarSuccessIsDarkIcons(optBoolean(resultFlags, "successStatusBarIsDarkIcons", true))
      setStatusBarErrorIsDarkIcons(optBoolean(resultFlags, "errorStatusBarIsDarkIcons", true))

      setErrorBackgroundColor(firstString(resultColors, "errorBackgroundColor", "errorBackground") ?: "#FFD6D6")
      setErrorIcon(errorIconId)
      setErrorText(firstString(resultTexts, "errorText", "error") ?: firstString(texts, "errorText") ?: "Algo deu errado na verificação.")
      setErrorTextColor(firstString(resultColors, "errorTextColor", "errorText") ?: "#D93025")

      setRetryButtonColor(firstString(resultColors, "retryButtonColor", "retryButtonBackground") ?: "#0F9D58")
      setRetryButtonText(firstString(resultTexts, "retryButtonText", "retryButton") ?: firstString(texts, "retryButtonText") ?: "Tentar novamente")
      setRetryButtonTextColor(firstString(resultColors, "retryButtonTextColor", "retryButtonText") ?: "#FFFFFF")
    }
  }

  private fun resolveIProovFonts(
    context: Context?,
    iproovFontsMap: ReadableMap?,
    instructionsFontsMap: ReadableMap?,
    permissionFontsMap: ReadableMap?,
    resultFontsMap: ReadableMap?,
    processingFontsMap: ReadableMap?,
    resolvedFontResource: Int,
    fontResource: String?,
    fontPath: String?
  ): Map<IProovFontsKey, Any> {
    val fallback = resolveDefaultIProovFontValue(fontResource, fontPath, resolvedFontResource)
    val baseValue: Any = when {
      resolvedFontResource != DEFAULT_IPROOV_FONT_RES -> resolvedFontResource
      context != null && fallback is String && FontResolver.fontAssetExists(context, fallback) -> fallback
      fallback is String && !fallback.contains("ubuntu_regular") -> fallback
      else -> resolvedFontResource
    }

    if (
      iproovFontsMap == null &&
      instructionsFontsMap == null &&
      permissionFontsMap == null &&
      resultFontsMap == null &&
      processingFontsMap == null
    ) {
      return buildDefaultIProovFonts(baseValue)
    }

    val configuredFonts = IProovFonts(iproovFontsMap, instructionsFontsMap, permissionFontsMap).apply().toMutableMap()
    resultFontsMap?.getString("text")?.trim()?.takeIf { it.isNotEmpty() }?.let {
      configuredFonts[IProovFontsKey.RESULT_MESSAGE_FONT] = fontAssetPath(it)
    }
    resultFontsMap?.getString("retryButton")?.trim()?.takeIf { it.isNotEmpty() }?.let {
      configuredFonts[IProovFontsKey.RESULT_RETRY_BUTTON_FONT] = fontAssetPath(it)
    }
    processingFontsMap?.getString("message")?.trim()?.takeIf { it.isNotEmpty() }?.let {
      configuredFonts[IProovFontsKey.PROCESSING_MESSAGE_FONT] = fontAssetPath(it)
    }

    val filteredFonts = configuredFonts.mapNotNull { (key, path) ->
      val name = path
        .removePrefix("fonts/")
        .substringBeforeLast(".ttf")
        .substringBeforeLast(".otf")
        .trim()
      if (name.isEmpty() || name == "ubuntu_regular") {
        null
      } else {
        key to path
      }
    }.toMap()

    if (context == null) {
      return filteredFonts
    }

    return filteredFonts.mapValues { (_, fontPathValue) ->
      FontResolver.resolveFromAssetPath(context, fontPathValue)
    }
  }

  private fun resolveDrawableResourceId(context: Context?, value: Any?): Int? =
    AssetProcessor.resolveDrawableResourceId(context, value)

  private fun resolveRequiredDrawable(
    context: Context?,
    value: String?,
    invalidParam: String
  ): Int? {
    val name = value?.trim().orEmpty()
    if (name.isEmpty()) return null
    return resolveDrawableResourceId(context, name)
      ?: throw CustomThemeException(invalidParam, "drawable")
  }

  private fun resolveIProovDrawables(
    context: Context?,
    drawables: Map<IProovDrawablesKey, Any>
  ): Map<IProovDrawablesKey, Any> {
    if (context == null || drawables.isEmpty()) return emptyMap()

    val reservedForDirectSetters = setOf(
      IProovDrawablesKey.IPROOV_LOGO,
      IProovDrawablesKey.IPROOV_CLOSE_BUTTON
    )

    return drawables.mapNotNull { (key, value) ->
      if (key in reservedForDirectSetters) return@mapNotNull null
      val resourceId = resolveDrawableResourceId(context, value)
      key to (resourceId ?: value)
    }.toMap()
  }

  private fun resolveFontResource(context: Context?, fontResource: String?): Int {
    if (context == null) return DEFAULT_IPROOV_FONT_RES
    return FontResolver.resolveFontResourceId(context, fontResource)
  }

  private fun applyFontPathIfSupported(target: Any, fontPath: String?): Boolean {
    val path = fontPath?.trim().orEmpty()
    if (path.isEmpty()) return false
    return try {
      val method = target.javaClass.methods.firstOrNull { method ->
        method.name == "setFontPath" &&
          method.parameterTypes.size == 1 &&
          method.parameterTypes[0] == String::class.java
      } ?: return false
      method.invoke(target, path)
      true
    } catch (_: Throwable) {
      false
    }
  }

  private fun applyIProovBaseFont(
    target: Any,
    fontResource: String?,
    fontPath: String?,
    resolvedFontResource: Int
  ) {
    val explicitPath = fontPath?.trim().orEmpty()
    if (explicitPath.isNotEmpty()) {
      applyFontPathIfSupported(target, explicitPath)
    }

    val resourceName = fontResource?.trim().orEmpty()
    if (resourceName.isNotEmpty() && resolvedFontResource == DEFAULT_IPROOV_FONT_RES) {
      val normalized = resourceName
        .substringAfterLast('/')
        .substringBeforeLast(".ttf")
        .substringBeforeLast(".otf")
      val pathCandidates = listOf(
        "fonts/$normalized.ttf",
        "fonts/$normalized.otf",
        "$normalized.ttf",
        "$normalized.otf"
      )
      for (candidate in pathCandidates) {
        if (applyFontPathIfSupported(target, candidate)) return
      }
    }
  }

  private fun buildDefaultIProovFonts(baseFontValue: Any): Map<IProovFontsKey, Any> {
    return mapOf(
      IProovFontsKey.INSTRUCTIONS_TITLE_FONT to baseFontValue,
      IProovFontsKey.INSTRUCTIONS_CAPTION_FONT to baseFontValue,
      IProovFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to baseFontValue,
      IProovFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to baseFontValue,
      IProovFontsKey.INSTRUCTIONS_BUTTON_FONT to baseFontValue,
      IProovFontsKey.PERMISSION_TITLE_FONT to baseFontValue,
      IProovFontsKey.PERMISSION_CAPTION_FONT to baseFontValue,
      IProovFontsKey.PERMISSION_BUTTON_FONT to baseFontValue,
      IProovFontsKey.RESULT_MESSAGE_FONT to baseFontValue,
      IProovFontsKey.RESULT_RETRY_BUTTON_FONT to baseFontValue,
      IProovFontsKey.PROCESSING_MESSAGE_FONT to baseFontValue,
    )
  }

  private fun resolveDefaultIProovFontValue(
    fontResource: String?,
    fontPath: String?,
    resolvedFontResource: Int
  ): Any {
    val explicitPath = fontPath?.trim().orEmpty()
    if (explicitPath.isNotEmpty()) return explicitPath

    val resourceName = fontResource?.trim().orEmpty()
    if (resourceName.isNotEmpty() && resolvedFontResource == DEFAULT_IPROOV_FONT_RES) {
      val normalized = resourceName
        .substringAfterLast('/')
        .substringBeforeLast(".ttf")
        .substringBeforeLast(".otf")
      return "fonts/$normalized.ttf"
    }

    return resolvedFontResource
  }

  private fun resolveIProovFilter(
    configuration: ReadableMap?,
    filterForeground: String?,
    filterBackground: String?
  ): DomainFilterTheme {
    val explicitFilterStyle = firstString(configuration, "filterStyle")
      ?.lowercase()
      ?.replace(" ", "")
      ?.replace("_", "")
    val useLineDrawing = when (explicitFilterStyle) {
      "linedrawing" -> true
      "natural" -> false
      else -> filterForeground != null || filterBackground != null
    }

    if (useLineDrawing) {
      return FilterTheme.LineDrawing(
        style = parseLineDrawingStyle(configuration),
        background = filterBackground?.let {
          ThemeColorValidator.parseColorOrThrow(it, "filterLineDrawingBackground")
        },
        foreground = filterForeground?.let {
          ThemeColorValidator.parseColorOrThrow(it, "filterLineDrawingForeground")
        }
      )
    }

    return FilterTheme.Natural(parseNaturalStyle(configuration))
  }

  private fun parseNaturalStyle(configuration: ReadableMap?): NaturalStyle {
    return when (
      firstString(configuration, "naturalStyle")
        ?.lowercase()
        ?.replace(" ", "")
        ?.replace("_", "")
    ) {
      "blur" -> NaturalStyle.BLUR
      else -> NaturalStyle.CLEAR
    }
  }

  private fun parseLineDrawingStyle(configuration: ReadableMap?): LineDrawingStyle {
    return when (
      firstString(configuration, "lineDrawingStyle")
        ?.lowercase()
        ?.replace(" ", "")
        ?.replace("_", "")
    ) {
      "shaded" -> LineDrawingStyle.SHADED
      "vibrant" -> LineDrawingStyle.VIBRANT
      else -> LineDrawingStyle.CLASSIC
    }
  }
}
