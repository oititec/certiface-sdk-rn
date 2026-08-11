package br.com.certiface.rn.sdk.factories

import androidx.core.graphics.toColorInt
import br.com.certiface.rn.sdk.exceptions.CustomThemeException
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType

internal object ThemeColorValidator {
  private val PROVIDER_KEYS = setOf("facetec", "iproov", "fortface")
  private const val MAX_DEPTH = 12

  fun validate(theme: ReadableMap?, activeProviderKey: String) {
    theme ?: return
    val iterator = theme.keySetIterator()
    while (iterator.hasNextKey()) {
      val key = iterator.nextKey()
      if (key in PROVIDER_KEYS && key != activeProviderKey) continue
      if (theme.getType(key) == ReadableType.Map) {
        theme.getMap(key)?.let { validateNode(it, 0) }
      }
    }
  }

  fun parseColorOrThrow(hex: String, invalidParam: String): Int {
    return try {
      hex.toColorInt()
    } catch (e: IllegalArgumentException) {
      throw CustomThemeException(invalidParam, "color", e)
    }
  }

  private fun validateNode(map: ReadableMap, depth: Int) {
    if (depth > MAX_DEPTH) {
      throw CustomThemeException("theme", "structure")
    }

    if (map.hasKey("colors") && map.getType("colors") == ReadableType.Map) {
      val colors = map.getMap("colors") ?: return
      val iterator = colors.keySetIterator()
      while (iterator.hasNextKey()) {
        val key = iterator.nextKey()
        if (colors.getType(key) != ReadableType.String) continue
        val hex = colors.getString(key)?.trim().orEmpty()
        if (hex.isEmpty()) continue
        parseColorOrThrow(hex, key)
      }
    }

    val iterator = map.keySetIterator()
    while (iterator.hasNextKey()) {
      val key = iterator.nextKey()
      if (map.getType(key) == ReadableType.Map) {
        map.getMap(key)?.let { validateNode(it, depth + 1) }
      }
    }
  }
}
