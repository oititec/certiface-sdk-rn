package br.com.certiface.rn.sdk.theme

import android.content.Context
import br.com.certiface.designsystem.R

object FontResolver {
  val defaultFontRes: Int = R.font.ubuntu_regular
  private const val DEFAULT_ASSET_MARKER = "ubuntu_regular"

  fun resolve(context: Context?, fontName: String?): Any {
    if (context == null) return fontAssetPath(fontName)
    val resourceId = resolveFontResourceId(context, fontName)
    if (resourceId != defaultFontRes) return resourceId
    val assetPath = fontAssetPath(fontName)
    if (fontAssetExists(context, assetPath)) return assetPath
    return defaultFontRes
  }

  fun resolveFromAssetPath(context: Context?, assetPath: String): Any {
    val trimmed = assetPath.trim()
    if (trimmed.isEmpty()) return defaultFontRes
    val name = trimmed
      .removePrefix("fonts/")
      .substringBeforeLast(".ttf")
      .substringBeforeLast(".otf")
    return resolveExplicit(context, name.ifEmpty { null }, trimmed)
  }

  fun resolveExplicit(context: Context?, fontName: String?, assetPath: String): Any {
    if (context == null) return assetPath
    val resourceId = resolveFontResourceId(context, fontName)
    if (resourceId != defaultFontRes) return resourceId
    if (fontAssetExists(context, assetPath)) return assetPath
    if (assetPath.contains(DEFAULT_ASSET_MARKER)) return defaultFontRes
    return assetPath
  }

  fun resolveFontResourceId(context: Context, fontName: String?): Int {
    val raw = fontName?.trim().orEmpty()
    if (raw.isEmpty()) return defaultFontRes

    val normalized = raw
      .substringAfterLast('/')
      .substringBeforeLast(".ttf")
      .substringBeforeLast(".otf")

    val packages = listOf(
      context.packageName,
      "br.com.certiface.rn.sdk",
      "br.com.certiface.designsystem",
      "br.com.fortface.sdk"
    )
    for (pkg in packages) {
      val resourceId = context.resources.getIdentifier(normalized, "font", pkg)
      if (resourceId != 0) return resourceId
    }
    return defaultFontRes
  }

  fun resolveCameraFontResId(context: Context?, assetPathOrName: String): Int? {
    if (context == null) return null
    val name = assetPathOrName
      .trim()
      .removePrefix("fonts/")
      .substringAfterLast('/')
      .substringBeforeLast(".ttf")
      .substringBeforeLast(".otf")
      .trim()
    if (name.isEmpty() || name == DEFAULT_ASSET_MARKER) return null
    val resourceId = resolveFontResourceId(context, name)
    return resourceId.takeIf { it != 0 && it != defaultFontRes }
  }

  fun fontAssetExists(context: Context, assetPath: String): Boolean {
    return try {
      context.assets.open(assetPath).close()
      true
    } catch (_: Exception) {
      false
    }
  }

  fun isResolvedDefault(value: Any): Boolean {
    return value == defaultFontRes ||
      (value is String && value.contains(DEFAULT_ASSET_MARKER))
  }
}
