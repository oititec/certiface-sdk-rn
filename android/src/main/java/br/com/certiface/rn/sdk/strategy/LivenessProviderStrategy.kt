package br.com.certiface.rn.sdk.strategy

import android.content.Context
import br.com.certiface.domain.callback.CertifaceResultCallback
import br.com.certiface.manager.exports.LivenessResult
import com.facebook.react.bridge.ReadableMap

interface LivenessProviderStrategy {
  fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: CertifaceResultCallback<LivenessResult>
  )
}
