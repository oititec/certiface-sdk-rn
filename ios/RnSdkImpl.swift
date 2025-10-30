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

    let customization: IProovCustomization
    if isCustomEnabled {
      customization = ThemeFactory.createIProovCustomization(from: theme)
    } else {
      customization = IProovCustomization.builder().build()
    }

    let options = LivenessManagerOptions
      .builder(appKey: appKey, environment: .hml)
      .setIProovCustomization(customization)
      .build()

    let manager = CertifaceSDKFactory.createLivenessManager(for: .iproov)

    DispatchQueue.main.async { [weak self] in
      guard let self, let viewController = getRootViewController() else {
        onError("Cannot get rootViewController")
        return
      }

      manager.start(at: viewController, options: options, callback: self)
    }
  }
}
