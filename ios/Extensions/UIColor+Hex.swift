//
//  UIColor+Hex.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 01/08/25.
//

import UIKit

extension UIColor {
  convenience init?(hex: String) {
    let trimmed = hex.trimmingCharacters(in: .whitespacesAndNewlines)
    let hexString = trimmed.hasPrefix("#") ? String(trimmed.dropFirst()) : trimmed

    let scanner = Scanner(string: hexString)
    var color: UInt64 = 0
    guard scanner.scanHexInt64(&color) else {
      return nil
    }

    switch hexString.count {
    case 6:
      let red = Double((color & 0xFF0000) >> 16) / 255.0
      let green = Double((color & 0x00FF00) >> 8) / 255.0
      let blue = Double(color & 0x0000FF) / 255.0
      self.init(red: red, green: green, blue: blue, alpha: 1.0)
    case 8:
      let alpha = Double((color & 0xFF00_0000) >> 24) / 255.0
      let red = Double((color & 0x00FF_0000) >> 16) / 255.0
      let green = Double((color & 0x0000_FF00) >> 8) / 255.0
      let blue = Double(color & 0x0000_00FF) / 255.0
      self.init(red: red, green: green, blue: blue, alpha: alpha)
    default:
      return nil
    }
  }
}
