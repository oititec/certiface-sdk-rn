//
//  RnSdkBundle.swift
//  RnSdk
//
//  Created by Vitor Souza on 28/10/25.
//

import UIKit

final class RnSdkBundle {
  static func getImage(named imageName: String) -> UIImage? {
    guard let resourceBundle = getBundle() else { return nil }
    return UIImage(named: imageName, in: resourceBundle, compatibleWith: nil)
  }

  private static func getBundle() -> Bundle? {
    let bundleUrl = Bundle(for: RnSdkBundle.self).url(
      forResource: "RnSdkBundle",
      withExtension: "bundle"
    )
    guard let bundleUrl else { return nil }
    guard let resourceBundle = Bundle(url: bundleUrl) else {
      return nil
    }
    return resourceBundle
  }
}