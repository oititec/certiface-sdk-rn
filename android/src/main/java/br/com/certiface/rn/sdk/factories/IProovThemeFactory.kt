package br.com.certiface.rn.sdk.factories

import android.content.Context
import android.util.Log
import androidx.core.graphics.toColorInt
import br.com.certiface.designsystem.R
import br.com.certiface.domain.model.iproov.OrientationGPA
import br.com.certiface.domain.model.iproov.OrientationLA
import br.com.certiface.manager.exports.FilterTheme
import br.com.certiface.manager.exports.IProovDrawablesKey
import br.com.certiface.manager.exports.IProovFontsKey
import br.com.certiface.manager.exports.IProovTheme
import br.com.certiface.manager.exports.LineDrawingStyle
import br.com.certiface.manager.exports.NaturalStyle
import br.com.certiface.rn.sdk.theme.IProovFonts
import br.com.certiface.rn.sdk.theme.fontAssetPath
import br.com.certiface.rn.sdk.processors.AssetProcessor
import com.facebook.react.bridge.ReadableMap

object IProovThemeFactory {
  private const val TAG = "IProovThemeFactory"
  private val DEFAULT_IPROOV_FONT_RES = R.font.ubuntu_regular

  fun create(isCustom: Boolean, theme: ReadableMap? = null, context: Context? = null): IProovTheme =
    if (isCustom) buildCustom(theme, context) else buildDefault()

  private fun buildDefault() = IProovTheme.build {
    setIsEnabledScreenShots(true)
  }

  private fun buildCustom(theme: ReadableMap? = null, context: Context? = null) = IProovTheme.build {

    val iproovTheme = theme?.getMap("iproov")
    val colors = iproovTheme?.getMap("colors")
    val texts = iproovTheme?.getMap("texts")
    val instructionsTheme = theme?.getMap("instructions")
    val instructionsFontsMap = instructionsTheme?.getMap("fonts")
    val resultTheme = theme?.getMap("result")
    val resultFontsMap = resultTheme?.getMap("fonts")
    val iproovFontsMap = iproovTheme?.getMap("fonts")
    val iproovFontResource = iproovTheme?.getString("fontResource")
    val iproovFontPath = iproovTheme?.getString("fontPath")
    val resolvedIProovFontResource = resolveFontResource(context, iproovFontResource)

    val iProovFonts = resolveIProovFonts(
      context = context,
      iproovFontsMap = iproovFontsMap,
      instructionsFontsMap = instructionsFontsMap,
      resultFontsMap = resultFontsMap,
      resolvedFontResource = resolvedIProovFontResource,
      fontResource = iproovFontResource,
      fontPath = iproovFontPath
    )

    setTitle(firstString(texts, "title") ?: "Verificação Facial")
    setTitleColor(firstString(colors, "titleColor", "title") ?: "#FFFFFF")
    firstString(colors, "closeButtonColor", "closeButtonIcon")?.let { setCloseButtonColor(it) }
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
    setIsEnabledScreenShots(true)
    setDisableExteriorEffects(false)
    setTimeoutSecs(60)
    setPromptRoundedCorners(true)
    setFontsKey(iProovFonts)
    val filterForeground = firstString(colors, "filterLineDrawingForeground")
    val filterBackground = firstString(colors, "filterLineDrawingBackground")
    setFilter(
      if (filterForeground != null || filterBackground != null) {
        FilterTheme.LineDrawing(
          style = LineDrawingStyle.CLASSIC,
          background = filterBackground?.toColorInt(),
          foreground = filterForeground?.toColorInt()
        )
      } else {
        FilterTheme.Natural(NaturalStyle.CLEAR)
      }
    )

    setOrientation(
      gpa = OrientationGPA.PORTRAIT,
      la = OrientationLA.PORTRAIT
    )

    setOvalColors(
      ready = (firstString(colors, "ovalReadyColor", "ovalReady") ?: "#00FF00").toColorInt(),
      notReady = (firstString(colors, "ovalNotReadyColor", "ovalNotReady") ?: "#FF0000").toColorInt(),
      stroke = (firstString(colors, "ovalStrokeColor", "ovalCapturing") ?: "#FFFFFF").toColorInt(),
      completed = (firstString(colors, "ovalCompletedColor", "ovalCompleted") ?: "#00FF00").toColorInt()
    )

    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")
    val instructionsConfiguration = instructionsTheme?.getMap("configuration")
    val instructionsFlags = instructionsTheme?.getMap("flags")
    val showInstructionScreen = instructionsConfiguration?.getBoolean("showInstructionScreen") ?: true
    val instructionStatusBarDarkIcons = instructionsFlags?.getBoolean("statusBarIsDarkIcons") ?: false

    val iproovAssets = iproovTheme?.getMap("assets")
    val iproovDrawablesRaw = AssetProcessor.processIProovAssets(theme)

    resolveDrawableResourceId(context, iproovAssets?.getString("logoImage"))?.let { setLogo(it) }
    resolveDrawableResourceId(context, iproovAssets?.getString("closeButtonIcon"))?.let { setCloseButton(it) }

    val iproovDrawables = resolveIProovDrawables(context, iproovDrawablesRaw)

    if (iproovDrawables.isNotEmpty()) {
      setDrawablesKey(iproovDrawables)
    } else {
      Log.d(TAG, "Nenhum drawable customizado encontrado, usando padrões")
    }

    setInstructionsTheme {
      setShowInstructionScreen(showInstructionScreen)
      setTitleText(
        firstString(instructionsTexts, "title", "titleText")
          ?: firstString(texts, "instructionsTitleText")
          ?: "Teste title"
      )
      setTitleColor(firstString(instructionsColors, "titleColor", "title") ?: "#FFFFFF")
      setCaptionText(
        firstString(instructionsTexts, "caption", "captionText")
          ?: firstString(texts, "instructionsCaptionText")
          ?: "teste caption."
      )
      setCaptionColor(firstString(instructionsColors, "captionColor", "caption") ?: "#AAAAAA")
      setBackgroundColor(firstString(instructionsColors, "backgroundColor", "background") ?: "#1F1F1F")
      setStatusBarColor(firstString(instructionsColors, "statusBarColor", "statusBar") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(instructionStatusBarDarkIcons)
      setBottomSheetColor(firstString(instructionsColors, "bottomSheetColor", "bottomSheet") ?: "#333333")
      setBottomSheetCornerRadius(16f)
      setContinueButtonText(
        instructionsTexts?.getString("continueButton")
          ?: instructionsTexts?.getString("continueButtonText")
          ?: texts?.getString("continueButton")
          ?: texts?.getString("continueButtonText")
          ?: "Startar"
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

      val backButtonDrawableId = iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG]
      backButtonDrawableId?.let { setBackButtonImg(it) }
      resolveInstructionsBackButtonTintColor(instructionsColors, hasCustomBackButtonImage = backButtonDrawableId != null)
        ?.let { setBackButtonColor(it) }

      iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE]?.let { setContextImage(it) }
      iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON]?.let { setFirstInstructionIcon(it) }
      iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON]?.let { setSecondInstructionIcon(it) }
    }

    val permissionTheme = theme?.getMap("permission")
    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")

    setPermissionTheme {
      setTitle(firstString(permissionTexts, "title") ?: firstString(texts, "permissionTitle") ?: "Permissões Necessárias")
      setTitleColor(firstString(permissionColors, "titleColor", "title") ?: "#FFFFFF")
      setSubTitle(firstString(permissionTexts, "caption") ?: firstString(texts, "permissionCaption"))
      setSubTitleColor(firstString(permissionColors, "captionColor", "caption") ?: "#FFFFFF")
      setBackgroundColor(firstString(permissionColors, "backgroundColor", "background") ?: "#1F1F1F")
      setStatusBarColor(firstString(permissionColors, "statusBarColor", "statusBar") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(false)
      setPermissionButtonText(
        firstString(permissionTexts, "checkPermissionButton") ?: "Permitir acesso"
      )
      setPermissionButtonColor(
        firstString(permissionColors, "checkPermissionButtonBackground", "checkPermissionButtonColor") ?: "#00FF00"
      )
      setPermissionButtonTextColor(
        firstString(permissionColors, "checkPermissionButtonText") ?: "#000000"
      )
      iproovDrawables[IProovDrawablesKey.PERMISSION_CAMERA_ICON]?.let { setCameraIcon(it) }
      iproovDrawables[IProovDrawablesKey.PERMISSION_BACK_BUTTON_ICON]?.let { setBackButtonIcon(it) }
    }

    val processingTheme = theme?.getMap("processing")
    val processingColors = processingTheme?.getMap("colors")

    setProcessingTheme {
      setBackgroundColor(firstString(processingColors, "backgroundColor", "background") ?: "#000000")
      setLoadingDialogColor(firstString(processingColors, "loadingDialogColor", "loading") ?: "#FFFFFF")
      setStatusBarColor(firstString(processingColors, "statusBarColor", "statusBar") ?: "#000000")
      setStatusBarIsDarkIcons(true)
      setLoadingIndicatorSize(100)
      setLoadingIndicatorWidth(10)
    }

    val resultColors = resultTheme?.getMap("colors")
    val resultTexts = resultTheme?.getMap("texts")

    val successIconId = iproovDrawables[IProovDrawablesKey.RESULT_SUCCESS_ICON] ?: R.drawable.success_icon
    val errorIconId = iproovDrawables[IProovDrawablesKey.RESULT_ERROR_ICON] ?: R.drawable.error_icon

    setResultTheme {
      setSuccessBackgroundColor(firstString(resultColors, "successBackgroundColor", "successBackground") ?: "#DFFFD6")
      setSuccessIcon(successIconId)
      setSuccessText(firstString(resultTexts, "successText", "success") ?: firstString(texts, "successText") ?: "Verificação concluída com sucesso!")
      setSuccessTextColor(firstString(resultColors, "successTextColor", "successText") ?: "#0F9D58")

      setStatusBarSuccessColor(firstString(resultColors, "statusBarSuccessColor", "successStatusBar") ?: "#DFFFD6")
      setStatusBarErrorColor(firstString(resultColors, "statusBarErrorColor", "errorStatusBar") ?: "#FFD6D6")
      setStatusBarSuccessIsDarkIcons(true)
      setStatusBarErrorIsDarkIcons(true)

      setErrorBackgroundColor(firstString(resultColors, "errorBackgroundColor", "errorBackground") ?: "#FFD6D6")
      setErrorIcon(errorIconId)
      setErrorText(firstString(resultTexts, "errorText", "error") ?: firstString(texts, "errorText") ?: "Algo deu errado na verificação.")
      setErrorTextColor(firstString(resultColors, "errorTextColor", "errorText") ?: "#D93025")

      setRetryButtonColor(firstString(resultColors, "retryButtonColor", "retryButtonBackground", "retryBackground") ?: "#0F9D58")
      setRetryButtonText(firstString(resultTexts, "retryButtonText", "retryButton") ?: firstString(texts, "retryButtonText") ?: "Tentar novamente")
      setRetryButtonTextColor(firstString(resultColors, "retryButtonTextColor", "retryButtonText", "retryText") ?: "#FFFFFF")
    }
  }

  private fun resolveIProovFonts(
    context: Context?,
    iproovFontsMap: ReadableMap?,
    instructionsFontsMap: ReadableMap?,
    resultFontsMap: ReadableMap?,
    resolvedFontResource: Int,
    fontResource: String?,
    fontPath: String?
  ): Map<IProovFontsKey, Any> {
    val fallback = resolveDefaultIProovFontValue(fontResource, fontPath, resolvedFontResource)
    val baseValue: Any = when {
      resolvedFontResource != DEFAULT_IPROOV_FONT_RES -> resolvedFontResource
      context != null && fallback is String && fontAssetExists(context, fallback) -> fallback
      else -> resolvedFontResource
    }

    if (iproovFontsMap == null && instructionsFontsMap == null && resultFontsMap == null) {
      return buildDefaultIProovFonts(baseValue)
    }

    val configuredFonts = IProovFonts(iproovFontsMap, instructionsFontsMap).apply().toMutableMap()
    resultFontsMap?.getString("text")?.trim()?.takeIf { it.isNotEmpty() }?.let {
      configuredFonts[IProovFontsKey.RESULT_MESSAGE_FONT] = fontAssetPath(it)
    }
    resultFontsMap?.getString("retryButton")?.trim()?.takeIf { it.isNotEmpty() }?.let {
      configuredFonts[IProovFontsKey.RESULT_RETRY_BUTTON_FONT] = fontAssetPath(it)
    }
    if (context == null) {
      return configuredFonts
    }

    return configuredFonts.mapValues { (_, fontPathValue) ->
      when (baseValue) {
        is Int -> baseValue
        is String -> {
          if (fontAssetExists(context, fontPathValue)) fontPathValue else baseValue
        }
        else -> baseValue
      }
    }
  }

  private fun resolveDrawableResourceId(context: Context?, value: Any?): Int? =
    AssetProcessor.resolveDrawableResourceId(context, value)

  private fun resolveIProovDrawables(
    context: Context?,
    drawables: Map<IProovDrawablesKey, Any>
  ): Map<IProovDrawablesKey, Int> {
    if (context == null || drawables.isEmpty()) return emptyMap()

    val reservedForDirectSetters = setOf(
      IProovDrawablesKey.IPROOV_LOGO,
      IProovDrawablesKey.IPROOV_CLOSE_BUTTON
    )

    return drawables.mapNotNull { (key, value) ->
      if (key in reservedForDirectSetters) return@mapNotNull null
      val resourceId = resolveDrawableResourceId(context, value)
      if (resourceId == null) {
        if (value is String) {
          Log.w(TAG, "Drawable '$value' ignorado: recurso não encontrado")
        }
        return@mapNotNull null
      }
      key to resourceId
    }.toMap()
  }

  private fun fontAssetExists(context: Context, assetPath: String): Boolean {
    return try {
      context.assets.open(assetPath).close()
      true
    } catch (_: Exception) {
      false
    }
  }

  private fun resolveFontResource(context: Context?, fontResource: String?): Int {
    if (context == null) return DEFAULT_IPROOV_FONT_RES
    val raw = fontResource?.trim().orEmpty()
    if (raw.isEmpty()) return DEFAULT_IPROOV_FONT_RES

    val normalized = raw
      .substringAfterLast('/')
      .substringBeforeLast(".ttf")
      .substringBeforeLast(".otf")

    val packages = listOf(
      context.packageName,
      "br.com.certiface.rn.sdk",
      "br.com.certiface.designsystem"
    )
    for (pkg in packages) {
      val resourceId = context.resources.getIdentifier(normalized, "font", pkg)
      if (resourceId != 0) return resourceId
    }
    return DEFAULT_IPROOV_FONT_RES
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
}
