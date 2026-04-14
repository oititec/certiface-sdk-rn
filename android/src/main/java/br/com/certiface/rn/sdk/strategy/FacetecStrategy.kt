package br.com.certiface.rn.sdk.strategy

import android.content.Context
import br.com.certiface.domain.callback.CertifaceResultCallback
import br.com.certiface.facetecsdk.domain.model.FacetecManagerOptions
import br.com.certiface.manager.exports.LivenessResult
import br.com.certiface.manager.main.CertifaceSDK
import br.com.certiface.manager.main.LivenessProvider
import br.com.certiface.rn.sdk.factories.FacetecThemeFactory
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
