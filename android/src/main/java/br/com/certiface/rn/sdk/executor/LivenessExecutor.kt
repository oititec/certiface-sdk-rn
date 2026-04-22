package br.com.certiface.rn.sdk.executor

import android.content.Context
import br.com.certiface.domain.liveness.LivenessResponse
import br.com.certiface.domain.model.ErrorResponse
import br.com.certiface.manager.exports.Environment
import br.com.certiface.manager.exports.LivenessResult
import br.com.certiface.manager.exports.ResultCallback
import br.com.certiface.manager.exports.SDKConfig
import br.com.certiface.manager.main.CertifaceSDK
import br.com.certiface.rn.sdk.model.Features
import br.com.certiface.rn.sdk.strategy.FacetecStrategy
import br.com.certiface.rn.sdk.strategy.IProovStrategy
import br.com.certiface.rn.sdk.strategy.LivenessProviderStrategy
import com.facebook.react.bridge.ReadableMap

class LivenessExecutor(val appkey: String, val feature: Features) {

  private val strategies: Map<Features, LivenessProviderStrategy> = mapOf(
    Features.Facetec to FacetecStrategy(),
    Features.IProov to IProovStrategy()
  )

  fun executeLiveness(
    context: Context,
    environment: String,
    execOnSuccess: (LivenessResult?) -> Unit,
    execOnError: (String?) -> Unit,
    isCustomEnabled: Boolean = false,
    theme: ReadableMap? = null
  ) {
    val sdkEnvironment = when (environment) {
      "HML" -> Environment.HML
      "PRD" -> Environment.PRD
      else -> Environment.HML
    }

    CertifaceSDK.initialize(
      context,
      SDKConfig(
        environment = sdkEnvironment,
        appKey = appkey
      )
    )

    val strategy = strategies[feature]
      ?: error("Nenhuma strategy pra feature $feature")

    val callback = object : ResultCallback<LivenessResult> {
      override fun onSuccess(result: LivenessResponse) {
        execOnSuccess(result.livenessResult)
      }

      override fun onError(result: LivenessResponse) {
        val errorResponse = result.errorResponse
        val errorMessage = if (errorResponse != null) {
          "[${errorResponse.errorType}]: ${errorResponse.errorMessage}"
        } else {
          "Unknown error occurred"
        }
        execOnError(errorMessage)
      }
    }
    strategy.start(context, appkey, isCustomEnabled, theme, callback)
  }
}
