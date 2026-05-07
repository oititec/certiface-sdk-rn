package br.com.certiface.rn.sdk.theme

fun fontAssetPath(fontName: String?): String {
  val trimmed = fontName?.trim().orEmpty()
  val resolved = if (trimmed.isEmpty()) "ubuntu_regular" else trimmed
  return if (resolved.endsWith(".ttf", ignoreCase = true)) {
    "fonts/$resolved"
  } else {
    "fonts/$resolved.ttf"
  }
}
