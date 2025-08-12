package br.com.oititec.rn.sdk.strategy


import android.content.Context
import br.com.oiti.domain.callback.OitiResultCallback
import br.com.oiti.facetecsdk.domain.model.FacetecManagerOptions
import br.com.oiti.manager.exports.LivenessResult
import br.com.oiti.manager.main.OitiSDK
import br.com.oititec.rn.sdk.factories.FacetecThemeFactory
import com.facebook.react.bridge.ReadableMap

class FacetecStrategy : LivenessProviderStrategy {
  override fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: OitiResultCallback<LivenessResult>
  ) {
    val facetecTheme = FacetecThemeFactory.create(isCustom, theme)
    val opts = FacetecManagerOptions(appKey, facetecTheme)
    val mgr = OitiSDK.createLivenessManager(OitiSDK.LivenessProvider.FACETEC)
    mgr.start(opts, callback)
  }
}
