//
//  UIColor+Hex.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 01/08/25.
//

import Foundation
import UIKit

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
