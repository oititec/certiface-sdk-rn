package br.com.oititec.rn.sdk.strategy

import android.content.Context
import br.com.oiti.domain.callback.CertifaceResultCallback
import br.com.oiti.iproov.domain.model.IProovManagerOptions
import br.com.oiti.manager.exports.LivenessResult
import br.com.oiti.manager.main.CertifaceSDK
import br.com.oititec.rn.sdk.factories.IProovThemeFactory
import com.facebook.react.bridge.ReadableMap

class IProovStrategy : LivenessProviderStrategy {
  override fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: CertifaceResultCallback<LivenessResult>
  ) {
    val iproovTheme = IProovThemeFactory.create(isCustom, theme, context)
    val opts = IProovManagerOptions(appKey, iproovTheme)
    val manager = CertifaceSDK.createLivenessManager(CertifaceSDK.LivenessProvider.IPROOV)
    manager.start(opts, callback)
  }
}
