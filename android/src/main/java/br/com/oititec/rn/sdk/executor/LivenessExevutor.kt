package br.com.oititec.rn.sdk.executor

import android.content.Context
//import br.com.oiti.domain.liveness.LivenessResponse
//import br.com.oiti.domain.model.ErrorResponse
//import br.com.oiti.manager.exports.Environment
//import br.com.oiti.manager.exports.LivenessResult
//import br.com.oiti.manager.exports.ResultCallback
//import br.com.oiti.manager.exports.SDKConfig
//import br.com.oiti.manager.main.OitiSDK
//import br.com.oititec.rn.sdk.model.Features
//import br.com.oititec.rn.sdk.strategy.FacetecStrategy
//import br.com.oititec.rn.sdk.strategy.IProovStrategy
//import br.com.oititec.rn.sdk.strategy.LivenessProviderStrategy
//
//class LivenessExecutor(val appkey: String, val feature: Features) {
//
//  private val strategies: Map<Features, LivenessProviderStrategy> = mapOf(
//    Features.Facetec to FacetecStrategy(),
//    Features.IProov to IProovStrategy()
//  )
//
//  fun executeLiveness(
//    context: Context,
//    execOnSuccess: (LivenessResult?) -> Unit,
//    execOnError: (ErrorResponse?) -> Unit,
//    isCustomEnabled: Boolean = false
//  ) {
//    OitiSDK.initialize(
//      context,
//      SDKConfig(
//        environment = Environment.HML,
//        appKey = appkey
//      )
//    )
//
//    val strategy = strategies[feature]
//      ?: error("Nenhuma strategy pra feature $feature")
//
//    val callback = object : ResultCallback<LivenessResult> {
//      override fun onSuccess(result: LivenessResponse) {
//        execOnSuccess(result.livenessResult)
//      }
//
//      override fun onError(result: LivenessResponse) {
//        execOnError(result.errorResponse)
//      }
//    }
//    strategy.start(context, appkey, isCustomEnabled, callback)
//  }
//}
