package br.com.oititec.rn.sdk.factories

import android.content.Context
import android.util.Log
import androidx.core.graphics.toColorInt
import br.com.oiti.designsystem.R
import br.com.oiti.domain.model.iproov.OrientationGPA
import br.com.oiti.domain.model.iproov.OrientationLA
import br.com.oiti.manager.exports.FilterTheme
import br.com.oiti.manager.exports.IProovDrawablesKey
import br.com.oiti.manager.exports.IProovFontsKey
import br.com.oiti.manager.exports.IProovTheme
import br.com.oiti.manager.exports.NaturalStyle
import br.com.oititec.rn.sdk.theme.IProovFonts
import br.com.oititec.rn.sdk.processors.AssetProcessor
import com.facebook.react.bridge.ReadableMap

object IProovThemeFactory {
  private const val TAG = "IProovThemeFactory"

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

    val iProovFonts = if (iproovFontsMap != null) {
      IProovFonts(iproovFontsMap).apply()
    } else {
      mapOf(
        IProovFontsKey.INSTRUCTIONS_TITLE_FONT to R.font.ubuntu_regular,
        IProovFontsKey.INSTRUCTIONS_CAPTION_FONT to R.font.ubuntu_regular,
        IProovFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to R.font.ubuntu_regular,
        IProovFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to R.font.ubuntu_regular,
        IProovFontsKey.INSTRUCTIONS_BUTTON_FONT to R.font.ubuntu_regular,
        IProovFontsKey.PERMISSION_TITLE_FONT to R.font.ubuntu_regular,
        IProovFontsKey.PERMISSION_CAPTION_FONT to R.font.ubuntu_regular,
        IProovFontsKey.PERMISSION_BUTTON_FONT to R.font.ubuntu_regular,
        IProovFontsKey.RESULT_MESSAGE_FONT to R.font.ubuntu_regular,
        IProovFontsKey.RESULT_RETRY_BUTTON_FONT to R.font.ubuntu_regular,
      )
    }

    setTitle(texts?.getString("title") ?: "Verificação Facial")
    setTitleColor(colors?.getString("titleColor") ?: "#FFFFFF")
    setHeaderBackgroundColor(colors?.getString("headerBackgroundColor") ?: "#121212")
    setPromptTextColor(colors?.getString("promptTextColor") ?: "#FFFFFF")
    setPromptBackgroundColor(colors?.getString("promptBackgroundColor") ?: "#1F1F1F")
    setSurroundColor(colors?.getString("surroundColor") ?: "#00FF00")
    setFontResource(R.font.ubuntu_regular)
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
      ready = (colors?.getString("ovalReadyColor") ?: "#00FF00").toColorInt(),
      notReady = (colors?.getString("ovalNotReadyColor") ?: "#FF0000").toColorInt(),
      stroke = (colors?.getString("ovalStrokeColor") ?: "#FFFFFF").toColorInt(),
      completed = (colors?.getString("ovalCompletedColor") ?: "#00FF00").toColorInt()
    )

    val instructionsTheme = theme?.getMap("instructions")
    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")
    val instructionsConfiguration = instructionsTheme?.getMap("configuration")
    val showInstructionScreen = instructionsConfiguration?.getBoolean("showInstructionScreen") ?: true

    Log.d(TAG, "🏭 Iniciando construção do tema IProov customizado...")
    val iproovDrawables = AssetProcessor.processIProovAssets(theme)
    Log.d(TAG, "📦 Assets processados: ${iproovDrawables.size} encontrados")
    
    Log.d(TAG, "🎨 Assets encontrados para processamento: ${iproovDrawables.size}")
    iproovDrawables.forEach { (key, value) ->
      Log.d(TAG, "   📎 $key = '$value'")
    }
    
    if (iproovDrawables.isNotEmpty()) {
      Log.d(TAG, "🎨 Configurando drawables customizados: ${iproovDrawables.size} assets")
      setDrawablesKey(iproovDrawables)
    } else {
      Log.d(TAG, "📋 Nenhum drawable customizado encontrado, usando padrões")
    }

    setInstructionsTheme {
      setShowInstructionScreen(showInstructionScreen)
      setTitleText(instructionsTexts?.getString("titleText") ?: texts?.getString("instructionsTitleText") ?: "Teste title")
      setTitleColor(instructionsColors?.getString("titleColor") ?: "#FFFFFF")
      setCaptionText(instructionsTexts?.getString("captionText") ?: texts?.getString("instructionsCaptionText") ?: "teste caption.")
      setCaptionColor(instructionsColors?.getString("captionColor") ?: "#AAAAAA")
      setBackgroundColor(instructionsColors?.getString("backgroundColor") ?: "#1F1F1F")
      setStatusBarColor(instructionsColors?.getString("statusBarColor") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(false)
      setBottomSheetColor(instructionsColors?.getString("bottomSheetColor") ?: "#333333")
      setBottomSheetCornerRadius(16f)
      setContinueButtonText(instructionsTexts?.getString("continueButtonText") ?: texts?.getString("continueButtonText") ?: "Startar")
      setContinueButtonColor(instructionsColors?.getString("continueButtonColor") ?: "#00FF00")
      setContinueButtonTextColor(instructionsColors?.getString("continueButtonTextColor") ?: "#000000")

      val contextImageName = iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE] as? String
      if (contextImageName != null && context != null) {
        val resourceId = AssetProcessor.getDrawableResourceId(context, contextImageName)
        if (resourceId != 0) {
          Log.d(TAG, "✅ Usando context image customizado: $resourceId")
          setContextImage(resourceId)
        } else {
          Log.w(TAG, "⚠️ Context image customizado não encontrado")
        }
      }
    }

    val permissionTheme = theme?.getMap("permission")
    val permissionColors = permissionTheme?.getMap("colors")
    val permissionTexts = permissionTheme?.getMap("texts")

    setPermissionTheme {
      setTitle(permissionTexts?.getString("title") ?: texts?.getString("permissionTitle") ?: "Permissões Necessárias")
      setTitleColor(permissionColors?.getString("titleColor") ?: "#FFFFFF")
      setBackgroundColor(permissionColors?.getString("backgroundColor") ?: "#1F1F1F")
      setStatusBarColor(permissionColors?.getString("statusBarColor") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(false)
    }

    val processingTheme = theme?.getMap("processing")
    val processingColors = processingTheme?.getMap("colors")

    setProcessingTheme {
      setBackgroundColor(processingColors?.getString("backgroundColor") ?: "#000000")
      setLoadingDialogColor(processingColors?.getString("loadingDialogColor") ?: "#FFFFFF")
      setStatusBarColor(processingColors?.getString("statusBarColor") ?: "#000000")
      setStatusBarIsDarkIcons(true)
      setLoadingIndicatorSize(100)
      setLoadingIndicatorWidth(10)
    }

    val resultTheme = theme?.getMap("result")
    val resultColors = resultTheme?.getMap("colors")
    val resultTexts = resultTheme?.getMap("texts")

    setResultTheme {
      setSuccessBackgroundColor(resultColors?.getString("successBackgroundColor") ?: "#DFFFD6")
      setSuccessIcon(R.drawable.success_icon)
      setSuccessText(resultTexts?.getString("successText") ?: texts?.getString("successText") ?: "Verificação concluída com sucesso!")
      setSuccessTextColor(resultColors?.getString("successTextColor") ?: "#0F9D58")

      setStatusBarSuccessColor(resultColors?.getString("statusBarSuccessColor") ?: "#DFFFD6")
      setStatusBarErrorColor(resultColors?.getString("statusBarErrorColor") ?: "#FFD6D6")
      setStatusBarSuccessIsDarkIcons(true)
      setStatusBarErrorIsDarkIcons(true)

      setErrorBackgroundColor(resultColors?.getString("errorBackgroundColor") ?: "#FFD6D6")
      setErrorIcon(R.drawable.error_icon)
      setErrorText(resultTexts?.getString("errorText") ?: texts?.getString("errorText") ?: "Algo deu errado na verificação.")
      setErrorTextColor(resultColors?.getString("errorTextColor") ?: "#D93025")

      setRetryButtonColor(resultColors?.getString("retryButtonColor") ?: "#0F9D58")
      setRetryButtonText(resultTexts?.getString("retryButtonText") ?: texts?.getString("retryButtonText") ?: "Tentar novamente")
      setRetryButtonTextColor(resultColors?.getString("retryButtonTextColor") ?: "#FFFFFF")
    }
  }
}
