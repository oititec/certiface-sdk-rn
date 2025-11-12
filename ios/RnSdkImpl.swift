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

    let optionsBuilder = LivenessManagerOptions.builder(appKey: appKey, environment: sdkEnvironment)

    if livenessProvider == .iproov {
      let customization: IProovCustomization
      if isCustomEnabled {
        customization = ThemeFactory.createIProovCustomization(from: theme)
      } else {
        customization = IProovCustomization.builder().build()
      }
      optionsBuilder.setIProovCustomization(customization)
    } else {
      let customization: FacetecCustomization
      if isCustomEnabled {
        customization = ThemeFactory.createFacetecCustomization(from: theme)
      } else {
        customization = FacetecCustomization.builder().build()
      }
      optionsBuilder.setFacetecCustomization(customization)
    }

    let options = optionsBuilder.build()
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
