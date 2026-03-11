package br.com.oititec.sdk.flutter.oiti_sdk.strategy

import android.content.Context
import br.com.oiti.iproov.domain.model.IProovManagerOptions
import br.com.oiti.manager.exports.LivenessResult
import br.com.oiti.domain.callback.CertifaceResultCallback
import br.com.oiti.manager.main.CertifaceSDK
import br.com.oiti.manager.main.LivenessProvider
import br.com.oititec.sdk.flutter.oiti_sdk.factories.IProovThemeFactory

class IProovStrategy : LivenessProviderStrategy {
  override fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: Map<String, Any>?,
    callback: CertifaceResultCallback<LivenessResult>
  ) {
    val iproovTheme = IProovThemeFactory.create(isCustom, theme, context)
    val opts = IProovManagerOptions(appKey, iproovTheme)
    val manager = CertifaceSDK.createLivenessManager(LivenessProvider.IPROOV)
    manager.start(opts, callback)
  }
}
