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
