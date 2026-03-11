package br.com.oititec.sdk.flutter.oiti_sdk.strategy

import android.content.Context
import br.com.oiti.facetecsdk.domain.model.FacetecManagerOptions
import br.com.oiti.manager.exports.LivenessResult
import br.com.oiti.domain.callback.CertifaceResultCallback
import br.com.oiti.manager.main.CertifaceSDK
import br.com.oititec.sdk.flutter.oiti_sdk.factories.FacetecThemeFactory
import br.com.oiti.manager.main.LivenessProvider

class FacetecStrategy : LivenessProviderStrategy {
  override fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: Map<String, Any>?,
    callback: CertifaceResultCallback<LivenessResult>
  ) {
    val facetecTheme = FacetecThemeFactory.create(isCustom, theme, context)
    val opts = FacetecManagerOptions(appKey, facetecTheme)
    val manager = CertifaceSDK.createLivenessManager(LivenessProvider.FACETEC)
    manager.start(opts, callback)
  }
}
