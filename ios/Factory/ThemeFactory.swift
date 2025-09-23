//
//  ThemeFactory.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 01/08/25.
//

import Foundation
import OIComponents
import OitiSDK
import UIKit

class ThemeFactory {

  static func createIProovCustomization(from theme: [String: Any]?) -> IProovCustomization {
    guard let theme = theme,
      let iproovTheme = theme["iproov"] as? [String: Any]
    else {
      return IProovCustomization.builder().build()
    }

    let colors = iproovTheme["colors"] as? [String: String] ?? [:]
    let texts = iproovTheme["texts"] as? [String: String] ?? [:]

    return IProovCustomization.builder()
      .setInstructionCustomization { instructionBuilder in
        instructionBuilder
          .setTitleText(
            texts["instructionsTitleText"] ?? "Verificação Facial",
            color: UIColor(hex: colors["titleColor"] ?? "#FFFFFF"),
            font: nil
          )
          .setCaptionText(
            texts["instructionsCaptionText"]
              ?? "Siga as instruções para completar a verificação",
            color: UIColor(hex: colors["titleColor"] ?? "#FFFFFF")?.withAlphaComponent(
              0.8),
            font: nil
          )
          .setBackgroundColor(
            UIColor(hex: colors["headerBackgroundColor"] ?? "#121212") ?? UIColor.black
          )
          .setBottomSheetColor(
            UIColor(hex: colors["promptBackgroundColor"] ?? "#1F1F1F")
              ?? UIColor.darkGray
          )
          .setContinueButtonText(
            texts["continueButtonText"] ?? "Começar",
            font: nil
          )
          .setContinueButtonColor(
            forContent: UIColor.white,
            background: UIColor(hex: colors["surroundColor"] ?? "#00FF00"),
            border: UIColor(hex: colors["surroundColor"] ?? "#00FF00")
          )
      }
      .setLivenessCustomization { livenessBuilder in
        livenessBuilder
          .setHeader(
            withText: texts["title"] ?? "Verificação Biométrica",
            textColor: UIColor(hex: colors["titleColor"] ?? "#FFFFFF") ?? UIColor.white,
            backgroundColor: UIColor(hex: colors["headerBackgroundColor"] ?? "#121212")
              ?? UIColor.black
          )
          .setPromptColors(
            forText: UIColor(hex: colors["promptTextColor"] ?? "#FFFFFF")
              ?? UIColor.white,
            backgroundColor: UIColor(hex: colors["promptBackgroundColor"] ?? "#1F1F1F")
              ?? UIColor.darkGray
          )
          .setPromptRoundedCorners(enabled: true)
          .setBackgroundColor(
            UIColor(hex: colors["headerBackgroundColor"] ?? "#121212") ?? UIColor.black
          )
          .setLAOvalStrokeColors(
            forCapturing: UIColor(hex: colors["ovalStrokeColor"] ?? "#FFFFFF")
              ?? UIColor.white,
            completed: UIColor(hex: colors["ovalCompletedColor"] ?? "#00FF00")
              ?? UIColor.green
          )
          .setGPAOvalStrokeColors(
            forNotReady: UIColor(hex: colors["ovalNotReadyColor"] ?? "#FF0000")
              ?? UIColor.red,
            completed: UIColor(hex: colors["ovalReadyColor"] ?? "#00FF00")
              ?? UIColor.green
          )
      }
      .setResultCustomization { resultBuilder in
        resultBuilder
          .setResultMessage(
            texts["successText"] ?? "Verificação realizada com sucesso!",
            forResultType: .success
          )
          .setResultMessage(
            texts["errorText"] ?? "Falha na verificação. Tente novamente.",
            forResultType: .error
          )
      }
      .build()
  }
}
