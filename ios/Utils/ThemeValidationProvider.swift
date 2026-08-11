import Foundation

enum ThemeValidationProvider {
  case facetec
  case iproov
  case fortface

  var themeKey: String {
    switch self {
    case .facetec:
      return "facetec"
    case .iproov:
      return "iproov"
    case .fortface:
      return "fortface"
    }
  }

  var excludedThemeKeys: [String] {
    switch self {
    case .facetec:
      return ["iproov", "fortface"]
    case .iproov:
      return ["facetec", "fortface"]
    case .fortface:
      return ["facetec", "iproov"]
    }
  }
}
