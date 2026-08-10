import Foundation
import UIKit

enum ThemeCustomizationValidator {
  static func validate(_ theme: [String: Any]?, provider: ThemeValidationProvider) throws {
    guard let theme else { return }

    let scopedTheme = scopeToProvider(theme, provider: provider)

    if let colorIssue = findColorIssue(in: scopedTheme) {
      throw ThemeCustomizationError.invalidParameters(
        category: "color",
        invalidParam: colorIssue
      )
    }

    if let drawableIssue = findDrawableIssue(in: scopedTheme) {
      throw ThemeCustomizationError.invalidParameters(
        category: "drawable",
        invalidParam: drawableIssue
      )
    }

    if let fontIssue = findFontIssue(in: scopedTheme) {
      throw ThemeCustomizationError.invalidParameters(
        category: "font",
        invalidParam: fontIssue
      )
    }

    if provider == .facetec, let textIssue = findBlankTextIssue(in: scopedTheme) {
      throw ThemeCustomizationError.invalidParameters(
        category: "text",
        invalidParam: textIssue
      )
    }
  }

  private static func scopeToProvider(
    _ theme: [String: Any],
    provider: ThemeValidationProvider
  ) -> [String: Any] {
    var scoped = theme
    for key in provider.excludedThemeKeys {
      scoped.removeValue(forKey: key)
    }
    return scoped
  }

  private static func findFontIssue(in theme: [String: Any]) -> String? {
    let fontEntries = collectFontEntries(from: theme)
    for (param, fontName) in fontEntries {
      if resolveFontName(fontName) == nil {
        return param
      }
    }
    return nil
  }

  private static func findDrawableIssue(in theme: [String: Any]) -> String? {
    let assetEntries = collectAssetEntries(from: theme)
    for (param, imageName) in assetEntries {
      if RnSdkBundle.getImage(named: imageName) == nil {
        return param
      }
    }
    return nil
  }

  private static func findColorIssue(in node: Any, pathPrefix: String = "", depth: Int = 0) -> String? {
    guard depth <= 12 else { return "theme" }
    guard let dictionary = node as? [String: Any] else { return nil }

    if let colors = dictionary["colors"] as? [String: Any] {
      for (key, value) in colors {
        guard let hex = value as? String else { continue }
        let trimmed = hex.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else { continue }
        if UIColor(hex: trimmed) == nil {
          return key
        }
      }
    }

    for (key, value) in dictionary {
      if let nested = findColorIssue(in: value, pathPrefix: key, depth: depth + 1) {
        return nested
      }
    }
    return nil
  }

  private static func findBlankTextIssue(in theme: [String: Any]) -> String? {
    guard let facetec = theme["facetec"] as? [String: Any],
          let texts = facetec["texts"] as? [String: Any]
    else {
      return nil
    }

    let keys = [
      "readyHeader1",
      "readyHeader2",
      "readyMessage1",
      "readyMessage2",
      "readyButton",
      "retryHeader",
      "retrySubheader",
      "retryMessageSmile",
      "retryMessageLighting",
      "retryMessageContrast",
      "retryYourPicture",
      "retryIdealPicture",
      "retryButton",
      "resultUploadMessage",
      "resultSuccessMessage",
      "feedbackLookStraightInOval",
    ]

    for key in keys {
      guard let value = texts[key] as? String else { continue }
      if value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
        return mapFacetecTextKey(key)
      }
    }
    return nil
  }

  private static func mapFacetecTextKey(_ key: String) -> String {
    switch key {
    case "resultSuccessMessage":
      return "RESULT_SUCCESS_MESSAGE"
    case "resultUploadMessage":
      return "RESULT_UPLOAD_MESSAGE"
    case "readyHeader1":
      return "READY_HEADER_1"
    case "readyHeader2":
      return "READY_HEADER_2"
    case "readyMessage1":
      return "READY_MESSAGE_1"
    case "readyMessage2":
      return "READY_MESSAGE_2"
    case "readyButton":
      return "READY_BUTTON"
    case "retryHeader":
      return "RETRY_HEADER"
    case "retrySubheader":
      return "RETRY_SUBHEADER"
    case "retryMessageSmile":
      return "RETRY_MESSAGE_SMILE"
    case "retryMessageLighting":
      return "RETRY_MESSAGE_LIGHTING"
    case "retryMessageContrast":
      return "RETRY_MESSAGE_CONTRAST"
    case "retryYourPicture":
      return "RETRY_YOUR_PICTURE"
    case "retryIdealPicture":
      return "RETRY_IDEAL_PICTURE"
    case "retryButton":
      return "RETRY_BUTTON"
    case "feedbackLookStraightInOval":
      return "FEEDBACK_LOOK_STRAIGHT_IN_OVAL"
    default:
      return key
    }
  }

  private static func collectFontEntries(from theme: [String: Any]) -> [(String, String)] {
    var entries: [(String, String)] = []

    func appendFonts(from node: [String: Any]?, prefix: String) {
      guard let fonts = node?["fonts"] as? [String: Any] else { return }
      for (key, value) in fonts {
        guard let name = value as? String else { continue }
        let trimmed = name.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else { continue }
        entries.append((mapFontParam(prefix: prefix, key: key), trimmed))
      }
    }

    appendFonts(from: theme["instructions"] as? [String: Any], prefix: "INSTRUCTIONS")
    appendFonts(from: theme["permission"] as? [String: Any], prefix: "PERMISSION")
    appendFonts(from: theme["result"] as? [String: Any], prefix: "RESULT")
    appendFonts(from: theme["facetec"] as? [String: Any], prefix: "FACETEC")
    appendFonts(from: theme["iproov"] as? [String: Any], prefix: "IPROOV")

    if let iproov = theme["iproov"] as? [String: Any] {
      if let fontResource = iproov["fontResource"] as? String {
        let trimmed = fontResource.trimmingCharacters(in: .whitespacesAndNewlines)
        if !trimmed.isEmpty {
          entries.append(("IPROOV_BASE_FONT", trimmed))
        }
      }
      if let fontPath = iproov["fontPath"] as? String {
        let trimmed = fontPath.trimmingCharacters(in: .whitespacesAndNewlines)
        if !trimmed.isEmpty {
          entries.append(("IPROOV_BASE_FONT", trimmed))
        }
      }
    }

    return entries
  }

  private static func mapFontParam(prefix: String, key: String) -> String {
    switch (prefix, key) {
    case ("INSTRUCTIONS", "title"):
      return "INSTRUCTIONS_TITLE_FONT"
    case ("INSTRUCTIONS", "caption"):
      return "INSTRUCTIONS_CAPTION_FONT"
    case ("INSTRUCTIONS", "firstInstructionTitle"):
      return "INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT"
    case ("INSTRUCTIONS", "secondInstructionTitle"):
      return "INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT"
    case ("INSTRUCTIONS", "continueButton"):
      return "INSTRUCTIONS_BUTTON_FONT"
    case ("PERMISSION", "title"):
      return "PERMISSION_TITLE_FONT"
    case ("PERMISSION", "caption"):
      return "PERMISSION_CAPTION_FONT"
    case ("PERMISSION", "checkPermissionButton"):
      return "PERMISSION_BUTTON_FONT"
    case ("RESULT", "text"):
      return "RESULT_MESSAGE_FONT"
    case ("RESULT", "retryButton"):
      return "RESULT_RETRY_BUTTON_FONT"
    case ("FACETEC", "guidanceHeader"):
      return "GUIDANCE_CUSTOMIZATION_HEADER_FONT"
    case ("FACETEC", "guidanceSubtext"):
      return "GUIDANCE_CUSTOMIZATION_SUBTEXT_FONT"
    case ("FACETEC", "guidanceButton"):
      return "GUIDANCE_CUSTOMIZATION_BUTTON_FONT"
    case ("FACETEC", "readyScreenHeader"):
      return "GUIDANCE_CUSTOMIZATION_READY_SCREEN_HEADER_FONT"
    case ("FACETEC", "readyScreenSubtext"):
      return "GUIDANCE_CUSTOMIZATION_READY_SCREEN_SUBTEXT_FONT"
    case ("FACETEC", "retryScreenHeader"):
      return "GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_HEADER_FONT"
    case ("FACETEC", "retryScreenSubtext"):
      return "GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_SUBTEXT_FONT"
    case ("FACETEC", "resultScreenMessage"):
      return "RESULT_SCREEN_CUSTOMIZATION_MESSAGE_FONT"
    case ("FACETEC", "feedbackMessage"):
      return "FEEDBACK_CUSTOMIZATION_TEXT_FONT"
    case ("IPROOV", "instructionsTitleFont"):
      return "INSTRUCTIONS_TITLE_FONT"
    case ("IPROOV", "instructionsCaptionFont"):
      return "INSTRUCTIONS_CAPTION_FONT"
    case ("IPROOV", "instructionsDocumentTypesInstructionsFont"):
      return "INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT"
    case ("IPROOV", "instructionsDocumentTipsInstructionsFont"):
      return "INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT"
    case ("IPROOV", "instructionsButtonFont"):
      return "INSTRUCTIONS_BUTTON_FONT"
    case ("IPROOV", "permissionTitleFont"):
      return "PERMISSION_TITLE_FONT"
    case ("IPROOV", "permissionCaptionFont"):
      return "PERMISSION_CAPTION_FONT"
    case ("IPROOV", "permissionButtonFont"):
      return "PERMISSION_BUTTON_FONT"
    case ("IPROOV", "resultMessageFont"):
      return "RESULT_MESSAGE_FONT"
    case ("IPROOV", "resultRetryButtonFont"):
      return "RESULT_RETRY_BUTTON_FONT"
    default:
      return "\(prefix)_\(key)".uppercased()
    }
  }

  private static func collectAssetEntries(from theme: [String: Any]) -> [(String, String)] {
    var entries: [(String, String)] = []

    func appendAssets(from node: [String: Any]?, mapping: [String: String]) {
      guard let assets = node?["assets"] as? [String: Any] else { return }
      for (key, param) in mapping {
        guard let name = assets[key] as? String else { continue }
        let trimmed = name.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else { continue }
        entries.append((param, trimmed))
      }
    }

    appendAssets(
      from: theme["instructions"] as? [String: Any],
      mapping: [
        "backButtonIcon": "INSTRUCTIONS_BACK_BUTTON_IMG",
        "contextImage": "INSTRUCTIONS_CONTEXT_IMAGE",
        "firstInstructionIcon": "INSTRUCTIONS_FIRST_INSTRUCTION_ICON",
        "secondInstructionIcon": "INSTRUCTIONS_SECOND_INSTRUCTION_ICON",
      ]
    )
    appendAssets(
      from: theme["permission"] as? [String: Any],
      mapping: [
        "backButtonIcon": "PERMISSION_BACK_BUTTON_ICON",
        "cameraImage": "PERMISSION_CAMERA_ICON",
      ]
    )
    appendAssets(
      from: theme["result"] as? [String: Any],
      mapping: [
        "successImage": "RESULT_SUCCESS_ICON",
        "errorImage": "RESULT_ERROR_ICON",
        "retryImage": "RESULT_RETRY_ICON",
      ]
    )
    appendAssets(
      from: theme["facetec"] as? [String: Any],
      mapping: [
        "overlayBrandImage": "FACETEC_OVERLAY_SHOW_BRANDING_IMAGE",
        "cancelButtonIcon": "FACETEC_CANCEL_BUTTON_CUSTOM_IMAGE",
        "resultScreenSuccessImage": "FACETEC_RESULT_CUSTOM_STATIC_RESULT_ANIMATION_SUCCESS",
        "resultScreenErrorImage": "FACETEC_RESULT_CUSTOM_STATIC_RESULT_ANIMATION_UNSUCCESS",
      ]
    )
    appendAssets(
      from: theme["iproov"] as? [String: Any],
      mapping: [
        "logoImage": "IPROOV_LOGO",
        "closeButtonIcon": "IPROOV_CLOSE_BUTTON",
      ]
    )

    return entries
  }

  private static func resolveFontName(_ fontName: String) -> String? {
    FontNameResolver.resolve(fontName)
  }
}
