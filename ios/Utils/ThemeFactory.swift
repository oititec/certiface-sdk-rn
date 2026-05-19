//
//  ThemeFactory.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 01/08/25.
//

import CertifaceSDK
import CertifaceIProov
import UIKit

final class ThemeFactory {
  static func createIProovCustomization(from theme: [String: Any]?) -> IProovCustomization {
    let builder = IProovCustomization.builder()
    guard let theme else { return builder.build() }

    return builder
      .setInstructionCustomization { instructionBuilder in
        customizeInstruction(in: instructionBuilder, with: theme)
      }
      .setCameraPermissionCustomization { cameraPermissionBuilder in
        customizeCameraPermission(in: cameraPermissionBuilder, with: theme)
      }
      .setLivenessCustomization { livenessBuilder in
        customizeLivenessIProov(in: livenessBuilder, with: theme)
      }
      .setLoadingCustomization { loadingBuilder in
        customizeLoadingIProov(in: loadingBuilder, with: theme)
      }
      .setResultCustomization { resultBuilder in
        customizeResultIProov(in: resultBuilder, with: theme)
      }
      .build()
  }

  static func createFacetecCustomization(from theme: [String: Any]?) -> FacetecCustomization {
    let builder = FacetecCustomization.builder()
    guard let theme else { return builder.build() }

    return builder
      .setInstructionCustomization { instructionBuilder in
        customizeInstruction(in: instructionBuilder, with: theme)
      }
      .setCameraPermissionCustomization { cameraPermissionBuilder in
        customizeCameraPermission(in: cameraPermissionBuilder, with: theme)
      }
      .setLoadingCustomization { loadingBuilder in
        customizeLoadingFacetec(in: loadingBuilder, with: theme)
      }
      .setLivenessDefaultTheme { defaultThemeBuilder in
        customizeLivenessFacetec(in: defaultThemeBuilder, with: theme)
      }
      .setLivenessLowLightTheme { defaultThemeBuilder in
        customizeLivenessFacetec(in: defaultThemeBuilder, with: theme)
      }
      .setLivenessTexts(customizeLivenessTexts(from: theme))
      .build()
  }

  // MARK: - Customization

  // MARK: Certiface

  private static func customizeInstruction(
    in builder: InstructionCustomizationBuilder,
    with theme: [String: Any]
  ) -> InstructionCustomizationBuilder {
    guard let instructionsTheme = theme["instructions"] as? [String: Any] else {
       return builder
    }

    let colors = instructionsTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["background"], with: builder.setBackgroundColor(_:))
    setColor(colors["backButtonIcon"], with: builder.setBackButtonIconColor(_:))
    setColor(colors["backButtonBackground"], with: builder.setBackButtonBackgroundColor(_:))
    setColor(colors["backButtonBorder"], with: builder.setBackButtonBorderColor(_:))
    setColor(colors["bottomSheet"], with: builder.setBottomSheetColor(_:))

    setColor(colors["title"], with: builder.setTitleColor)
    setColor(colors["caption"], with: builder.setCaptionColor(_:))
    setColor(colors["firstInstructionTitle"], with: builder.setFirstInstructionTitleColor(_:))
    setColor(colors["secondInstructionTitle"], with: builder.setSecondInstructionTitleColor(_:))
    setColor(
      colors["continueButtonTextColor"] ?? colors["continueButtonText"],
      with: builder.setContinueButtonTextColor(_:)
    )
    setColor(colors["continueButtonBackground"], with: builder.setContinueButtonBackgroundColor(_:))
    setColor(colors["continueButtonBorder"], with: builder.setContinueButtonBorderColor(_:))

    let texts = instructionsTheme["texts"] as? [String: String] ?? [:]
    setText(texts["title"], with: builder.setTitle(_:))
    setText(texts["caption"], with: builder.setCaption(_:))
    setText(texts["firstInstruction"], with: builder.setFirstInstructionTitle(_:))
    setText(texts["secondInstruction"], with: builder.setSecondInstructionTitle(_:))
    setText(texts["continueButton"] ?? texts["continueButtonText"], with: builder.setContinueButtonText(_:))

    let assets = instructionsTheme["assets"] as? [String: String] ?? [:]
    setImage(assets["backButtonIcon"], with: builder.setBackButtonIcon(_:))
    setImage(assets["contextImage"], with: builder.setContextImage(_:))
    setImage(assets["firstInstructionIcon"], with: builder.setFirstInstructionIcon(_:))
    setImage(assets["secondInstructionIcon"], with: builder.setSecondInstructionIcon(_:))

    let instFonts = instructionsTheme["fonts"] as? [String: String] ?? [:]
    let iproovTheme = theme["iproov"] as? [String: Any] ?? [:]
    let iproovFonts = iproovTheme["fonts"] as? [String: String] ?? [:]
    let iproovFontResource = resolveIProovBaseFont(from: iproovTheme)
    setFont(instFonts["title"] ?? iproovFonts["instructionsTitleFont"] ?? iproovFontResource, with: builder.setTitleFont(_:), size: 20)
    setFont(instFonts["caption"] ?? iproovFonts["instructionsCaptionFont"] ?? iproovFontResource, with: builder.setCaptionFont(_:), size: 20)
    setFont(
      instFonts["firstInstructionTitle"] ?? iproovFonts["instructionsDocumentTypesInstructionsFont"] ?? iproovFontResource,
      with: builder.setFirstInstructionTitleFont(_:),
      size: 20
    )
    setFont(
      instFonts["secondInstructionTitle"] ?? iproovFonts["instructionsDocumentTipsInstructionsFont"] ?? iproovFontResource,
      with: builder.setSecondInstructionTitleFont(_:),
      size: 20
    )
    setFont(
      instFonts["continueButton"] ?? iproovFonts["instructionsButtonFont"] ?? iproovFontResource,
      with: builder.setContinueButtonFont(_:),
      size: 20
    )

    return builder
  }

  private static func customizeCameraPermission(
    in builder: CameraPermissionCustomizationBuilder,
    with theme: [String: Any]
  ) -> CameraPermissionCustomizationBuilder {
    guard let cameraPermissionTheme = theme["permission"] as? [String: Any] else {
      return builder
    }

    let colors = cameraPermissionTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["background"], with: builder.setBackgroundColor(_:))
    setColor(colors["backButtonIcon"], with: builder.setBackButtonIconColor(_:))
    setColor(colors["backButtonBackground"], with: builder.setBackButtonBackgroundColor(_:))
    setColor(colors["backButtonBorder"], with: builder.setBackButtonBorderColor(_:))
    setColor(colors["cameraImage"], with: builder.setCameraImageColor(_:))
    setColor(colors["title"], with: builder.setTitleColor(_:))
    setColor(colors["caption"], with: builder.setCaptionColor(_:))
    setColor(colors["checkPermissionButtonText"], with: builder.setCheckPermissionButtonTextColor(_:))
    setColor(colors["checkPermissionButtonBackground"], with: builder.setCheckPermissionButtonBackgroundColor(_:))
    setColor(colors["checkPermissionButtonBorder"], with: builder.setCheckPermissionButtonBorderColor(_:))
    setColor(colors["bottomSheet"], with: builder.setBottomSheetColor(_:))
    setColor(colors["bottomSheetTitle"], with: builder.setBottomSheetTitleColor(_:))
    setColor(colors["bottomSheetCaption"], with: builder.setBottomSheetCaptionColor(_:))
    setColor(colors["openSettingsButtonText"], with: builder.setOpenSettingsButtonTextColor(_:))
    setColor(colors["openSettingsButtonBackground"], with: builder.setOpenSettingsButtonBackgroundColor(_:))
    setColor(colors["openSettingsButtonBorder"], with: builder.setOpenSettingsButtonBorderColor(_:))
    setColor(colors["closeButtonText"], with: builder.setCloseButtonTextColor(_:))
    setColor(colors["closeButtonBackground"], with: builder.setCloseButtonBackgroundColor(_:))
    setColor(colors["closeButtonBorder"], with: builder.setCloseButtonBorderColor(_:))

    let texts = cameraPermissionTheme["texts"] as? [String: String] ?? [:]
    setText(texts["title"], with: builder.setTitle(_:))
    setText(texts["caption"], with: builder.setCaption(_:))
    setText(texts["checkPermissionButton"], with: builder.setCheckPermissionButtonText(_:))
    setText(texts["bottomSheetTitle"], with: builder.setBottomSheetTitle(_:))
    setText(texts["bottomSheetCaption"], with: builder.setBottomSheetCaption(_:))
    setText(texts["openSettingsButton"], with: builder.setOpenSettingsButtonText(_:))
    setText(texts["closeButton"], with: builder.setCloseButtonText(_:))

    let assets = cameraPermissionTheme["assets"] as? [String: String] ?? [:]
    setImage(assets["backButtonIcon"], with: builder.setBackButtonIcon(_:))
    setImage(assets["cameraImage"], with: builder.setCameraImage(_:))

    let permFonts = cameraPermissionTheme["fonts"] as? [String: String] ?? [:]
    let iproovTheme = theme["iproov"] as? [String: Any] ?? [:]
    let iproovFonts = iproovTheme["fonts"] as? [String: String] ?? [:]
    let iproovFontResource = resolveIProovBaseFont(from: iproovTheme)
    setFont(permFonts["title"] ?? iproovFonts["permissionTitleFont"] ?? iproovFontResource, with: builder.setTitleFont(_:), size: 20)
    setFont(permFonts["caption"] ?? iproovFonts["permissionCaptionFont"] ?? iproovFontResource, with: builder.setCaptionFont(_:), size: 20)
    setFont(
      permFonts["checkPermissionButton"] ?? iproovFonts["permissionButtonFont"] ?? iproovFontResource,
      with: builder.setCheckPermissionButtonTextFont(_:),
      size: 20
    )
    setFont(
      permFonts["bottomSheetTitle"] ?? iproovFonts["permissionTitleFont"] ?? iproovFontResource,
      with: builder.setBottomSheetTitleFont(_:),
      size: 20
    )
    setFont(
      permFonts["bottomSheetCaption"] ?? iproovFonts["permissionCaptionFont"] ?? iproovFontResource,
      with: builder.setBottomSheetCaptionFont(_:),
      size: 20
    )
    setFont(
      permFonts["openSettingsButton"] ?? permFonts["opentSettingsButton"] ?? iproovFonts["permissionButtonFont"] ?? iproovFontResource,
      with: builder.setOpenSettingsButtonTextFont(_:),
      size: 20
    )
    setFont(
      permFonts["closeButton"] ?? iproovFonts["permissionButtonFont"] ?? iproovFontResource,
      with: builder.setCloseButtonTextFont(_:),
      size: 20
    )

    return builder
  }

  // MARK: IProov

  private static func customizeLivenessIProov(
    in builder: IProovLivenessCustomizationBuilder,
    with theme: [String: Any]
  ) -> IProovLivenessCustomizationBuilder {
    guard let instructionsTheme = theme["iproov"] as? [String: Any] else {
       return builder
    }

    let colors = instructionsTheme["colors"] as? [String: String] ?? [:]
    setColor(
      firstValue(in: colors, keys: "closeButtonColor", "closeButtonIcon"),
      with: builder.setCloseButtonImageColor(_:)
    )
    setColor(firstValue(in: colors, keys: "title", "titleColor"), with: builder.setTitleTextColor(_:))
    setColor(firstValue(in: colors, keys: "titleBackground", "headerBackgroundColor"), with: builder.setTitleBackgroundColor(_:))
    setColor(firstValue(in: colors, keys: "promptText", "promptTextColor"), with: builder.setPromptTextColor(_:))
    setColor(firstValue(in: colors, keys: "promptBackground", "promptBackgroundColor"), with: builder.setPromptBackgroundColor(_:))
    setColor(firstValue(in: colors, keys: "background", "surroundColor"), with: builder.setBackgroundColor(_:))
    setColor(firstValue(in: colors, keys: "ovalReady", "ovalReadyColor"), with: builder.setGPAOvalStrokeReadyColor(_:))
    setColor(firstValue(in: colors, keys: "ovalNotReady", "ovalNotReadyColor"), with: builder.setGPAOvalStrokeNotReadyColor(_:))
    setColor(firstValue(in: colors, keys: "ovalCapturing", "ovalStrokeColor"), with: builder.setLAOvalStrokeCapturingColor(_:))
    setColor(firstValue(in: colors, keys: "ovalCompleted", "ovalCompletedColor"), with: builder.setLAOvalStrokeCompletedColor(_:))
    setColor(colors["filterLineDrawingForeground"], with: builder.setFilterLineDrawingForegroundColor(_:))
    setColor(colors["filterLineDrawingBackground"], with: builder.setFilterLineDrawingBackgroundColor(_:))

    let texts = instructionsTheme["texts"] as? [String: String] ?? [:]
    setText(texts["title"], with: builder.setTitle(_:))

    let assets = instructionsTheme["assets"] as? [String: String] ?? [:]
    setImage(assets["closeButtonIcon"], with: builder.setCloseButtonImage(_:))
    setImage(assets["logoImage"], with: builder.setLogoImage(_:))
    _ = applyIProovBaseFont(in: builder, with: instructionsTheme)

    return builder
  }

  private static func customizeLoadingIProov(
    in builder: LoadingCustomizationBuilder,
    with theme: [String: Any]
  ) -> LoadingCustomizationBuilder {
    guard let loadingTheme = theme["processing"] as? [String: Any] else {
       return builder
    }

    let colors = loadingTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["background"], with: builder.setBackgroundColor(_:))
    setColor(colors["loading"], with: builder.setSpinnerColor(_:))

    return builder
  }

  private static func customizeResultIProov(
    in builder: IProovResultCustomizationBuilder,
    with theme: [String: Any]
  ) -> IProovResultCustomizationBuilder {
    guard let resultTheme = theme["result"] as? [String: Any] else {
       return builder
    }

    let colors = resultTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["successBackground"], with: builder.setSuccessBackgroundColor(_:))
    setColor(colors["successText"], with: builder.setSuccessMessageColor(_:))
    setColor(colors["errorBackground"], with: builder.setErrorBackgroundColor(_:))
    setColor(colors["errorText"], with: builder.setErrorMessageColor(_:))
    setColor(colors["retryBackground"], with: builder.setRetryBackgroundColor(_:))
    setColor(colors["retryText"], with: builder.setRetryMessageColor(_:))
    setColor(colors["retryButtonText"], with: builder.setRetryButtonTextColor(_:))
    setColor(colors["retryButtonBackground"], with: builder.setRetryButtonBackgroundColor(_:))
    setColor(colors["retryButtonBorder"], with: builder.setRetryButtonBorderColor(_:))

    let texts = resultTheme["texts"] as? [String: String] ?? [:]
    setText(texts["success"], with: builder.setSuccessMessage(_:))
    setText(texts["error"], with: builder.setErrorMessage(_:))
    setText(texts["retryButton"], with: builder.setRetryButtonText(_:))

    let assets = resultTheme["assets"] as? [String: String] ?? [:]
    setImage(assets["successImage"], with: builder.setSuccessImage(_:))
    setImage(assets["errorImage"], with: builder.setErrorImage(_:))
    setImage(assets["retryImage"], with: builder.setRetryImage(_:))

    let resultFonts = resultTheme["fonts"] as? [String: String] ?? [:]
    let iproovTheme = theme["iproov"] as? [String: Any] ?? [:]
    let iproovFonts = iproovTheme["fonts"] as? [String: String] ?? [:]
    let iproovFontResource = resolveIProovBaseFont(from: iproovTheme)
    setFont(
      resultFonts["text"] ?? iproovFonts["resultMessageFont"] ?? iproovFontResource,
      with: builder.setMessageFont(_:),
      size: 20
    )
    setFont(
      resultFonts["retryButton"] ?? iproovFonts["resultRetryButtonFont"] ?? iproovFontResource,
      with: builder.setRetryButtonTextFont(_:),
      size: 20
    )

    return builder
  }

  // MARK: Facetec

  private static func customizeLoadingFacetec(
    in builder: LoadingCustomizationBuilder,
    with theme: [String: Any]
  ) -> LoadingCustomizationBuilder {
    guard let loadingTheme = theme["processing"] as? [String: Any] else {
       return builder
    }

    let colors = loadingTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["background"], with: builder.setBackgroundColor(_:))
    setColor(colors["loading"], with: builder.setSpinnerColor(_:))

    return builder
  }

  private static func customizeLivenessFacetec(
    in builder: Liveness3DThemeBuilder,
    with theme: [String: Any]
  ) -> Liveness3DThemeBuilder {
    guard let livenessTheme = theme["facetec"] as? [String: Any] else {
       return builder
    }

    let colors = livenessTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["readyScreenHeader"], with: builder.setReadyScreenHeaderColor(_:))
    setColor(colors["readyScreenSubtext"], with: builder.setReadyScreenMessageColor(_:))
    setColor(colors["readyScreenTextBackground"], with: builder.setReadyScreenTextBackgroundColor(_:))
    setColor(colors["resultScreenMessage"], with: builder.setResultScreenMessageColor(_:))
    setColor(
      firstValue(in: colors, keys: "resultScreenUploadProgressBarFill", "resultScreenUploadProgressFill"),
      with: builder.setResultScreenUploadProgressBarFillColor(_:)
    )
    setColor(
      firstValue(in: colors, keys: "resultScreenUploadProgressBarTrack", "resultScreenUploadProgressTrack"),
      with: builder.setResultScreenUploadProgressBarTrackColor(_:)
    )
    builder.setResultScreenAnimationStyle(.blob(appearance: getResultStyleApperance(from: colors)))
    setColor(colors["retryScreenHeader"], with: builder.setRetryScreenHeaderColor(_:))
    setColor(colors["retryScreenSubtext"], with: builder.setRetryScreenCaptionColor(_:))
    setColor(colors["retryScreenImageBorder"], with: builder.setRetryScreenImageBorderColor(_:))
    setColor(colors["feedbackMessage"], with: builder.setFeedbackMessageColor(_:))
    setColor(colors["feedbackBarBackground"], with: builder.setFeedbackBarBackgroundColor(_:))
    setColor(colors["guidanceButtonTextNormal"], with: builder.setGuidanceButtonTextNormalColor(_:))
    setColor(colors["guidanceButtonTextHighlight"], with: builder.setGuidanceButtonTextHighlightColor(_:))
    setColor(colors["guidanceButtonTextDisabled"], with: builder.setGuidanceButtonTextDisabledColor(_:))
    setColor(colors["guidanceButtonBackgroundNormal"], with: builder.setGuidanceButtonBackgroundNormalColor(_:))
    setColor(colors["guidanceButtonBackgroundHighlight"], with: builder.setGuidanceButtonBackgroundHighlightColor(_:))
    setColor(colors["guidanceButtonBackgroundDisabled"], with: builder.setGuidanceButtonBackgroundDisabledColor(_:))
    setColor(colors["guidanceButtonBorder"], with: builder.setGuidanceButtonBorderColor(_:))
    setColor(colors["frameBorder"], with: builder.setFrameBorderColor(_:))
    setColor(colors["frameBackground"], with: builder.setFrameBackgroundColor(_:))
    setColor(colors["ovalStroke"], with: builder.setOvalStrokeColor(_:))
    setColor(colors["ovalProgressFirst"], with: builder.setOvalProgressFirstColor(_:))
    setColor(colors["ovalProgressSecond"], with: builder.setOvalProgressSecondColor(_:))
    setColor(colors["overlayBackground"], with: builder.setOverlayBackgroundColor(_:))

    let assets = livenessTheme["assets"] as? [String: String] ?? [:]
    setImage(assets["overlayBrandImage"], with: builder.setOverlayBrandImage(_:))
    setImage(assets["cancelButtonIcon"], with: builder.setCancelButtonIcon(_:))

    let fonts = livenessTheme["fonts"] as? [String: String] ?? [:]
    setFont(fonts["readyScreenHeader"], with: builder.setReadyScreenHeaderFont(_:), size: 0)
    setFont(fonts["readyScreenSubtext"], with: builder.setReadyScreenMessageFont(_:), size: 0)
    setFont(fonts["resultScreenMessage"], with: builder.setResultScreenMessageFont(_:), size: 0)
    setFont(fonts["retryScreenHeader"], with: builder.setRetryScreenHeaderFont(_:), size: 0)
    setFont(fonts["retryScreenSubtext"], with: builder.setRetryScreenCaptionFont(_:), size: 0)
    setFont(fonts["feedbackMessage"], with: builder.setFeedbackMessageFont(_:), size: 0)
    setFont(fonts["guidanceHeader"], with: builder.setGuidanceHeaderFont(_:), size: 0)
    setFont(fonts["guidanceSubtext"], with: builder.setGuidanceSubtextFont(_:), size: 0)
    setFont(fonts["guidanceButton"], with: builder.setGuidanceButtonFont(_:), size: 0)

    let sizes = livenessTheme["sizes"] as? [String: Any] ?? [:]
    if let width = intThemeValue(sizes["guidanceButtonBorderWidth"]) {
      _ = builder.setGuidanceButtonBorderWidth(width)
    }
    if let radius = intThemeValue(sizes["guidanceButtonCornerRadius"]) {
      _ = builder.setGuidanceButtonBorderCornerRadius(radius)
    }
    if let width = intThemeValue(sizes["guidanceRetryScreenImageBorderWidth"]) {
      _ = builder.setRetryScreenImageBorderWidth(width)
    }
    if let radius = intThemeValue(sizes["guidanceRetryScreenImageCornerRadius"]) {
      _ = builder.setRetryScreenImageBorderCornerRadius(radius)
    }
    if let width = intThemeValue(sizes["frameBorderWidth"]) {
      _ = builder.setFrameBorderWidth(width)
    }
    if let radius = intThemeValue(sizes["frameCornerRadius"]) {
      _ = builder.setFrameBorderCornerRadius(radius)
    }
    if let elevation = intThemeValue(sizes["frameElevation"]) {
      _ = builder.setFrameShadow(shadowFromElevation(elevation))
    }
    if let radius = intThemeValue(sizes["feedbackCornerRadius"]) {
      _ = builder.setFeedbackBarCornerRadius(radius)
    }
    if let elevation = intThemeValue(sizes["feedbackElevation"]) {
      _ = builder.setFeedbackBarShadow(shadowFromElevation(elevation))
    }

    return builder
  }

  private static func customizeLivenessTexts(
    from theme: [String: Any]
  ) -> [Liveness3DTextKey : String] {
    guard let livenessTheme = theme["facetec"] as? [String: Any] else {
      return [:]
    }
    guard let texts = livenessTheme["texts"] as? [String: String] else {
      return [:]
    }
    let keys: [String: Liveness3DTextKey] = [
      "readyHeader1": .readyHeader1,
      "readyHeader2": .readyHeader2,
      "readyMessage1": .readyMessage1,
      "readyMessage2": .readyMessage2,
      "readyButton": .readyButton,
      "feedbackLookStraightInOval": .feedbackPositionFaceStraightInOval,
      "feedbackCenterFace": .feedbackCenterFace,
      "feedbackFaceNotFound": .feedbackFaceNotFound,
      "feedbackFaceNotLookingStraightAhead": .feedbackFaceNotLookingStraightAhead,
      "feedbackFaceNotUpright": .feedbackFaceNotUpright,
      "feedbackHoldSteady": .feedbackHoldSteady,
      "feedbackMovePhoneAway": .feedbackMovePhoneAway,
      "feedbackMovePhoneCloser": .feedbackMovePhoneCloser,
      "feedbackMovePhoneToEyeLevel": .feedbackMovePhoneToEyeLevel,
      "feedbackUseEvenLighting": .feedbackUseEvenLighting,
      "feedbackFrameYourFace": .feedbackFrameYourFace,
      "feedbackHoldSteady1": .feedbackHoldSteady1,
      "feedbackHoldSteady2": .feedbackHoldSteady2,
      "feedbackHoldSteady3": .feedbackHoldSteady3,
      "feedbackRemoveDarkGlasses": .feedbackRemoveDarkGlasses,
      "feedbackNeutralExpression": .feedbackNeutralExpression,
      "feedbackConditionsTooBright": .feedbackConditionsTooBright,
      "feedbackBrightenYourEnvironment": .feedbackBrightenYourEnvironment,
      "resultUploadMessage": .resultUploadMessage,
      "resultSuccessMessage": .resultSuccessMessage,
      "retryHeader": .retryHeader,
      "retrySubheader": .retrySubheader,
      "retryMessageSmile": .retryMessageSmile,
      "retryMessageLighting": .retryMessageLightning,
      "retryMessageContrast": .retryMessageContrast,
      "retryYourPicture": .retryYourPicture,
      "retryIdealPicture": .retryIdealPicture,
      "retryButton": .retryButton,
    ]
    var livenessTexts = [Liveness3DTextKey : String]()

    for (themeKey, textKey) in keys {
      livenessTexts[textKey] = texts[themeKey]
    }

    return livenessTexts
  }

  // MARK: - Utils

  private static func setColor<T>(_ colorHex: String?, with builder: @escaping (UIColor) -> T) {
    if let colorHex, let color = UIColor(hex: colorHex) {
      _ = builder(color)
    }
  }

  private static func setText<T>(_ text: String?, with builder: @escaping (String) -> T) {
    if let text {
      _ = builder(text)
    }
  }

  private static func setImage<T>(_ imageName: String?, with builder: @escaping (UIImage) -> T) {
    if let imageName, let image = RnSdkBundle.getImage(named: imageName) {
      _ = builder(image)
    }
  }

  private static func intThemeValue(_ value: Any?) -> Int? {
    switch value {
    case let intValue as Int:
      return intValue
    case let doubleValue as Double:
      return Int(doubleValue)
    case let numberValue as NSNumber:
      return numberValue.intValue
    default:
      return nil
    }
  }

  private static func shadowFromElevation(_ elevation: Int) -> Liveness3DShadow {
    let opacity = min(Swift.max(Float(elevation) / 24.0, 0.08), 0.4)
    let radius = CGFloat(min(Swift.max(elevation, 1), 24))
    return Liveness3DShadow(
      color: .black,
      opacity: opacity,
      radius: Float(radius),
      offset: CGSize(width: 0, height: CGFloat(elevation) / 2),
      insets: .zero
    )
  }

  private static func firstValue(in source: [String: String], keys: String...) -> String? {
    for key in keys {
      guard let value = source[key]?.trimmingCharacters(in: .whitespacesAndNewlines) else { continue }
      if !value.isEmpty {
        return value
      }
    }
    return nil
  }

  private static func setFont<T>(_ fontName: String?, with builder: @escaping (UIFont) -> T, size: CGFloat) {
    guard let fontName else { return }
    let normalizedSize = size > 0 ? size : UIFont.systemFontSize
    guard let resolvedName = resolveFontName(fontName) else { return }
    guard let font = UIFont(name: resolvedName, size: normalizedSize) else { return }
    _ = builder(font)
  }

  private static func resolveFontName(_ fontName: String) -> String? {
    let trimmed = fontName.trimmingCharacters(in: .whitespacesAndNewlines)
    if UIFont(name: trimmed, size: UIFont.systemFontSize) != nil {
      return trimmed
    }

    let pathComponent = (trimmed as NSString).lastPathComponent
    if UIFont(name: pathComponent, size: UIFont.systemFontSize) != nil {
      return pathComponent
    }

    let baseName = (pathComponent as NSString).deletingPathExtension
    if UIFont(name: baseName, size: UIFont.systemFontSize) != nil {
      return baseName
    }

    let wanted = baseName.lowercased()
    for family in UIFont.familyNames {
      if family.lowercased().contains(wanted) {
        let familyCandidates = UIFont.fontNames(forFamilyName: family)
        if let first = familyCandidates.first {
          return first
        }
      }
      for candidate in UIFont.fontNames(forFamilyName: family) {
        let candidateNormalized = candidate.lowercased()
        if candidateNormalized == wanted || candidateNormalized.contains(wanted) {
          return candidate
        }
      }
    }

    return nil
  }

  private static func applyIProovBaseFont(
    in builder: IProovLivenessCustomizationBuilder,
    with iproovTheme: [String: Any]
  ) -> IProovLivenessCustomizationBuilder {
    guard let baseFont = resolveIProovBaseFont(from: iproovTheme) else {
      return builder
    }

    if let resolvedName = resolveFontName(baseFont) {
      _ = builder.setFont(withName: resolvedName)
      return builder
    }

    let fallbackName = ((baseFont as NSString).lastPathComponent as NSString).deletingPathExtension
    if !fallbackName.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
      _ = builder.setFont(withName: fallbackName)
    }
    return builder
  }

  private static func resolveIProovBaseFont(from iproovTheme: [String: Any]) -> String? {
    if let fontResource = iproovTheme["fontResource"] as? String,
       !fontResource.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
      return fontResource
    }

    guard let fontPath = iproovTheme["fontPath"] as? String else { return nil }
    let trimmedPath = fontPath.trimmingCharacters(in: .whitespacesAndNewlines)
    if trimmedPath.isEmpty { return nil }
    let lastPathComponent = (trimmedPath as NSString).lastPathComponent
    return (lastPathComponent as NSString).deletingPathExtension
  }

  private static func getResultStyleApperance(from colors: [String: String]) -> BlobAnimationAppearance {
    func getColor(from colorHex: String?, defaultColor: UIColor) -> UIColor {
      guard let colorHex, let color = UIColor(hex: colorHex) else {
        return defaultColor
      }
      return color
    }

    let blobColor = getColor(
      from: colors["resultScreenActivityIndicator"],
      defaultColor: .black
    )
    let checkmarkForegroundColor = getColor(
      from: colors["resultScreenResultAnimationForeground"],
      defaultColor: .black
    )
    let checkmarkBackgroundColor = getColor(
      from: colors["resultScreenResultAnimationBackground"],
      defaultColor: .black
    )
    return BlobAnimationAppearance(
      blobColor: blobColor,
      checkmarkForegroundColor: checkmarkForegroundColor,
      checkmarkBackgroundColor: checkmarkBackgroundColor
    )
  }
}
