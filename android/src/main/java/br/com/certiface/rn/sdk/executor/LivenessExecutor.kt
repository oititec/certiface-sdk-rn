package br.com.certiface.rn.sdk.executor

import android.content.Context
import br.com.certiface.domain.liveness.LivenessResponse
import br.com.certiface.domain.model.ErrorResponse
import br.com.certiface.manager.exports.Environment
import br.com.certiface.manager.exports.LivenessResult
import br.com.certiface.manager.exports.ResultCallback
import br.com.certiface.manager.exports.SDKConfig
import br.com.certiface.manager.main.CertifaceSDK
import br.com.certiface.rn.sdk.exceptions.CustomThemeException
import br.com.certiface.rn.sdk.model.Features
import br.com.certiface.rn.sdk.strategy.FacetecStrategy
import br.com.certiface.rn.sdk.strategy.IProovStrategy
import br.com.certiface.rn.sdk.strategy.LivenessProviderStrategy
import br.com.certiface.rn.sdk.strategy.SaasStrategy
import com.facebook.react.bridge.ReadableMap
import org.json.JSONObject

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
        execOnError(serializeErrorResponse(result.errorResponse))
      }
    }

    try {
      strategy.start(context, appkey, isCustomEnabled, theme, callback)
    } catch (e: CustomThemeException) {
      execOnError(e.toErrorPayloadJson())
    } catch (e: UnsupportedOperationException) {
      execOnError(
        JSONObject()
          .put("code", "UNSUPPORTED_OPERATION")
          .put("message", e.message ?: "Operação não suportada")
          .toString()
      )
    }
  }

  private fun serializeErrorResponse(errorResponse: ErrorResponse?): String {
    if (errorResponse == null) {
      return JSONObject()
        .put("code", "UNKNOWN_ERROR")
        .put("message", "Unknown error occurred")
        .toString()
    }

    val json = JSONObject()
      .put("code", errorResponse.errorType.name)
      .put("message", errorResponse.errorMessage)
    if (!errorResponse.invalidParam.isNullOrEmpty()) {
      json.put("invalidParam", errorResponse.invalidParam)
    }
    return json.toString()
  }

  companion object {
    private val saasStrategy = SaasStrategy()
    private const val SAAS_PLACEHOLDER_APP_KEY = "saas-demo"

    fun executeSaasLiveness(
      context: Context,
      token: String,
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
          appKey = SAAS_PLACEHOLDER_APP_KEY
        )
      )

      val callback = object : ResultCallback<LivenessResult> {
        override fun onSuccess(result: LivenessResponse) {
          execOnSuccess(result.livenessResult)
        }

        override fun onError(result: LivenessResponse) {
          execOnError(serializeSaasErrorResponse(result.errorResponse))
        }
      }

      try {
        saasStrategy.start(context, token, isCustomEnabled, theme, callback)
      } catch (e: CustomThemeException) {
        execOnError(e.toErrorPayloadJson())
      }
    }

    private fun serializeSaasErrorResponse(errorResponse: ErrorResponse?): String {
      if (errorResponse == null) {
        return JSONObject()
          .put("code", "UNKNOWN_ERROR")
          .put("message", "Unknown error occurred")
          .toString()
      }

      val json = JSONObject()
        .put("code", errorResponse.errorType.name)
        .put("message", errorResponse.errorMessage)
      if (!errorResponse.invalidParam.isNullOrEmpty()) {
        json.put("invalidParam", errorResponse.invalidParam)
      }
      return json.toString()
    }
  }
}
