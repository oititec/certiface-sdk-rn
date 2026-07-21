package br.com.certiface.rn.sdk.exceptions

import org.json.JSONObject

class CustomThemeException(
  val invalidParam: String,
  val category: String,
  cause: Throwable? = null
) : Exception(
  "Parâmetros de customização inválidos: $category $invalidParam.",
  cause
) {
  fun toErrorPayloadJson(): String {
    return JSONObject()
      .put("code", "INVALID_PARAMS")
      .put("message", message)
      .put("invalidParam", invalidParam)
      .toString()
  }
}
