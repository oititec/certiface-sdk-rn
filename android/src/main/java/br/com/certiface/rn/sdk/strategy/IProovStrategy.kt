package br.com.certiface.rn.sdk.strategy

import android.content.Context
import br.com.certiface.domain.callback.CertifaceResultCallback
import br.com.certiface.iproov.domain.model.IProovManagerOptions
import br.com.certiface.manager.exports.LivenessResult
import br.com.certiface.manager.main.CertifaceSDK
import br.com.certiface.manager.main.LivenessProvider
import br.com.certiface.rn.sdk.factories.IProovThemeFactory
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
    val manager = CertifaceSDK.createLivenessManager(LivenessProvider.IPROOV)
    manager.start(opts, callback)
  }
}
