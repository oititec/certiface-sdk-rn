package br.com.certiface.rn.sdk.factories

import br.com.certiface.domain.model.facetec.FacetecButtonLocation
import br.com.certiface.domain.model.facetec.FacetecExitAnimationStyle
import br.com.certiface.domain.model.iproov.OrientationGPA
import br.com.certiface.domain.model.iproov.OrientationLA
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType

internal fun firstString(map: ReadableMap?, vararg keys: String): String? {
  map ?: return null
  for (key in keys) {
    if (!map.hasKey(key)) continue
    if (map.getType(key) != ReadableType.String) continue
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

internal fun firstInt(map: ReadableMap?, vararg keys: String, default: Int): Int {
  map ?: return default
  for (key in keys) {
    if (!map.hasKey(key)) continue
    if (map.getType(key) == ReadableType.Number) {
      return map.getDouble(key).toInt()
    }
  }
  return default
}

internal fun optFloat(map: ReadableMap?, key: String, default: Float): Float {
  map ?: return default
  if (!map.hasKey(key)) return default
  return when (map.getType(key)) {
    ReadableType.Number -> map.getDouble(key).toFloat()
    else -> default
  }
}

internal fun optFloatOrNull(map: ReadableMap?, key: String): Float? {
  map ?: return null
  if (!map.hasKey(key)) return null
  if (map.getType(key) != ReadableType.Number) return null
  return map.getDouble(key).toFloat()
}

internal fun optBoolean(map: ReadableMap?, key: String, default: Boolean): Boolean {
  map ?: return default
  if (!map.hasKey(key)) return default
  return when (map.getType(key)) {
    ReadableType.Boolean -> map.getBoolean(key)
    else -> default
  }
}

internal fun clampedInt(
  map: ReadableMap?,
  key: String,
  default: Int,
  min: Int,
  max: Int
): Int {
  return optInt(map, key, default).coerceIn(min, max)
}

internal fun clampedThemeInt(
  map: ReadableMap?,
  key: String,
  min: Int,
  max: Int
): Int? {
  map ?: return null
  if (!map.hasKey(key)) return null
  if (map.getType(key) != ReadableType.Number) return null
  return map.getDouble(key).toInt().coerceIn(min, max)
}

internal fun parseFacetecButtonLocation(
  map: ReadableMap?,
  key: String,
  default: FacetecButtonLocation
): FacetecButtonLocation {
  val value = firstString(map, key)?.uppercase()?.replace(" ", "_") ?: return default
  return when (value) {
    "TOP_LEFT", "TOPLEFT" -> FacetecButtonLocation.TOP_LEFT
    "TOP_RIGHT", "TOPRIGHT" -> FacetecButtonLocation.TOP_RIGHT
    else -> default
  }
}

internal fun parseFacetecExitAnimationStyle(
  map: ReadableMap?,
  key: String,
  default: FacetecExitAnimationStyle
): FacetecExitAnimationStyle {
  val value = firstString(map, key)?.uppercase()?.replace(" ", "_") ?: return default
  return when (value) {
    "CIRCLE_FADE", "CIRCLEFADE" -> FacetecExitAnimationStyle.CIRCLE_FADE
    "RIPPLE_OUT", "RIPPLEOUT" -> FacetecExitAnimationStyle.RIPPLE_OUT
    "RIPPLE_IN", "RIPPLEIN" -> FacetecExitAnimationStyle.RIPPLE_IN
    "NONE" -> FacetecExitAnimationStyle.NONE
    else -> default
  }
}

internal fun parseOrientationGpa(
  map: ReadableMap?,
  key: String,
  default: OrientationGPA
): OrientationGPA {
  val value = firstString(map, key)?.uppercase()?.replace(" ", "_") ?: return default
  return when (value) {
    "PORTRAIT" -> OrientationGPA.PORTRAIT
    "REVERSE_PORTRAIT", "REVERSEPORTRAIT" -> OrientationGPA.REVERSE_PORTRAIT
    "LANDSCAPE" -> OrientationGPA.LANDSCAPE
    "REVERSE_LANDSCAPE", "REVERSELANDSCAPE" -> OrientationGPA.REVERSE_LANDSCAPE
    else -> default
  }
}

internal fun parseOrientationLa(
  map: ReadableMap?,
  key: String,
  default: OrientationLA
): OrientationLA {
  val value = firstString(map, key)?.uppercase()?.replace(" ", "_") ?: return default
  return when (value) {
    "PORTRAIT" -> OrientationLA.PORTRAIT
    "REVERSE_PORTRAIT", "REVERSEPORTRAIT" -> OrientationLA.REVERSE_PORTRAIT
    else -> default
  }
}

internal fun parseFortfaceCancelPosition(
  map: ReadableMap?,
  key: String,
  default: br.com.certiface.domain.model.fortface.FortfaceCancelPosition
): br.com.certiface.domain.model.fortface.FortfaceCancelPosition {
  val value = firstString(map, key)?.uppercase()?.replace(" ", "_") ?: return default
  return when (value) {
    "LEFT" -> br.com.certiface.domain.model.fortface.FortfaceCancelPosition.LEFT
    "RIGHT" -> br.com.certiface.domain.model.fortface.FortfaceCancelPosition.RIGHT
    else -> default
  }
}

internal fun parseFortfaceScreenMode(
  map: ReadableMap?,
  key: String,
  default: br.com.certiface.domain.model.fortface.FortfaceScreenMode
): br.com.certiface.domain.model.fortface.FortfaceScreenMode {
  val value = firstString(map, key)?.uppercase()?.replace(" ", "_") ?: return default
  return when (value) {
    "FULL_SCREEN", "FULLSCREEN" -> br.com.certiface.domain.model.fortface.FortfaceScreenMode.FULL_SCREEN
    "MODAL" -> br.com.certiface.domain.model.fortface.FortfaceScreenMode.MODAL
    else -> default
  }
}

internal fun parseFortfaceScreenOrientation(
  map: ReadableMap?,
  key: String,
  default: br.com.certiface.domain.model.fortface.FortfaceScreenOrientation
): br.com.certiface.domain.model.fortface.FortfaceScreenOrientation {
  val value = firstString(map, key)?.uppercase()?.replace(" ", "_") ?: return default
  return when (value) {
    "PORTRAIT" -> br.com.certiface.domain.model.fortface.FortfaceScreenOrientation.PORTRAIT
    "LANDSCAPE" -> br.com.certiface.domain.model.fortface.FortfaceScreenOrientation.LANDSCAPE
    "AUTOMATIC", "AUTO" -> br.com.certiface.domain.model.fortface.FortfaceScreenOrientation.AUTOMATIC
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
