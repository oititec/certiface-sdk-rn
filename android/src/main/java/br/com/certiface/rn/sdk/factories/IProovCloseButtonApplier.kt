package br.com.certiface.rn.sdk.factories

import android.content.Context
import br.com.certiface.rn.sdk.processors.AssetProcessor
import com.facebook.react.bridge.ReadableMap

internal object IProovCloseButtonApplier {
  fun apply(
    context: Context?,
    iproovAssets: ReadableMap?,
    colors: ReadableMap?,
    setCloseButton: (Int) -> Unit,
    setCloseButtonColor: (String) -> Unit
  ) {
    val closeButtonIconName = iproovAssets?.getString("closeButtonIcon")?.trim().orEmpty()
    val closeButtonColor = firstString(colors, "closeButtonColor", "closeButtonIcon")

    if (closeButtonIconName.isNotEmpty()) {
      AssetProcessor.resolveDrawableResourceId(context, closeButtonIconName)?.let(setCloseButton)
    }

    closeButtonColor?.let(setCloseButtonColor)
  }
}
