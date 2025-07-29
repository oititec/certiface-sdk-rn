package br.com.oititec.rn.sdk.executor

import android.content.Context
import androidx.core.graphics.toColorInt
import br.com.oiti.core.config.OitiSDkConfig
import br.com.oiti.domain.liveness.LivenessResponse
import br.com.oiti.domain.model.ErrorResponse
import br.com.oiti.domain.model.iproov.OrientationGPA
import br.com.oiti.domain.model.iproov.OrientationLA
import br.com.oiti.manager.exports.Environment
import br.com.oiti.manager.exports.FilterTheme
import br.com.oiti.manager.exports.IProovFontsKey
import br.com.oiti.manager.exports.IProovManagerOptions
import br.com.oiti.manager.exports.IProovTheme
import br.com.oiti.manager.exports.LivenessResult
import br.com.oiti.manager.exports.NaturalStyle
import br.com.oiti.manager.exports.ResultCallback
import br.com.oiti.manager.exports.SDKConfig
import br.com.oiti.manager.main.OitiSDK
import br.com.oititec.rn.sdk.R

class LivenessExecutor(val appkey: String) {

  fun executeLiveness(
    context: Context,
    execOnSuccess: (LivenessResult?) -> Unit,
    execOnError: (ErrorResponse?) -> Unit,
    isCustomEnabled: Boolean = false,
    theme: Map<String, Any>? = null
  ) {

    OitiSDK.initialize(
      context,
      SDKConfig(
        environment = Environment.HML,
        appKey = appkey
      )
    )

    val iProovManager =
      OitiSDK.createLivenessManager(provider = OitiSDK.LivenessProvider.IPROOV)

    val themeBuilder = if (isCustomEnabled && theme != null) {
      buildCustomTheme(theme)
    } else {
      buildDefaultTheme()
    }

    val options = IProovManagerOptions("")

    iProovManager.start(options, object : ResultCallback<LivenessResult> {
      override fun onSuccess(livenessResponse: LivenessResponse) {
        execOnSuccess(livenessResponse.livenessResult)
      }

      override fun onError(livenessResponse: LivenessResponse) {
        execOnError(livenessResponse.errorResponse)
      }
    })
  }

  private fun buildCustomTheme(theme: Map<String, Any>): IProovTheme {
    val iProovFonts =
      mapOf(
        IProovFontsKey.INSTRUCTIONS_TITLE_FONT to R.font.sixty,
        IProovFontsKey.INSTRUCTIONS_CAPTION_FONT to R.font.sixty,
        IProovFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to R.font.sixty,
        IProovFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to R.font.sixty,
        IProovFontsKey.INSTRUCTIONS_BUTTON_FONT to R.font.sixty,
        IProovFontsKey.PERMISSION_TITLE_FONT to R.font.sixty,
        IProovFontsKey.PERMISSION_CAPTION_FONT to R.font.sixty,
        IProovFontsKey.PERMISSION_BUTTON_FONT to R.font.sixty,

        IProovFontsKey.RESULT_MESSAGE_FONT to R.font.sixty,
        IProovFontsKey.RESULT_RETRY_BUTTON_FONT to R.font.sixty,
      )

    return IProovTheme.build {
      // Configurações principais
      theme["title"]?.let { setTitle(it.toString()) }
      theme["titleColor"]?.let { setTitleColor(it.toString()) }
      theme["headerBackgroundColor"]?.let { setHeaderBackgroundColor(it.toString()) }
      theme["promptTextColor"]?.let { setPromptTextColor(it.toString()) }
      theme["promptBackgroundColor"]?.let { setPromptBackgroundColor(it.toString()) }
      theme["surroundColor"]?.let { setSurroundColor(it.toString()) }
      theme["fontResource"]?.let { setFontResource(R.font.sixty) }
      theme["isEnabledScreenShots"]?.let { setIsEnabledScreenShots(it as Boolean) }
      theme["disableExteriorEffects"]?.let { setDisableExteriorEffects(it as Boolean) }
      theme["timeoutSecs"]?.let { setTimeoutSecs((it as Number).toInt()) }
      theme["promptRoundedCorners"]?.let { setPromptRoundedCorners(it as Boolean) }
      theme["fontsKey"]?.let { setFontsKey(iProovFonts) }

      // Filter
      val filterType = theme["filterType"] as? String
      val filterStyle = theme["filterStyle"] as? String
      if (filterType == "Natural" && filterStyle == "CLEAR") {
        setFilter(FilterTheme.Natural(NaturalStyle.CLEAR))
      }

      // Orientation
      val orientationGpa = theme["orientationGpa"] as? String
      val orientationLa = theme["orientationLa"] as? String


      // Camera
      val camera = theme["camera"] as? String
      //setCamera(if (camera == "FRONT") CameraSelection.FRONT else CameraSelection.BACK)

      // Oval colors
      val ovalReadyColor = theme["ovalReadyColor"] as? Number
      val ovalNotReadyColor = theme["ovalNotReadyColor"] as? Number
      val ovalStrokeColor = theme["ovalStrokeColor"] as? Number
      val ovalCompletedColor = theme["ovalCompletedColor"] as? Number

      if (ovalReadyColor != null && ovalNotReadyColor != null &&
        ovalStrokeColor != null && ovalCompletedColor != null) {
        setOvalColors(
          ready = ovalReadyColor.toInt(),
          notReady = ovalNotReadyColor.toInt(),
          stroke = ovalStrokeColor.toInt(),
          completed = ovalCompletedColor.toInt()
        )
      }

      // Instructions theme
      val instructionsTheme = theme["instructionsTheme"] as? Map<String, Any>
      instructionsTheme?.let { instructions ->
        setInstructionsTheme {
          instructions["titleText"]?.let { setTitleText(it.toString()) }
          instructions["titleColor"]?.let { setTitleColor(it.toString()) }
          instructions["captionText"]?.let { setCaptionText(it.toString()) }
          instructions["captionColor"]?.let { setCaptionColor(it.toString()) }
          instructions["backgroundColor"]?.let { setBackgroundColor(it.toString()) }
          instructions["bottomSheetColor"]?.let { setBottomSheetColor(it.toString()) }
          instructions["documentTipsInstructionText"]?.let { setDocumentTipsInstructionText(it.toString()) }
          instructions["documentTypesInstructionText"]?.let { setDocumentTypesInstructionText(it.toString()) }
          instructions["bottomSheetCornerRadius"]?.let { setBottomSheetCornerRadius((it as Number).toFloat()) }
          instructions["continueButtonText"]?.let { setContinueButtonText(it.toString()) }
          instructions["continueButtonColor"]?.let { setContinueButtonColor(it.toString()) }
          instructions["continueButtonTextColor"]?.let { setContinueButtonTextColor(it.toString()) }
        }
      }

      // Permission theme
      val permissionTheme = theme["permissionTheme"] as? Map<String, Any>
      permissionTheme?.let { permission ->
        setPermissionTheme {
          permission["title"]?.let { setTitle(it.toString()) }
          permission["titleColor"]?.let { setTitleColor(it.toString()) }
          permission["backgroundColor"]?.let { setBackgroundColor(it.toString()) }
          permission["checkPermissionButtonText"]?.let { setCheckPermissionButtonText(it.toString()) }
          permission["checkPermissionButtonStyle"]?.let { setCheckPermissionButtonStyle(it.toString()) }
        }
      }

      // Processing theme
      val processingTheme = theme["processingTheme"] as? Map<String, Any>
      processingTheme?.let { processing ->
        setProcessingTheme {
          processing["backgroundColor"]?.let { setBackgroundColor(it.toString()) }
          processing["loadingDialogColor"]?.let { setLoadingDialogColor(it.toString()) }
          processing["loadingIndicatorSize"]?.let { setLoadingIndicatorSize((it as Number).toInt()) }
          processing["loadingIndicatorWidth"]?.let { setLoadingIndicatorWidth((it as Number).toInt()) }
        }
      }

      // Result theme
      val resultTheme = theme["resultTheme"] as? Map<String, Any>
      resultTheme?.let { result ->
        setResultTheme {
          result["successBackgroundColor"]?.let { setSuccessBackgroundColor(it.toString()) }
          result["successIcon"]?.let { setSuccessIcon(br.com.oiti.designsystem.R.drawable.success_icon) }
          result["successText"]?.let { setSuccessText(it.toString()) }
          result["successTextColor"]?.let { setSuccessTextColor(it.toString()) }
          result["errorBackgroundColor"]?.let { setErrorBackgroundColor(it.toString()) }
          result["errorIcon"]?.let { setErrorIcon(br.com.oiti.designsystem.R.drawable.error_icon) }
          result["errorText"]?.let { setErrorText(it.toString()) }
          result["errorTextColor"]?.let { setErrorTextColor(it.toString()) }
          result["retryButtonColor"]?.let { setRetryButtonColor(it.toString()) }
          result["retryButtonText"]?.let { setRetryButtonText(it.toString()) }
          result["retryButtonTextColor"]?.let { setRetryButtonTextColor(it.toString()) }
        }
      }
    }
  }

  private fun buildDefaultTheme(): IProovTheme {
    return IProovTheme.build {
      setIsEnabledScreenShots(true)
    }
  }
}
