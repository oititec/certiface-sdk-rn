import Foundation

enum ThemeValidationProvider {
  case facetec
  case iproov

  var themeKey: String {
    switch self {
    case .facetec:
      return "facetec"
    case .iproov:
      return "iproov"
    }
  }

  var excludedThemeKeys: [String] {
    switch self {
    case .facetec:
      return ["iproov"]
    case .iproov:
      return ["facetec"]
    }
  }
}
