//
//  RnSdkImpl.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 21/07/25.
//

import CertifaceSDK
import UIKit

@objc public class RnSdkImpl: NSObject {
  var onSuccessCallback: ((String) -> Void)?
  var onErrorCallback: ((String) -> Void)?

  @objc public func startJourney(
    appKey: String,
    environment: String,
    provider: String,
    isCustomEnabled: Bool,
    theme: [String: Any]?,
    onSuccess: @escaping (String) -> Void,
    onError: @escaping (String) -> Void
  ) {
    print(
      "AppKey: \(appKey), Environment: \(environment), Provider: \(provider), CustomEnabled: \(isCustomEnabled)"
    )
    if let themeData = theme {
      print("Theme: \(themeData)")
    }

    self.onSuccessCallback = onSuccess
    self.onErrorCallback = onError

    let sdkEnvironment: CertifaceSDK.Environment
    if environment == "PRD" {
      sdkEnvironment = .prd
    } else {
      sdkEnvironment = .hml
    }

    let livenessProvider: LivenessProvider
    if provider == "FACETEC" {
      livenessProvider = .facetec
    } else if provider == "IPROOV" {
      livenessProvider = .iproov
    } else {
      onError("Invalid provider: \(provider)")
      return
    }

    var facetecCustomization = FacetecCustomization.builder().build()
    var iproovCustomization = IProovCustomization.builder().build()

    var showInstructionsScreen = true

    if isCustomEnabled {
      switch livenessProvider {
      case .facetec:
        facetecCustomization = ThemeFactory.createFacetecCustomization(from: theme)
      case .iproov:
        iproovCustomization = ThemeFactory.createIProovCustomization(from: theme)
      @unknown default:
        break
      }

      if let themeData = theme,
         let instructionsTheme = themeData["instructions"] as? [String: Any],
         let configuration = instructionsTheme["configuration"] as? [String: Any],
         let showInstruction = configuration["showInstructionScreen"] as? Bool {
        showInstructionsScreen = showInstruction
      }
    }

    let options = LivenessManagerOptions
      .builder(appKey: appKey, environment: sdkEnvironment)
      .setShowInstructionsScreen(showInstructionsScreen)
      .setFacetecCustomization(facetecCustomization)
      .setIProovCustomization(iproovCustomization)
      .build()
    let manager = CertifaceSDKFactory.createLivenessManager(for: livenessProvider)

    DispatchQueue.main.async { [weak self] in
      guard let self, let viewController = getRootViewController() else {
        onError("Cannot get rootViewController")
        return
      }

      manager.start(at: viewController, options: options, callback: self)
    }
  }
}
