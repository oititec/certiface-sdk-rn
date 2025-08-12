package br.com.oititec.rn.sdk.strategy

import android.content.Context
import br.com.oiti.domain.callback.OitiResultCallback
import br.com.oiti.manager.exports.LivenessResult
import com.facebook.react.bridge.ReadableMap

interface LivenessProviderStrategy {
  fun start(
    context: Context,
    appKey: String,
    isCustom: Boolean,
    theme: ReadableMap?,
    callback: OitiResultCallback<LivenessResult>
  )
}
