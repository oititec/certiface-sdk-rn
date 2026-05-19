package br.com.certiface.rn.sdk.factories

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType

internal fun firstString(map: ReadableMap?, vararg keys: String): String? {
  map ?: return null
  for (key in keys) {
    if (!map.hasKey(key)) continue
    val value = map.getString(key)?.trim()
    if (!value.isNullOrEmpty()) return value
  }
  return null
}

internal fun optInt(map: ReadableMap?, key: String, default: Int): Int {
  map ?: return default
  if (!map.hasKey(key)) return default
  return when (map.getType(key)) {
    ReadableType.Number -> map.getDouble(key).toInt()
    else -> default
  }
}

internal fun optBoolean(map: ReadableMap?, key: String, default: Boolean): Boolean {
  map ?: return default
  if (!map.hasKey(key)) return default
  return when (map.getType(key)) {
    ReadableType.Boolean -> map.getBoolean(key)
    else -> default
  }
}

internal fun resolveInstructionsBackButtonTintColor(
  instructionsColors: ReadableMap?,
  hasCustomBackButtonImage: Boolean
): String? {
  val explicitTint = firstString(instructionsColors, "backButtonColor")
  if (explicitTint != null) return explicitTint
  if (hasCustomBackButtonImage) return null
  return firstString(instructionsColors, "backButtonIcon")
}
