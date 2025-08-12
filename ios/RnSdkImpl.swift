//
//  RnSdkImpl.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 21/07/25.
//

import Foundation
import OIComponents
import OitiSDK
import UIKit

// MARK: - UIColor Extension for Hex Support

extension UIColor {
  convenience init?(hex: String) {
    let hexString = hex.trimmingCharacters(in: .whitespacesAndNewlines)
    let scanner = Scanner(
      string: hexString.hasPrefix("#") ? String(hexString.dropFirst()) : hexString)

    var color: UInt64 = 0

    guard scanner.scanHexInt64(&color) else {
      return nil
    }

    if hexString.count == 7 || (hexString.hasPrefix("#") && hexString.count == 7) {
      let red = Double((color & 0xFF0000) >> 16) / 255.0
      let green = Double((color & 0x00FF00) >> 8) / 255.0
      let blue = Double(color & 0x0000FF) / 255.0

      self.init(red: red, green: green, blue: blue, alpha: 1.0)
    } else if hexString.count == 9 || (hexString.hasPrefix("#") && hexString.count == 9) {
      let red = Double((color & 0xFF00_0000) >> 24) / 255.0
      let green = Double((color & 0x00FF_0000) >> 16) / 255.0
      let blue = Double((color & 0x0000_FF00) >> 8) / 255.0
      let alpha = Double(color & 0x0000_00FF) / 255.0

      self.init(red: red, green: green, blue: blue, alpha: alpha)
    } else {
      return nil
    }
  }
}

// MARK: - Theme Factory

class ThemeFactory {

  static func createIProovCustomization(from theme: [String: Any]?) -> IProovCustomization {
    guard let theme = theme else {
      return IProovCustomization.builder().build()
    }

    let iproovTheme = theme["iproov"] as? [String: Any]
    let instructionsTheme = theme["instructions"] as? [String: Any]
    let permissionTheme = theme["permission"] as? [String: Any]

    let iproovColors = iproovTheme?["colors"] as? [String: String] ?? [:]
    let iproovTexts = iproovTheme?["texts"] as? [String: String] ?? [:]

    let instructionsColors = instructionsTheme?["colors"] as? [String: String] ?? [:]
    let instructionsTexts = instructionsTheme?["texts"] as? [String: String] ?? [:]

    let permissionColors = permissionTheme?["colors"] as? [String: String] ?? [:]
    let permissionTexts = permissionTheme?["texts"] as? [String: String] ?? [:]

    return IProovCustomization.builder()
      .setInstructionCustomization { instructionBuilder in
        instructionBuilder
          .setTitleText(
            instructionsTexts["titleText"] ?? iproovTexts["instructionsTitleText"]
              ?? "Verificação Facial",
            color: UIColor(
              hex: instructionsColors["titleColor"] ?? iproovColors["titleColor"] ?? "#FFFFFF"),
            font: nil
          )
          .setCaptionText(
            instructionsTexts["captionText"] ?? iproovTexts["instructionsCaptionText"]
              ?? "Siga as instruções para completar a verificação",
            color: UIColor(
              hex: instructionsColors["captionColor"] ?? iproovColors["titleColor"] ?? "#FFFFFF")?
              .withAlphaComponent(0.8),
            font: nil
          )
          .setBackgroundColor(
            UIColor(
              hex: instructionsColors["backgroundColor"] ?? iproovColors["headerBackgroundColor"]
                ?? "#121212") ?? UIColor.black
          )
          .setBottomSheetColor(
            UIColor(
              hex: instructionsColors["bottomSheetColor"] ?? iproovColors["promptBackgroundColor"]
                ?? "#1F1F1F") ?? UIColor.darkGray
          )
          .setContinueButtonText(
            instructionsTexts["continueButtonText"] ?? iproovTexts["continueButtonText"]
              ?? "Começar",
            font: nil
          )
          .setContinueButtonColor(
            forContent: UIColor(hex: instructionsColors["continueButtonTextColor"] ?? "#FFFFFF")
              ?? UIColor.white,
            background: UIColor(
              hex: instructionsColors["continueButtonColor"] ?? iproovColors["surroundColor"]
                ?? "#00FF00"),
            border: UIColor(
              hex: instructionsColors["continueButtonColor"] ?? iproovColors["surroundColor"]
                ?? "#00FF00")
          )
      }
      .setLivenessCustomization { livenessBuilder in
        livenessBuilder
          .setHeader(
            withText: iproovTexts["title"] ?? "Verificação Biométrica",
            textColor: UIColor(hex: iproovColors["titleColor"] ?? "#FFFFFF") ?? UIColor.white,
            backgroundColor: UIColor(hex: iproovColors["headerBackgroundColor"] ?? "#121212")
              ?? UIColor.black
          )
          .setPromptColors(
            forText: UIColor(hex: iproovColors["promptTextColor"] ?? "#FFFFFF") ?? UIColor.white,
            backgroundColor: UIColor(hex: iproovColors["promptBackgroundColor"] ?? "#1F1F1F")
              ?? UIColor.darkGray
          )
          .setPromptRoundedCorners(enabled: true)
          .setBackgroundColor(
            UIColor(hex: iproovColors["headerBackgroundColor"] ?? "#121212") ?? UIColor.black
          )
          .setLAOvalStrokeColors(
            forCapturing: UIColor(hex: iproovColors["ovalStrokeColor"] ?? "#FFFFFF")
              ?? UIColor.white,
            completed: UIColor(hex: iproovColors["ovalCompletedColor"] ?? "#00FF00")
              ?? UIColor.green
          )
          .setGPAOvalStrokeColors(
            forNotReady: UIColor(hex: iproovColors["ovalNotReadyColor"] ?? "#FF0000")
              ?? UIColor.red,
            completed: UIColor(hex: iproovColors["ovalReadyColor"] ?? "#00FF00") ?? UIColor.green
          )
      }
      .setResultCustomization { resultBuilder in
        let resultTheme = theme["result"] as? [String: Any]
        let resultTexts = resultTheme?["texts"] as? [String: String] ?? [:]

        resultBuilder
          .setResultMessage(
            resultTexts["successText"] ?? iproovTexts["successText"]
              ?? "Verificação realizada com sucesso!",
            forResultType: .success
          )
          .setResultMessage(
            resultTexts["errorText"] ?? iproovTexts["errorText"]
              ?? "Falha na verificação. Tente novamente.",
            forResultType: .error
          )
      }
      .build()
  }
}

// MARK: - RnSdkImpl

@objc public class RnSdkImpl: NSObject {

  var onSuccessCallback: ((String) -> Void)?
  var onErrorCallback: ((String) -> Void)?

  @objc public func testString(
    string: String
  ) {
    print(string)
  }

  @objc public func startJourney(
    appKey: String,
    isCustomEnabled: Bool,
    theme: [String: Any]?,
    onSuccess: @escaping (String) -> Void,
    onError: @escaping (String) -> Void
  ) {
    print("AppKey: \(appKey), CustomEnabled: \(isCustomEnabled)")
    if let themeData = theme {
      print("Theme: \(themeData)")
    }

    self.onSuccessCallback = onSuccess
    self.onErrorCallback = onError

    guard let viewController = getRootViewController() else {
      onError("Cannot get rootViewController")
      return
    }

    let builder =
      LivenessManagerOptions
      .builder(appKey: appKey, environment: .hml)

    if isCustomEnabled {
      let customization = ThemeFactory.createIProovCustomization(from: theme)
      builder.setIProovCustomization(customization)
    }

    let options = builder.build()

    let manager = OitiSDKFactory.createLivenessManager(for: .iproov)
    DispatchQueue.main.async {
      manager.start(at: viewController, options: options, callback: self)
    }
  }

}
