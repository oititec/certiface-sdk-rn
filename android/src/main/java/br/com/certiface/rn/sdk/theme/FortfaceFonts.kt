package br.com.certiface.rn.sdk.theme

import br.com.certiface.manager.exports.FortfaceFontsKey
import com.facebook.react.bridge.ReadableMap

class FortfaceFonts(
  private val instructionsFonts: ReadableMap?,
  private val permissionFonts: ReadableMap?,
  private val fortfaceFonts: ReadableMap?
) {
  private val instructionsTitleFont: String = fontAssetPath(instructionsFonts?.getString("title"))
  private val instructionsCaptionFont: String = fontAssetPath(instructionsFonts?.getString("caption"))
  private val instructionsFirstInstructionTitleFont: String =
    fontAssetPath(instructionsFonts?.getString("firstInstructionTitle"))
  private val instructionsSecondInstructionTitleFont: String =
    fontAssetPath(instructionsFonts?.getString("secondInstructionTitle"))
  private val instructionsContinueButtonFont: String =
    fontAssetPath(instructionsFonts?.getString("continueButton"))

  private val permissionTitleFont: String = fontAssetPath(permissionFonts?.getString("title"))
  private val permissionCaptionFont: String = fontAssetPath(permissionFonts?.getString("caption"))
  private val permissionButtonFont: String =
    fontAssetPath(permissionFonts?.getString("checkPermissionButton"))

  private val cameraMessageFont: String =
    fontAssetPath(fortfaceFonts?.getString("cameraMessage"))
  private val cameraFooterFont: String =
    fontAssetPath(fortfaceFonts?.getString("cameraFooter"))

  fun apply(): HashMap<FortfaceFontsKey, String> {
    return hashMapOf(
      FortfaceFontsKey.INSTRUCTIONS_TITLE_FONT to instructionsTitleFont,
      FortfaceFontsKey.INSTRUCTIONS_CAPTION_FONT to instructionsCaptionFont,
      FortfaceFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to instructionsFirstInstructionTitleFont,
      FortfaceFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to instructionsSecondInstructionTitleFont,
      FortfaceFontsKey.INSTRUCTIONS_BUTTON_FONT to instructionsContinueButtonFont,
      FortfaceFontsKey.PERMISSION_TITLE_FONT to permissionTitleFont,
      FortfaceFontsKey.PERMISSION_CAPTION_FONT to permissionCaptionFont,
      FortfaceFontsKey.PERMISSION_BUTTON_FONT to permissionButtonFont,
      FortfaceFontsKey.SDK_CAMERA_MESSAGE_FONT to cameraMessageFont,
      FortfaceFontsKey.SDK_CAMERA_FOOTER_FONT to cameraFooterFont
    )
  }
}
