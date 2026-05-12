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
import br.com.certiface.manager.exports.NaturalStyle
import br.com.certiface.rn.sdk.theme.IProovFonts
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
    val iproovFontsMap = iproovTheme?.getMap("fonts")
    val iproovFontResource = iproovTheme?.getString("fontResource")
    val iproovFontPath = iproovTheme?.getString("fontPath")
    val resolvedIProovFontResource = resolveFontResource(context, iproovFontResource)

    val defaultIProovFontValue = resolveDefaultIProovFontValue(
      fontResource = iproovFontResource,
      fontPath = iproovFontPath,
      resolvedFontResource = resolvedIProovFontResource
    )
    val iProovFonts = if (iproovFontsMap != null) {
      IProovFonts(iproovFontsMap).apply()
    } else {
      buildDefaultIProovFonts(defaultIProovFontValue)
    }

    setTitle(firstString(texts, "title") ?: "Verificação Facial")
    setTitleColor(firstString(colors, "titleColor", "title") ?: "#FFFFFF")
    firstString(colors, "closeButtonColor", "closeButtonIcon")?.let { setCloseButtonColor(it) }
    setHeaderBackgroundColor(firstString(colors, "headerBackgroundColor", "titleBackground") ?: "#121212")
    setPromptTextColor(firstString(colors, "promptTextColor", "promptText") ?: "#FFFFFF")
    setPromptBackgroundColor(firstString(colors, "promptBackgroundColor", "promptBackground") ?: "#1F1F1F")
    setSurroundColor(firstString(colors, "surroundColor", "background") ?: "#00FF00")
    setFontResource(resolvedIProovFontResource)
    applyIProovBaseFont(
      target = this,
      fontResource = iproovFontResource,
      fontPath = iproovFontPath,
      resolvedFontResource = resolvedIProovFontResource
    )
    setIsEnabledScreenShots(true)
    setDisableExteriorEffects(false)
    setTimeoutSecs(60)
    setPromptRoundedCorners(true)
    setFontsKey(iProovFonts)
    setFilter(FilterTheme.Natural(NaturalStyle.CLEAR))

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

    val instructionsTheme = theme?.getMap("instructions")
    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")
    val instructionsConfiguration = instructionsTheme?.getMap("configuration")
    val instructionsFlags = instructionsTheme?.getMap("flags")
    val showInstructionScreen = instructionsConfiguration?.getBoolean("showInstructionScreen") ?: true
    val instructionStatusBarDarkIcons = instructionsFlags?.getBoolean("statusBarIsDarkIcons") ?: false

    val iproovDrawablesRaw = AssetProcessor.processIProovAssets(theme)
    val iproovDrawables = if (context != null && iproovDrawablesRaw.isNotEmpty()) {
      iproovDrawablesRaw.mapValues { (_, value) ->
        when (value) {
          is String -> {
            val id = AssetProcessor.getDrawableResourceId(context, value)
            if (id != 0) id else value
          }
          else -> value
        }
      }
    } else {
      iproovDrawablesRaw
    }

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

      when (val contextImageRes = iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE]) {
        is Int -> if (contextImageRes != 0) {
          setContextImage(contextImageRes)
        }
        is String -> context?.let { ctx ->
          val resourceId = AssetProcessor.getDrawableResourceId(ctx, contextImageRes)
          if (resourceId != 0) {
            setContextImage(resourceId)
          } else {
            Log.w(TAG, "Context image customizado não encontrado")
          }
        }
        else -> {}
      }
    }

    val permissionTheme = theme?.getMap("permission")
    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")

    setPermissionTheme {
      setTitle(firstString(permissionTexts, "title") ?: firstString(texts, "permissionTitle") ?: "Permissões Necessárias")
      setTitleColor(firstString(permissionColors, "titleColor", "title") ?: "#FFFFFF")
      setBackgroundColor(firstString(permissionColors, "backgroundColor", "background") ?: "#1F1F1F")
      setStatusBarColor(firstString(permissionColors, "statusBarColor", "statusBar") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(false)
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

    val resultTheme = theme?.getMap("result")
    val resultColors = resultTheme?.getMap("colors")
    val resultTexts = resultTheme?.getMap("texts")

    setResultTheme {
      setSuccessBackgroundColor(firstString(resultColors, "successBackgroundColor", "successBackground") ?: "#DFFFD6")
      setSuccessIcon(R.drawable.success_icon)
      setSuccessText(firstString(resultTexts, "successText", "success") ?: firstString(texts, "successText") ?: "Verificação concluída com sucesso!")
      setSuccessTextColor(firstString(resultColors, "successTextColor", "successText") ?: "#0F9D58")

      setStatusBarSuccessColor(firstString(resultColors, "statusBarSuccessColor", "successStatusBar") ?: "#DFFFD6")
      setStatusBarErrorColor(firstString(resultColors, "statusBarErrorColor", "errorStatusBar") ?: "#FFD6D6")
      setStatusBarSuccessIsDarkIcons(true)
      setStatusBarErrorIsDarkIcons(true)

      setErrorBackgroundColor(firstString(resultColors, "errorBackgroundColor", "errorBackground") ?: "#FFD6D6")
      setErrorIcon(R.drawable.error_icon)
      setErrorText(firstString(resultTexts, "errorText", "error") ?: firstString(texts, "errorText") ?: "Algo deu errado na verificação.")
      setErrorTextColor(firstString(resultColors, "errorTextColor", "errorText") ?: "#D93025")

      setRetryButtonColor(firstString(resultColors, "retryButtonColor", "retryButtonBackground", "retryBackground") ?: "#0F9D58")
      setRetryButtonText(firstString(resultTexts, "retryButtonText", "retryButton") ?: firstString(texts, "retryButtonText") ?: "Tentar novamente")
      setRetryButtonTextColor(firstString(resultColors, "retryButtonTextColor", "retryButtonText", "retryText") ?: "#FFFFFF")
    }
  }

  private fun firstString(map: ReadableMap?, vararg keys: String): String? {
    map ?: return null
    for (key in keys) {
      if (!map.hasKey(key)) continue
      val value = map.getString(key)?.trim()
      if (!value.isNullOrEmpty()) return value
    }
    return null
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
