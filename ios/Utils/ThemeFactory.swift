//
//  ThemeFactory.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 01/08/25.
//

import CertifaceSDK
import CertifaceIProov
import CertifaceFortface
import UIKit

final class ThemeFactory {
  static func validate(_ theme: [String: Any]?, provider: ThemeValidationProvider) throws {
    try ThemeCustomizationValidator.validate(theme, provider: provider)
  }

  static func createIProovCustomization(from theme: [String: Any]?) throws -> IProovCustomization {
    try validate(theme, provider: .iproov)
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

  static func createFacetecCustomization(from theme: [String: Any]?) throws -> FacetecCustomization {
    try validate(theme, provider: .facetec)
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

  static func createSaasCustomization(from theme: [String: Any]?) throws -> SaasCustomization {
    try validate(theme, provider: .facetec)
    let builder = SaasCustomization.builder()
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
      .build()
  }

  static func createFortfaceCustomization(from theme: [String: Any]?) throws -> CertifaceSDK.FortfaceCustomization {
    try validate(theme, provider: .fortface)
    let builder = CertifaceSDK.FortfaceCustomization.builder()
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
      .setVendorCustomization { vendorBuilder in
        customizeFortfaceVendor(in: vendorBuilder, with: theme)
      }
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
    let rawIconSize = CGFloat(doubleThemeValue(assets["instructionIconSize"]) ?? 60)
    let iconSize = min(max(rawIconSize, 16), 256)

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
    let configuration = instructionsTheme["configuration"] as? [String: Any] ?? [:]
    let closeButtonImageName = assets["closeButtonIcon"]
    let closeButtonImage = closeButtonImageName.flatMap { RnSdkBundle.getImage(named: $0) }
    let closeButtonColorHex = firstValue(in: colors, keys: "closeButtonColor", "closeButtonIcon")

    if let closeButtonImage {
      let tintableImage = BackButtonIconComposer.prepare(closeButtonImage)
      _ = builder.setCloseButtonImage(tintableImage)
      setColor(closeButtonColorHex, with: builder.setCloseButtonImageColor(_:))
    } else {
      setColor(closeButtonColorHex, with: builder.setCloseButtonImageColor(_:))
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

    let filterForeground = colors["filterLineDrawingForeground"]
    let filterBackground = colors["filterLineDrawingBackground"]
    let useLineDrawing = resolveIProovUseLineDrawing(
      configuration: configuration,
      filterForeground: filterForeground,
      filterBackground: filterBackground
    )
    _ = builder.setFilterStyle(resolveIProovFilterStyle(configuration: configuration, useLineDrawing: useLineDrawing))
    if useLineDrawing {
      setColor(filterForeground, with: builder.setFilterLineDrawingForegroundColor(_:))
      setColor(filterBackground, with: builder.setFilterLineDrawingBackgroundColor(_:))
    }

    let flags = instructionsTheme["flags"] as? [String: Any] ?? [:]
    let timeoutSecs = intThemeValue(configuration["timeoutSecs"]) ?? 60
    _ = builder.setTimeout(TimeInterval(timeoutSecs))
    _ = builder.setPromptRoundedCornersEnabled(flags["promptRoundedCorners"] as? Bool ?? true)

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
    _ = builder.setSpinnerScaleFactor(resolveIProovSpinnerScaleFactor(from: processingSizes))

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
      CGFloat(doubleThemeValue(processingSizes["spinnerWidth"] ?? processingSizes["loadingIndicatorWidth"]) ?? 10)
    )
    _ = builder.setSpinnerScaleFactor(resolveIProovSpinnerScaleFactor(from: processingSizes))

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
    if let strokeWidth = intThemeValue(sizes["ovalStrokeWidth"]), strokeWidth > 0 {
      _ = builder.setOvalStrokeWidth(strokeWidth)
    }
    setColor(colors["ovalProgressFirst"], with: builder.setOvalProgressFirstColor(_:))
    setColor(colors["ovalProgressSecond"], with: builder.setOvalProgressSecondColor(_:))
    if let progressWidth = intThemeValue(sizes["ovalProgressStrokeWidth"] ?? sizes["ovalProgressWidth"]),
       progressWidth > 0 {
      _ = builder.setOvalProgressWidth(progressWidth)
    }
    if let progressOffset = intThemeValue(sizes["ovalProgressRadialOffset"] ?? sizes["ovalProgressOffset"]),
       progressOffset > 0 {
      _ = builder.setOvalProgressOffset(progressOffset)
    }
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
    setFont(fonts["readyScreenHeader"], with: builder.setReadyScreenHeaderFont(_:), size: 20)
    setFont(fonts["readyScreenSubtext"], with: builder.setReadyScreenMessageFont(_:), size: 16)
    setFont(fonts["resultScreenMessage"], with: builder.setResultScreenMessageFont(_:), size: 16)
    setFont(fonts["retryScreenHeader"], with: builder.setRetryScreenHeaderFont(_:), size: 20)
    setFont(fonts["retryScreenSubtext"], with: builder.setRetryScreenCaptionFont(_:), size: 16)
    setFont(fonts["feedbackMessage"], with: builder.setFeedbackMessageFont(_:), size: 18)
    setFont(fonts["guidanceHeader"], with: builder.setGuidanceHeaderFont(_:), size: 20)
    setFont(fonts["guidanceSubtext"], with: builder.setGuidanceSubtextFont(_:), size: 16)
    setFont(fonts["guidanceButton"], with: builder.setGuidanceButtonFont(_:), size: 16)

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
      guard let value = texts[themeKey]?.trimmingCharacters(in: .whitespacesAndNewlines),
            !value.isEmpty else {
        continue
      }
      livenessTexts[textKey] = value
    }

    return livenessTexts
  }

  // MARK: Fortface

  private static func customizeFortfaceVendor(
    in builder: CertifaceFortface.FortfaceCustomizationBuilder,
    with theme: [String: Any]
  ) -> CertifaceFortface.FortfaceCustomizationBuilder {
    guard let fortfaceTheme = theme["fortface"] as? [String: Any] else {
      return builder
    }

    let colors = fortfaceTheme["colors"] as? [String: String] ?? [:]
    let texts = fortfaceTheme["texts"] as? [String: String] ?? [:]
    let flags = fortfaceTheme["flags"] as? [String: Any] ?? [:]
    let configuration = fortfaceTheme["configuration"] as? [String: String] ?? [:]
    let fonts = fortfaceTheme["fonts"] as? [String: String] ?? [:]
    let assets = fortfaceTheme["assets"] as? [String: String] ?? [:]

    let cancelPositionKey = configuration["cancelPosition"]?.uppercased()
    _ = builder.setCancelButton(
      FortfaceCancelButton(
        position: cancelPositionKey == "RIGHT" ? .right : .left,
        enable: flags["cancelButtonEnable"] as? Bool,
        iconColor: colorFromHex(colors["cancelButton"])
      )
    )

    let screenModeKey = configuration["screenMode"]?.uppercased()
    _ = builder.setScreenMode(screenModeKey == "MODAL" ? .modal : .fullscreen)

    let screenOrientationKey = configuration["screenOrientation"]?.uppercased()
    switch screenOrientationKey {
    case "PORTRAIT":
      _ = builder.setScreenOrientation(.portrait)
    case "LANDSCAPE":
      _ = builder.setScreenOrientation(.landscape)
    default:
      _ = builder.setScreenOrientation(.automatic)
    }

    if let backgroundColor = colorFromHex(colors["cameraBackground"]) {
      _ = builder.setCameraBackground(FortfaceCameraBackground(color: backgroundColor))
    }

    _ = builder.setCameraColor(
      FortfaceCameraColor(
        neutral: colorFromHex(colors["cameraNeutral"]),
        alert: colorFromHex(colors["cameraAlert"]),
        success: colorFromHex(colors["cameraSuccess"]),
        brightness: colorFromHex(colors["cameraBrightnessAlert"]),
        brightnessBackground: colorFromHex(colors["cameraBrightnessAlert"]),
        loadingBackground: colorFromHex(colors["cameraLoading"]),
        loadingStroke: colorFromHex(colors["cameraLoadingStroke"]) ?? .white,
        messageTextColorResource: colorFromHex(colors["cameraMessageText"])
      )
    )

    if let cameraFrameTextVisible = flags["cameraFrameTextVisible"] as? Bool {
      _ = builder.setCameraFrameText(FortfaceCameraFrameText(visible: cameraFrameTextVisible))
    }

    let cameraFont = fonts["cameraMessage"] ?? fonts["cameraFooter"]
    _ = builder.setCameraMessages(
      FortfaceCameraMessages(
        familyFont: cameraFont,
        positioned: texts["cameraFacePositioned"],
        noFace: texts["cameraNoFace"],
        faceNear: texts["cameraFaceNear"],
        faceFar: texts["cameraFaceFar"],
        noFaceYaw: texts["cameraNoFaceYaw"],
        facePitchIsUp: texts["cameraFacePitchUp"],
        facePitchIsDown: texts["cameraFacePitchDown"],
        highBrightness: texts["cameraFaceBrightnessHigh"],
        lowBrightness: texts["cameraFaceBrightnessLow"],
        faceCenterLeft: texts["cameraFaceCenterLeft"],
        faceCenterRight: texts["cameraFaceCenterRight"],
        faceCenterUp: texts["cameraFaceCenterUp"],
        faceCenterDown: texts["cameraFaceCenterDown"],
        faceRollRight: texts["cameraFaceRollRight"],
        faceRollLeft: texts["cameraFaceRollLeft"],
        noFaceRoll: texts["cameraNoFaceRoll"]
      )
    )

    if let logoName = assets["cameraLogo"], let logo = RnSdkBundle.getImage(named: logoName) {
      _ = builder.setCameraLogo(FortfaceCameraLogo(icon: logo, iconSmall: logo))
    }

    return builder
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

  private static func normalizeIProovStyleKey(_ value: String?) -> String? {
    guard let value else { return nil }
    return value
      .trimmingCharacters(in: .whitespacesAndNewlines)
      .lowercased()
      .replacingOccurrences(of: " ", with: "")
      .replacingOccurrences(of: "_", with: "")
  }

  private static func resolveIProovUseLineDrawing(
    configuration: [String: Any],
    filterForeground: String?,
    filterBackground: String?
  ) -> Bool {
    switch normalizeIProovStyleKey(firstValue(in: configuration, keys: "filterStyle")) {
    case "linedrawing":
      return true
    case "natural":
      return false
    default:
      let hasForeground = !(filterForeground?.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ?? true)
      let hasBackground = !(filterBackground?.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ?? true)
      return hasForeground || hasBackground
    }
  }

  private static func resolveIProovFilterStyle(
    configuration: [String: Any],
    useLineDrawing: Bool
  ) -> FilterStyle {
    if useLineDrawing {
      switch normalizeIProovStyleKey(firstValue(in: configuration, keys: "lineDrawingStyle")) {
      case "shaded":
        return .lineDrawing(.shaded)
      case "vibrant":
        return .lineDrawing(.vibrant)
      default:
        return .lineDrawing(.classic)
      }
    }

    switch normalizeIProovStyleKey(firstValue(in: configuration, keys: "naturalStyle")) {
    case "blur":
      return .natural(.blur)
    default:
      return .natural(.clear)
    }
  }

  private static func resolveIProovSpinnerScaleFactor(from processingSizes: [String: Any]) -> Int {
    if let spinnerSize = intThemeValue(processingSizes["spinnerSize"]) {
      return min(max(spinnerSize, 1), 10)
    }
    if let androidSize = intThemeValue(processingSizes["loadingIndicatorSize"]) {
      return min(max(Int((Double(androidSize) / 20.0).rounded()), 1), 10)
    }
    return 5
  }

  private static func setFont<T>(_ fontName: String?, with builder: @escaping (UIFont) -> T, size: CGFloat) {
    guard let fontName else { return }
    let normalizedSize = size > 0 ? size : UIFont.systemFontSize
    guard let resolvedName = FontNameResolver.resolve(fontName) else { return }
    guard let font = UIFont(name: resolvedName, size: normalizedSize) else { return }
    _ = builder(font)
  }

  private static func resolveFontName(_ fontName: String) -> String? {
    FontNameResolver.resolve(fontName)
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

    return BlobAnimationAppearance(
      blobColor: getColor(
        from: colors["resultScreenActivityIndicator"],
        defaultColor: .black
      ),
      checkmarkForegroundColor: getColor(
        from: colors["resultScreenResultAnimationForeground"],
        defaultColor: .white
      ),
      checkmarkBackgroundColor: getColor(
        from: colors["resultScreenResultAnimationBackground"],
        defaultColor: .black
      )
    )
  }
}

enum FontNameResolver {
  private static let lock = NSLock()
  private static var cache: [String: String?] = [:]

  static func resolve(_ fontName: String) -> String? {
    let trimmed = fontName.trimmingCharacters(in: .whitespacesAndNewlines)
    guard !trimmed.isEmpty else { return nil }

    lock.lock()
    if let cached = cache[trimmed] {
      lock.unlock()
      return cached
    }
    lock.unlock()

    let resolved = resolveUncached(trimmed)

    lock.lock()
    cache[trimmed] = resolved
    lock.unlock()
    return resolved
  }

  private static func resolveUncached(_ trimmed: String) -> String? {
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
}
