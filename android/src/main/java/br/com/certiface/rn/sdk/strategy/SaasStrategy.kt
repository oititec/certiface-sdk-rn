package br.com.certiface.rn.sdk.strategy

import android.content.Context
import android.content.pm.ApplicationInfo
import br.com.certiface.domain.callback.CertifaceResultCallback
import br.com.certiface.domain.model.saas.SaasLivenessOptions
import br.com.certiface.manager.exports.LivenessResult
import br.com.certiface.manager.main.CertifaceSDK
import br.com.certiface.rn.sdk.factories.FacetecThemeFactory
import br.com.certiface.rn.sdk.factories.FortfaceThemeFactory
import br.com.certiface.rn.sdk.factories.optBoolean
import br.com.certiface.rn.sdk.telemetry.RnFacetecStartupTelemetry
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
    val facetecTheme =
      if (isCustom) {
        RnFacetecStartupTelemetry.measure("rn_facetec_theme_build") {
          FacetecThemeFactory.create(true, theme, context)
        }
      } else {
        FacetecThemeFactory.buildDefault()
      }
    val fortfaceTheme =
      if (isCustom) {
        RnFacetecStartupTelemetry.measure("rn_fortface_theme_build") {
          FortfaceThemeFactory.create(true, theme, context)
        }
      } else {
        val showInstructionScreen =
          optBoolean(
            theme?.getMap("instructions")?.getMap("configuration"),
            "showInstructionScreen",
            true
          )
        FortfaceThemeFactory.buildDefault(showInstructionScreen)
      }
    val opts = SaasLivenessOptions(
      journeyToken = token,
      isDebug = isDebug,
      facetecTheme = facetecTheme,
      fortfaceTheme = fortfaceTheme
    )
    val manager = CertifaceSDK.createSaasLivenessManager()
    RnFacetecStartupTelemetry.mark("rn_saas_manager_start")
    manager.start(opts, callback)
  }
}
