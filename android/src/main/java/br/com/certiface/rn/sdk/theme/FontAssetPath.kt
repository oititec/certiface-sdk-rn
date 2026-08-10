package br.com.certiface.rn.sdk.theme

fun fontAssetPath(fontName: String?): String {
  val trimmed = fontName?.trim().orEmpty()
  val sanitized = trimmed
    .replace("\\", "/")
    .split("/")
    .lastOrNull()
    .orEmpty()
    .filter { it.isLetterOrDigit() || it == '_' || it == '-' || it == '.' }
  val resolved = if (sanitized.isEmpty()) "ubuntu_regular" else sanitized
  return if (resolved.endsWith(".ttf", ignoreCase = true)) {
    "fonts/$resolved"
  } else {
    "fonts/$resolved.ttf"
  }
}
