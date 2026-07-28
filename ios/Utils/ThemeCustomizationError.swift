import Foundation

enum ThemeCustomizationError: Error {
  case invalidParameters(category: String, invalidParam: String)

  var category: String {
    switch self {
    case let .invalidParameters(category, _):
      return category
    }
  }

  var invalidParam: String {
    switch self {
    case let .invalidParameters(_, invalidParam):
      return invalidParam
    }
  }

  var errorMessage: String {
    "Parâmetros de customização inválidos: \(category) \(invalidParam)."
  }
}
