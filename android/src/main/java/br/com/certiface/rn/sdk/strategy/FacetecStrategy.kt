package br.com.certiface.rn.sdk.strategy

import android.content.Context
import br.com.certiface.domain.callback.CertifaceResultCallback
import br.com.certiface.manager.exports.LivenessResult
import com.facebook.react.bridge.ReadableMap

/**
 * @deprecated FaceTec now requires a journeyToken (SaaS flow) instead of an appKey.
 * Use [SaasStrategy] via `startSaasJourney()` instead.
 *
 * This strategy is kept for backward compatibility but will emit a clear
 * error if invoked, since [FacetecManagerOptions] no longer accepts appKey.
 */
@Deprecated(
  message = "FaceTec agora usa fluxo SaaS com journeyToken. Use startSaasJourney() no lugar de startJourney() com provider FACETEC.",
  replaceWith = ReplaceWith("SaasStrategy")
)
class FacetecStrategy : LivenessProviderStrategy {
  override fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: CertifaceResultCallback<LivenessResult>
  ) {
    throw UnsupportedOperationException(
      "FaceTec agora usa fluxo SaaS com journeyToken. " +
        "Use CertifaceSDK.startSaasJourney(token, environment, ...) ao invés de " +
        "startJourney(appKey, environment, 'FACETEC', ...)."
    )
  }
}
