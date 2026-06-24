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
    let assets = instructionsTheme["assets"] as? [String: Any] ?? [:]
    let assetStrings = assets.compactMapValues { $0 as? String }

    setColor(
      firstValue(in: colors, keys: "background", "backgroundColor", "statusBar", "statusBarColor"),
      with: builder.setBackgroundColor(_:)
    )
    setColor(firstValue(in: colors, keys: "bottomSheet", "bottomSheetColor"), with: builder.setBottomSheetColor(_:))
    let instSizes = instructionsTheme["sizes"] as? [String: Any] ?? [:]
    _ = builder.setBottomSheetCornerRadius(CGFloat(doubleThemeValue(instSizes["bottomSheetCornerRadius"]) ?? 16))

    resolveBackButtonTintColor(
      colors: colors,
      hasCustomBackButtonImage: assetStrings["backButtonIcon"] != nil
    ).map { builder.setBackButtonIconColor($0) }

    setColor(firstValue(in: colors, keys: "title", "titleColor"), with: builder.setTitleColor(_:))
    setColor(firstValue(in: colors, keys: "caption", "captionColor"), with: builder.setCaptionColor(_:))
    setColor(
      firstValue(in: colors, keys: "firstInstructionTitle", "firstInstructionTextColor"),
      with: builder.setFirstInstructionTitleColor(_:)
    )
    setColor(
      firstValue(in: colors, keys: "secondInstructionTitle", "secondInstructionTextColor"),
      with: builder.setSecondInstructionTitleColor(_:)
    )
    setColor(
      firstValue(in: colors, keys: "continueButtonTextColor", "continueButtonText"),
      with: builder.setContinueButtonTextColor(_:)
    )
    setColor(
      firstValue(in: colors, keys: "continueButtonBackground", "continueButtonColor"),
      with: builder.setContinueButtonBackgroundColor(_:)
    )
    setColor(colors["continueButtonBorder"], with: builder.setContinueButtonBorderColor(_:))

    let texts = instructionsTheme["texts"] as? [String: String] ?? [:]
    setText(firstValue(in: texts, keys: "title", "titleText"), with: builder.setTitle(_:))
    setText(firstValue(in: texts, keys: "caption", "captionText"), with: builder.setCaption(_:))
    setText(
      firstValue(in: texts, keys: "firstInstruction", "firstInstructionText"),
      with: builder.setFirstInstructionTitle(_:)
    )
    setText(
      firstValue(in: texts, keys: "secondInstruction", "secondInstructionText"),
      with: builder.setSecondInstructionTitle(_:)
    )
    setText(
      firstValue(in: texts, keys: "continueButton", "continueButtonText"),
      with: builder.setContinueButtonText(_:)
    )

    setBackButtonImage(assetStrings["backButtonIcon"], with: builder.setBackButtonIcon(_:))
    setImage(assetStrings["contextImage"], with: builder.setContextImage(_:))

    let iconScale = InstructionIconScaleMode.from(assets["instructionIconScale"] as? String)
    let iconSize = CGFloat(doubleThemeValue(assets["instructionIconSize"]) ?? 60)

    setInstructionIcon(
      assetStrings["firstInstructionIcon"],
      backgroundHex: firstValue(
        in: colors,
        keys: "firstInstructionIconBackground", "firstInstructionIconBackgroundColor"
      ),
      borderHex: firstValue(
        in: colors,
        keys: "firstInstructionIconBorder", "firstInstructionIconBorderColor"
      ),
      scaleMode: iconScale,
      size: iconSize,
      with: builder.setFirstInstructionIcon(_:)
    )
    setInstructionIcon(
      assetStrings["secondInstructionIcon"],
      backgroundHex: firstValue(
        in: colors,
        keys: "secondInstructionIconBackground", "secondInstructionIconBackgroundColor"
      ),
      borderHex: firstValue(
        in: colors,
        keys: "secondInstructionIconBorder", "secondInstructionIconBorderColor"
      ),
      scaleMode: iconScale,
      size: iconSize,
      with: builder.setSecondInstructionIcon(_:)
    )

    let instFonts = instructionsTheme["fonts"] as? [String: String] ?? [:]
    let iproovTheme = theme["iproov"] as? [String: Any] ?? [:]
    let iproovFonts = iproovTheme["fonts"] as? [String: String] ?? [:]
    let iproovFontResource = resolveIProovBaseFont(from: iproovTheme)
    setFont(instFonts["title"] ?? iproovFonts["instructionsTitleFont"] ?? iproovFontResource, with: builder.setTitleFont(_:), size: CGFloat(doubleThemeValue(instSizes["titleFontSize"]) ?? 20))
    setFont(instFonts["caption"] ?? iproovFonts["instructionsCaptionFont"] ?? iproovFontResource, with: builder.setCaptionFont(_:), size: CGFloat(doubleThemeValue(instSizes["captionFontSize"]) ?? 20))
    setFont(
      instFonts["firstInstructionTitle"] ?? iproovFonts["instructionsDocumentTypesInstructionsFont"] ?? iproovFontResource,
      with: builder.setFirstInstructionTitleFont(_:),
      size: CGFloat(doubleThemeValue(instSizes["firstInstructionTitleFontSize"]) ?? 16)
    )
    setFont(
      instFonts["secondInstructionTitle"] ?? iproovFonts["instructionsDocumentTipsInstructionsFont"] ?? iproovFontResource,
      with: builder.setSecondInstructionTitleFont(_:),
      size: CGFloat(doubleThemeValue(instSizes["secondInstructionTitleFontSize"]) ?? 16)
    )
    setFont(
      instFonts["continueButton"] ?? iproovFonts["instructionsButtonFont"] ?? iproovFontResource,
      with: builder.setContinueButtonFont(_:),
      size: CGFloat(doubleThemeValue(instSizes["continueButtonFontSize"]) ?? 20)
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
    setColor(
      firstValue(in: colors, keys: "background", "backgroundColor", "statusBar", "statusBarColor"),
      with: builder.setBackgroundColor(_:)
    )
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
    setBackButtonImage(assets["backButtonIcon"], with: builder.setBackButtonIcon(_:))
    setImage(assets["cameraImage"], with: builder.setCameraImage(_:))

    let permFonts = cameraPermissionTheme["fonts"] as? [String: String] ?? [:]
    let permSizes = cameraPermissionTheme["sizes"] as? [String: Any] ?? [:]
    let iproovTheme = theme["iproov"] as? [String: Any] ?? [:]
    let iproovFonts = iproovTheme["fonts"] as? [String: String] ?? [:]
    let iproovFontResource = resolveIProovBaseFont(from: iproovTheme)
    setFont(permFonts["title"] ?? iproovFonts["permissionTitleFont"] ?? iproovFontResource, with: builder.setTitleFont(_:), size: CGFloat(doubleThemeValue(permSizes["titleFontSize"]) ?? 20))
    setFont(permFonts["caption"] ?? iproovFonts["permissionCaptionFont"] ?? iproovFontResource, with: builder.setCaptionFont(_:), size: CGFloat(doubleThemeValue(permSizes["captionFontSize"]) ?? 20))
    setFont(
      permFonts["checkPermissionButton"] ?? iproovFonts["permissionButtonFont"] ?? iproovFontResource,
      with: builder.setCheckPermissionButtonTextFont(_:),
      size: CGFloat(doubleThemeValue(permSizes["checkPermissionButtonFontSize"]) ?? 20)
    )
    setFont(
      permFonts["bottomSheetTitle"] ?? iproovFonts["permissionTitleFont"] ?? iproovFontResource,
      with: builder.setBottomSheetTitleFont(_:),
      size: CGFloat(doubleThemeValue(permSizes["bottomSheetTitleFontSize"]) ?? 20)
    )
    setFont(
      permFonts["bottomSheetCaption"] ?? iproovFonts["permissionCaptionFont"] ?? iproovFontResource,
      with: builder.setBottomSheetCaptionFont(_:),
      size: CGFloat(doubleThemeValue(permSizes["bottomSheetCaptionFontSize"]) ?? 20)
    )
    setFont(
      permFonts["openSettingsButton"] ?? permFonts["opentSettingsButton"] ?? iproovFonts["permissionButtonFont"] ?? iproovFontResource,
      with: builder.setOpenSettingsButtonTextFont(_:),
      size: CGFloat(doubleThemeValue(permSizes["openSettingsButtonFontSize"]) ?? 20)
    )
    setFont(
      permFonts["closeButton"] ?? iproovFonts["permissionButtonFont"] ?? iproovFontResource,
      with: builder.setCloseButtonTextFont(_:),
      size: CGFloat(doubleThemeValue(permSizes["closeButtonFontSize"]) ?? 20)
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
    let assets = instructionsTheme["assets"] as? [String: String] ?? [:]
    let closeButtonImageName = assets["closeButtonIcon"]
    let hasCloseButtonImage = closeButtonImageName.flatMap { RnSdkBundle.getImage(named: $0) } != nil

    if hasCloseButtonImage {
      setImage(closeButtonImageName, with: builder.setCloseButtonImage(_:))
    } else {
      setColor(
        firstValue(in: colors, keys: "closeButtonColor", "closeButtonIcon"),
        with: builder.setCloseButtonImageColor(_:)
      )
    }

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
    setColor(
      firstValue(in: colors, keys: "background", "backgroundColor", "statusBar", "statusBarColor"),
      with: builder.setBackgroundColor(_:)
    )
    setColor(firstValue(in: colors, keys: "loading", "loadingDialogColor"), with: builder.setSpinnerColor(_:))
    let processingSizes = loadingTheme["sizes"] as? [String: Any] ?? [:]
    _ = builder.setSpinnerWidth(
      CGFloat(doubleThemeValue(processingSizes["spinnerWidth"] ?? processingSizes["loadingIndicatorWidth"]) ?? 10)
    )
    _ = builder.setSpinnerScaleFactor(
      intThemeValue(processingSizes["spinnerSize"] ?? processingSizes["loadingIndicatorSize"]) ?? 100
    )

    return builder
  }

  private static func customizeResultIProov(
    in builder: IProovResultCustomizationBuilder,
    with theme: [String: Any]
  ) -> IProovResultCustomizationBuilder {
    guard let resultTheme = theme["result"] as? [String: Any] else {
       return builder
    }

    let colorsMap = resultTheme["colors"] as? [String: Any] ?? [:]
    setColor(
      firstValue(in: colorsMap, keys: "successBackground", "successBackgroundColor"),
      with: builder.setSuccessBackgroundColor(_:)
    )
    setColor(
      firstValue(in: colorsMap, keys: "successText", "successTextColor"),
      with: builder.setSuccessMessageColor(_:)
    )
    _ = IProovResultBackgroundPatcher.applyErrorAndRetryBackgrounds(
      to: builder,
      errorBackground: colorFromHex(
        firstValue(in: colorsMap, keys: "errorBackground", "errorBackgroundColor")
      ),
      retryBackground: colorFromHex(
        firstValue(in: colorsMap, keys: "retryBackground", "retryBackgroundColor")
      )
    )
    setColor(
      firstValue(in: colorsMap, keys: "errorText", "errorTextColor"),
      with: builder.setErrorMessageColor(_:)
    )
    setColor(
      firstValue(in: colorsMap, keys: "retryText", "retryTextColor"),
      with: builder.setRetryMessageColor(_:)
    )
    setColor(
      firstValue(
        in: colorsMap,
        keys: "retryButtonText",
        "retryButtonTextColor",
        "retryTextColor"
      ),
      with: builder.setRetryButtonTextColor(_:)
    )
    setColor(
      firstValue(in: colorsMap, keys: "retryButtonBackground", "retryButtonColor"),
      with: builder.setRetryButtonBackgroundColor(_:)
    )
    setColor(
      firstValue(in: colorsMap, keys: "retryButtonBorder", "retryButtonBorderColor"),
      with: builder.setRetryButtonBorderColor(_:)
    )

    let textsMap = resultTheme["texts"] as? [String: Any] ?? [:]
    setText(firstValue(in: textsMap, keys: "success", "successText"), with: builder.setSuccessMessage(_:))
    setText(firstValue(in: textsMap, keys: "error", "errorText"), with: builder.setErrorMessage(_:))
    setText(
      firstValue(in: textsMap, keys: "retryButton", "retryButtonText"),
      with: builder.setRetryButtonText(_:)
    )

    let assetsMap = resultTheme["assets"] as? [String: Any] ?? [:]
    setImage(firstValue(in: assetsMap, keys: "successImage"), with: builder.setSuccessImage(_:))
    setImage(firstValue(in: assetsMap, keys: "errorImage"), with: builder.setErrorImage(_:))
    setImage(firstValue(in: assetsMap, keys: "retryImage"), with: builder.setRetryImage(_:))

    let resultFontsMap = resultTheme["fonts"] as? [String: Any] ?? [:]
    let resultSizes = resultTheme["sizes"] as? [String: Any] ?? [:]
    let iproovTheme = theme["iproov"] as? [String: Any] ?? [:]
    let iproovFonts = iproovTheme["fonts"] as? [String: String] ?? [:]
    let iproovFontResource = resolveIProovBaseFont(from: iproovTheme)
    setFont(
      firstValue(in: resultFontsMap, keys: "text")
        ?? iproovFonts["resultMessageFont"]
        ?? iproovFontResource,
      with: builder.setMessageFont(_:),
      size: CGFloat(doubleThemeValue(resultSizes["textFontSize"]) ?? 20)
    )
    setFont(
      firstValue(in: resultFontsMap, keys: "retryButton")
        ?? iproovFonts["resultRetryButtonFont"]
        ?? iproovFontResource,
      with: builder.setRetryButtonTextFont(_:),
      size: CGFloat(doubleThemeValue(resultSizes["retryButtonFontSize"]) ?? 20)
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
    setColor(
      firstValue(in: colors, keys: "background", "backgroundColor", "statusBar", "statusBarColor"),
      with: builder.setBackgroundColor(_:)
    )
    setColor(firstValue(in: colors, keys: "loading", "loadingDialogColor"), with: builder.setSpinnerColor(_:))
    let processingSizes = loadingTheme["sizes"] as? [String: Any] ?? [:]
    _ = builder.setSpinnerWidth(
      CGFloat(doubleThemeValue(processingSizes["spinnerWidth"] ?? processingSizes["loadingIndicatorWidth"]) ?? 80)
    )
    _ = builder.setSpinnerScaleFactor(
      intThemeValue(processingSizes["spinnerSize"] ?? processingSizes["loadingIndicatorSize"]) ?? 80
    )

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
    let sizes = livenessTheme["sizes"] as? [String: Any] ?? [:]
    let configuration = livenessTheme["configuration"] as? [String: String] ?? [:]
    setColor(colors["readyScreenHeader"], with: builder.setReadyScreenHeaderColor(_:))
    setColor(colors["readyScreenSubtext"], with: builder.setReadyScreenMessageColor(_:))
    setColor(colors["readyScreenTextBackground"], with: builder.setReadyScreenTextBackgroundColor(_:))
    setColor(
      firstValue(in: colors, keys: "resultScreenMessage", "resultScreenForeground"),
      with: builder.setResultScreenMessageColor(_:)
    )
    setColor(
      firstValue(in: colors, keys: "resultScreenUploadProgressBarFill", "resultScreenUploadProgressFill"),
      with: builder.setResultScreenUploadProgressBarFillColor(_:)
    )
    setColor(
      firstValue(in: colors, keys: "resultScreenUploadProgressBarTrack", "resultScreenUploadProgressTrack"),
      with: builder.setResultScreenUploadProgressBarTrackColor(_:)
    )
    builder.setResultScreenAnimationStyle(.blob(appearance: getResultStyleApperance(from: colors)))
    setColor(
      firstValue(in: colors, keys: "retryScreenHeader") ?? "#FF5252",
      with: builder.setRetryScreenHeaderColor(_:)
    )
    setColor(
      firstValue(in: colors, keys: "retryScreenSubtext") ?? "#DD3333",
      with: builder.setRetryScreenCaptionColor(_:)
    )
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
    _ = builder.setOvalStrokeWidth(intThemeValue(sizes["ovalStrokeWidth"]) ?? 4)
    setColor(colors["ovalProgressFirst"], with: builder.setOvalProgressFirstColor(_:))
    setColor(colors["ovalProgressSecond"], with: builder.setOvalProgressSecondColor(_:))
    _ = builder.setOvalProgressWidth(intThemeValue(sizes["ovalProgressStrokeWidth"] ?? sizes["ovalProgressWidth"]) ?? 6)
    _ = builder.setOvalProgressOffset(intThemeValue(sizes["ovalProgressRadialOffset"] ?? sizes["ovalProgressOffset"]) ?? 8)
    setColor(colors["overlayBackground"], with: builder.setOverlayBackgroundColor(_:))

    let flags = livenessTheme["flags"] as? [String: Any] ?? [:]
    let showBrandingImage = flags["overlayShowBrandingImage"] as? Bool ?? true
    let assets = livenessTheme["assets"] as? [String: String] ?? [:]
    if showBrandingImage {
      setImage(assets["overlayBrandImage"], with: builder.setOverlayBrandImage(_:))
    }
    let cancelButtonLocationKey = configuration["cancelButtonLocation"]?
      .uppercased()
      .replacingOccurrences(of: " ", with: "_")
    switch cancelButtonLocationKey {
    case "TOP_RIGHT", "TOPRIGHT":
      _ = builder.setCancelButtonLocation(.topRight)
    default:
      _ = builder.setCancelButtonLocation(.topLeft)
    }
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

    if let width = intThemeValue(sizes["guidanceButtonBorderWidth"]) {
      _ = builder.setGuidanceButtonBorderWidth(width)
    }
    if let radius = intThemeValue(sizes["guidanceButtonCornerRadius"]) {
      _ = builder.setGuidanceButtonBorderCornerRadius(radius)
    }
    if let width = intThemeValue(sizes["frameBorderWidth"]) {
      _ = builder.setFrameBorderWidth(Swift.max(width, 4))
    }
    if let radius = intThemeValue(sizes["frameCornerRadius"]) {
      _ = builder.setFrameBorderCornerRadius(radius)
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
    if let color = colorFromHex(colorHex) {
      _ = builder(color)
    }
  }

  private static func colorFromHex(_ colorHex: String?) -> UIColor? {
    guard let colorHex else { return nil }
    return UIColor(hex: colorHex)
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

  private static func setBackButtonImage<T>(_ imageName: String?, with builder: @escaping (UIImage) -> T) {
    if let imageName, let image = RnSdkBundle.getImage(named: imageName) {
      _ = builder(BackButtonIconComposer.prepare(image))
    }
  }

  private static func doubleThemeValue(_ value: Any?) -> Double? {
    switch value {
    case let doubleValue as Double:
      return doubleValue
    case let intValue as Int:
      return Double(intValue)
    case let numberValue as NSNumber:
      return numberValue.doubleValue
    default:
      return nil
    }
  }

  private static func resolveBackButtonTintColor(
    colors: [String: String],
    hasCustomBackButtonImage: Bool
  ) -> UIColor? {
    if let explicit = firstValue(in: colors, keys: "backButtonColor", "backButtonIconColor") {
      return UIColor(hex: explicit)
    }
    if hasCustomBackButtonImage {
      return nil
    }
    guard let fallback = firstValue(in: colors, keys: "backButtonIcon") else {
      return nil
    }
    return UIColor(hex: fallback)
  }

  private static func setInstructionIcon<T>(
    _ imageName: String?,
    backgroundHex: String?,
    borderHex: String?,
    scaleMode: InstructionIconScaleMode,
    size: CGFloat,
    with builder: @escaping (UIImage) -> T
  ) {
    guard let imageName, let icon = RnSdkBundle.getImage(named: imageName) else { return }

    if let backgroundHex, let backgroundColor = UIColor(hex: backgroundHex) {
      let borderColor = borderHex.flatMap { UIColor(hex: $0) }
      let composed = InstructionIconComposer.compose(
        icon: icon,
        backgroundColor: backgroundColor,
        borderColor: borderColor,
        size: size,
        scaleMode: scaleMode
      )
      _ = builder(composed)
      return
    }

    _ = builder(icon)
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

  private static func firstValue(in source: [String: Any], keys: String...) -> String? {
    for key in keys {
      guard let value = source[key] as? String else { continue }
      let trimmed = value.trimmingCharacters(in: .whitespacesAndNewlines)
      if !trimmed.isEmpty {
        return trimmed
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
