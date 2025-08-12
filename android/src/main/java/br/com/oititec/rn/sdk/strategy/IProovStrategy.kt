package br.com.oititec.rn.sdk.strategy

import android.content.Context
import br.com.oiti.domain.callback.OitiResultCallback
import br.com.oiti.iproov.domain.model.IProovManagerOptions
import br.com.oiti.manager.exports.LivenessResult
import br.com.oiti.manager.main.OitiSDK
import br.com.oititec.rn.sdk.factories.IProovThemeFactory
import com.facebook.react.bridge.ReadableMap

class IProovStrategy : LivenessProviderStrategy {
  override fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: OitiResultCallback<LivenessResult>
  ) {
    val iproovTheme = IProovThemeFactory.create(isCustom, theme)
    val opts = IProovManagerOptions(appKey, iproovTheme)
    val manager = OitiSDK.createLivenessManager(OitiSDK.LivenessProvider.IPROOV)
    manager.start(opts, callback)
  }
}
