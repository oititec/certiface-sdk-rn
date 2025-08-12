package br.com.oititec.rn.sdk.factories

import androidx.core.graphics.toColorInt
import br.com.oiti.designsystem.R
import br.com.oiti.domain.model.iproov.CameraSelection
import br.com.oiti.domain.model.iproov.OrientationGPA
import br.com.oiti.domain.model.iproov.OrientationLA
import br.com.oiti.manager.exports.FilterTheme
import br.com.oiti.manager.exports.IProovFontsKey
import br.com.oiti.manager.exports.IProovTheme
import br.com.oiti.manager.exports.NaturalStyle
import br.com.oititec.rn.sdk.theme.IProovFonts
import com.facebook.react.bridge.ReadableMap

object IProovThemeFactory {
  fun create(isCustom: Boolean, theme: ReadableMap? = null): IProovTheme =
    if (isCustom) buildCustom(theme) else buildDefault()

  private fun buildDefault() = IProovTheme.build {
    setIsEnabledScreenShots(true)
  }

  private fun buildCustom(theme: ReadableMap? = null) = IProovTheme.build {

    val iproovTheme = theme?.getMap("iproov")
    val colors = iproovTheme?.getMap("colors")
    val texts = iproovTheme?.getMap("texts")
    val iproovFontsMap = iproovTheme?.getMap("fonts")
    
    val iProovFonts = IProovFonts(iproovFontsMap).apply()

    setTitle(texts?.getString("title") ?: "Verificação Facial")
    setTitleColor(colors?.getString("titleColor") ?: "#FFFFFF")
    setHeaderBackgroundColor(colors?.getString("headerBackgroundColor") ?: "#121212")
    setPromptTextColor(colors?.getString("promptTextColor") ?: "#FFFFFF")
    setPromptBackgroundColor(colors?.getString("promptBackgroundColor") ?: "#1F1F1F")
    setSurroundColor(colors?.getString("surroundColor") ?: "#00FF00")
    setFontResource(R.font.ubuntu_regular)
    setIsEnabledScreenShots(true)
    setDisableExteriorEffects(false)
//            setDisableExteriorEffects(true)
    setTimeoutSecs(60)
    setPromptRoundedCorners(true)
    setFontsKey(iProovFonts)
    setFilter(FilterTheme.Natural(NaturalStyle.CLEAR))

    setOrientation(
      gpa = OrientationGPA.PORTRAIT,
      la = OrientationLA.PORTRAIT
    )
    setCamera(CameraSelection.FRONT)

    setOvalColors(
      ready = (colors?.getString("ovalReadyColor") ?: "#00FF00").toColorInt(),
      notReady = (colors?.getString("ovalNotReadyColor") ?: "#FF0000").toColorInt(),
      stroke = (colors?.getString("ovalStrokeColor") ?: "#FFFFFF").toColorInt(),
      completed = (colors?.getString("ovalCompletedColor") ?: "#00FF00").toColorInt()
    )

    val instructionsTheme = theme?.getMap("instructions")
    val instructionsColors = instructionsTheme?.getMap("colors")
    val instructionsTexts = instructionsTheme?.getMap("texts")

    setInstructionsTheme {
      setTitleText(instructionsTexts?.getString("titleText") ?: texts?.getString("instructionsTitleText") ?: "Teste title")
      setTitleColor(instructionsColors?.getString("titleColor") ?: "#FFFFFF")
      setCaptionText(instructionsTexts?.getString("captionText") ?: texts?.getString("instructionsCaptionText") ?: "teste caption.")
      setCaptionColor(instructionsColors?.getString("captionColor") ?: "#AAAAAA")
      setBackgroundColor(instructionsColors?.getString("backgroundColor") ?: "#1F1F1F")
      setStatusBarColor(instructionsColors?.getString("statusBarColor") ?: "#1F1F1F")
      setStatusBarIsDarkIcons(false)
      setBottomSheetColor(instructionsColors?.getString("bottomSheetColor") ?: "#333333")
      setDocumentTipsInstructionText(instructionsTexts?.getString("documentTipsInstructionText") ?: texts?.getString("documentTipsInstructionText") ?: "teste 1")
      setDocumentTypesInstructionText(instructionsTexts?.getString("documentTypesInstructionText") ?: texts?.getString("documentTypesInstructionText") ?: "teste 2")
      setBottomSheetCornerRadius(16f)
      setContinueButtonText(instructionsTexts?.getString("continueButtonText") ?: texts?.getString("continueButtonText") ?: "Startar")
      setContinueButtonColor(instructionsColors?.getString("continueButtonColor") ?: "#00FF00")
      setContinueButtonTextColor(instructionsColors?.getString("continueButtonTextColor") ?: "#000000")
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
      setCheckPermissionButtonText(permissionTexts?.getString("checkPermissionButtonText") ?: texts?.getString("checkPermissionButtonText") ?: "Permitir Acesso")
      setCheckPermissionButtonStyle(permissionColors?.getString("checkPermissionButtonColor") ?: "#00FF00")
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
