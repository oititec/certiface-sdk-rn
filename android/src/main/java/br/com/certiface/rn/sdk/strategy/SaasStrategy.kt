package br.com.certiface.rn.sdk.strategy

import android.content.Context
import android.content.pm.ApplicationInfo
import br.com.certiface.domain.callback.CertifaceResultCallback
import br.com.certiface.domain.model.saas.SaasLivenessOptions
import br.com.certiface.manager.exports.LivenessResult
import br.com.certiface.manager.main.CertifaceSDK
import br.com.certiface.rn.sdk.factories.FacetecThemeFactory
import br.com.certiface.rn.sdk.factories.FortfaceThemeFactory
import com.facebook.react.bridge.ReadableMap

class SaasStrategy {
  fun start(
    context: Context,
    token: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: CertifaceResultCallback<LivenessResult>
  ) {
    val isDebug =
      (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    val opts = SaasLivenessOptions(
      journeyToken = token,
      isDebug = isDebug,
      facetecTheme = FacetecThemeFactory.create(isCustom, theme, context),
      fortfaceTheme = FortfaceThemeFactory.create(isCustom, theme, context)
    )
    val manager = CertifaceSDK.createSaasLivenessManager()
    manager.start(opts, callback)
  }
}
