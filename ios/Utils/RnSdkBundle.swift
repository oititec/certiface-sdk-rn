//
//  RnSdkBundle.swift
//  RnSdk
//
//  Created by Vitor Souza on 28/10/25.
//

import UIKit

final class RnSdkBundle {
  static func getImage(named imageName: String) -> UIImage? {
    if let resourceBundle = getBundle(),
       let image = UIImage(named: imageName, in: resourceBundle, compatibleWith: nil) {
      return image
    }
    return UIImage(named: imageName, in: Bundle.main, compatibleWith: nil)
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