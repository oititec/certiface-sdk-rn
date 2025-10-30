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

  // MARK: Oiti

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
    setColor(colors["continueButtonText"], with: builder.setContinueButtonTextColor(_:))
    setColor(colors["continueButtonBackground"], with: builder.setContinueButtonBackgroundColor(_:))
    setColor(colors["continueButtonBorder"], with: builder.setContinueButtonBorderColor(_:))

    let texts = instructionsTheme["texts"] as? [String: String] ?? [:]
    setText(texts["title"], with: builder.setTitle(_:))
    setText(texts["caption"], with: builder.setCaption(_:))
    setText(texts["firstInstruction"], with: builder.setFirstInstructionTitle(_:))
    setText(texts["secondInstruction"], with: builder.setSecondInstructionTitle(_:))
    setText(texts["continueButton"], with: builder.setContinueButtonText(_:))

    let assets = instructionsTheme["assets"] as? [String: String] ?? [:]
    setImage(assets["backButtonIcon"], with: builder.setBackButtonIcon(_:))
    setImage(assets["contextImage"], with: builder.setContextImage(_:))
    setImage(assets["firstInstructionIcon"], with: builder.setFirstInstructionIcon(_:))
    setImage(assets["secondInstructionIcon"], with: builder.setSecondInstructionIcon(_:))

    let fonts = instructionsTheme["fonts"] as? [String: String] ?? [:]
    setFont(fonts["title"], with: builder.setTitleFont(_:), size: 20)
    setFont(fonts["caption"], with: builder.setCaptionFont(_:), size: 20)
    setFont(fonts["firstInstructionTitle"], with: builder.setFirstInstructionTitleFont(_:), size: 20)
    setFont(fonts["secondInstructionTitle"], with: builder.setSecondInstructionTitleFont(_:), size: 20)
    setFont(fonts["continueButton"], with: builder.setContinueButtonFont(_:), size: 20)

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

    let fonts = cameraPermissionTheme["fonts"] as? [String: String] ?? [:]
    setFont(fonts["title"], with: builder.setTitleFont(_:), size: 20)
    setFont(fonts["caption"], with: builder.setCaptionFont(_:), size: 20)
    setFont(fonts["checkPermissionButton"], with: builder.setCheckPermissionButtonTextFont(_:), size: 20)
    setFont(fonts["bottomSheetTitle"], with: builder.setBottomSheetTitleFont(_:), size: 20)
    setFont(fonts["bottomSheetCaption"], with: builder.setBottomSheetCaptionFont(_:), size: 20)
    setFont(fonts["opentSettingsButton"], with: builder.setOpenSettingsButtonTextFont(_:), size: 20)
    setFont(fonts["closeButton"], with: builder.setCloseButtonTextFont(_:), size: 20)

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
    setColor(colors["closeButtonIcon"], with: builder.setCloseButtonImageColor(_:))
    setColor(colors["title"], with: builder.setTitleTextColor(_:))
    setColor(colors["titleBackground"], with: builder.setTitleBackgroundColor(_:))
    setColor(colors["promptText"], with: builder.setPromptTextColor(_:))
    setColor(colors["promptBackground"], with: builder.setPromptBackgroundColor(_:))
    setColor(colors["background"], with: builder.setBackgroundColor(_:))
    setColor(colors["ovalReady"], with: builder.setGPAOvalStrokeReadyColor(_:))
    setColor(colors["ovalNotReady"], with: builder.setGPAOvalStrokeNotReadyColor(_:))
    setColor(colors["ovalCapturing"], with: builder.setLAOvalStrokeCapturingColor(_:))
    setColor(colors["ovalCompleted"], with: builder.setLAOvalStrokeCompletedColor(_:))
    setColor(colors["filterLineDrawingForeground"], with: builder.setFilterLineDrawingForegroundColor(_:))
    setColor(colors["filterLineDrawingBackground"], with: builder.setFilterLineDrawingBackgroundColor(_:))

    let texts = instructionsTheme["texts"] as? [String: String] ?? [:]
    setText(texts["title"], with: builder.setTitle(_:))

    let assets = instructionsTheme["assets"] as? [String: String] ?? [:]
    setImage(assets["closeButtonIcon"], with: builder.setCloseButtonImage(_:))
    setImage(assets["logoImage"], with: builder.setLogoImage(_:))

    return builder
  }

  private static func customizeLoadingIProov(
    in builder: IProovLoadingCustomizationBuilder,
    with theme: [String: Any]
  ) -> IProovLoadingCustomizationBuilder {
    guard let loadingTheme = theme["processing"] as? [String: Any] else {
       return builder
    }

    let colors = loadingTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["background"], with: builder.setBackgroundColor)
    setColor(colors["loading"], with: builder.setSpinnerColor)

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

    let fonts = resultTheme["fonts"] as? [String: String] ?? [:]
    setFont(fonts["text"], with: builder.setMessageFont(_:), size: 20)
    setFont(fonts["retryButton"], with: builder.setRetryButtonTextFont(_:), size: 20)

    return builder
  }

  // MARK: Facetec

  private static func customizeLoadingFacetec(
    in builder: FacetecLoadingCustomizationBuilder,
    with theme: [String: Any]
  ) -> FacetecLoadingCustomizationBuilder {
    guard let loadingTheme = theme["processing"] as? [String: Any] else {
       return builder
    }

    let colors = loadingTheme["colors"] as? [String: String] ?? [:]
    setColor(colors["background"], with: builder.setBackgroundColor)
    setColor(colors["loading"], with: builder.setSpinnerColor)

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
//    setColor(colors["titleColor"], with: builder.setTitleTextColor(_:))

    let texts = livenessTheme["texts"] as? [String: String] ?? [:]
//    setText(texts["title"], with: builder.setTitle(_:))

    let assets = livenessTheme["assets"] as? [String: String] ?? [:]
//    setImage(assets["logo"], with: builder.setLogoImage(_:))

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

  private static func setFont<T>(_ fontName: String?, with builder: @escaping (UIFont) -> T, size: CGFloat) {
    if let fontName, let font = UIFont(name: fontName, size: size) {
      _ = builder(font)
    }
  }
}
