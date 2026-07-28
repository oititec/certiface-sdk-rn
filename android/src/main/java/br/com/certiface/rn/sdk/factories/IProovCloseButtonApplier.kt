package br.com.certiface.rn.sdk.factories

import android.content.Context
import br.com.certiface.manager.exports.IProovDrawablesKey
import br.com.certiface.rn.sdk.exceptions.CustomThemeException
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
      val resourceId = AssetProcessor.resolveDrawableResourceId(context, closeButtonIconName)
        ?: throw CustomThemeException(IProovDrawablesKey.IPROOV_CLOSE_BUTTON.name, "drawable")
      setCloseButton(resourceId)
    }

    closeButtonColor?.let(setCloseButtonColor)
  }
}
