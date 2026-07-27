import Foundation

enum NativeErrorPayload {
  static func serialize(
    code: String,
    message: String,
    invalidParam: String? = nil
  ) -> String {
    var payload: [String: Any] = [
      "code": code,
      "message": message,
    ]
    if let invalidParam, !invalidParam.isEmpty {
      payload["invalidParam"] = invalidParam
    }

    guard let data = try? JSONSerialization.data(withJSONObject: payload),
          let json = String(data: data, encoding: .utf8)
    else {
      return "{\"code\":\"UNKNOWN_ERROR\",\"message\":\"Failed to serialize error\"}"
    }
    return json
  }

  static func fromThemeError(_ error: ThemeCustomizationError) -> String {
    serialize(
      code: "INVALID_PARAMS",
      message: error.errorMessage,
      invalidParam: error.invalidParam
    )
  }
}
