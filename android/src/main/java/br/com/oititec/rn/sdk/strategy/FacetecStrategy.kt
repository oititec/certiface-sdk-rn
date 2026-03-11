package br.com.oititec.rn.sdk.strategy

import android.content.Context
import br.com.oiti.domain.callback.CertifaceResultCallback
import br.com.oiti.facetecsdk.domain.model.FacetecManagerOptions
import br.com.oiti.manager.exports.LivenessResult
import br.com.oiti.manager.main.CertifaceSDK
import br.com.oiti.manager.main.LivenessProvider
import br.com.oititec.rn.sdk.factories.FacetecThemeFactory
import com.facebook.react.bridge.ReadableMap

class FacetecStrategy : LivenessProviderStrategy {
  override fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: CertifaceResultCallback<LivenessResult>
  ) {
    val facetecTheme = FacetecThemeFactory.create(isCustom, theme, context)
    val opts = FacetecManagerOptions(appKey, facetecTheme)
    val manager = CertifaceSDK.createLivenessManager(LivenessProvider.FACETEC)
    manager.start(opts, callback)
  }
}
