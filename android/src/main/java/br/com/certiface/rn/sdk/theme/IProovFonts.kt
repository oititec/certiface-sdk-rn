package br.com.certiface.rn.sdk.theme

import br.com.certiface.manager.exports.IProovFontsKey
import com.facebook.react.bridge.ReadableMap

class IProovFonts(private val fontsBuilder: ReadableMap?) {
    
    private val instructionsTitleFont: String =
        fontAssetPath(fontsBuilder?.getString("instructionsTitleFont"))

    private val instructionsCaptionFont: String =
        fontAssetPath(fontsBuilder?.getString("instructionsCaptionFont"))

    private val instructionsDocumentTypesInstructionsFont: String =
        fontAssetPath(fontsBuilder?.getString("instructionsDocumentTypesInstructionsFont"))

    private val instructionsDocumentTipsInstructionsFont: String =
        fontAssetPath(fontsBuilder?.getString("instructionsDocumentTipsInstructionsFont"))

    private val instructionsButtonFont: String =
        fontAssetPath(fontsBuilder?.getString("instructionsButtonFont"))

    private val permissionTitleFont: String = fontAssetPath(fontsBuilder?.getString("permissionTitleFont"))

    private val permissionCaptionFont: String =
        fontAssetPath(fontsBuilder?.getString("permissionCaptionFont"))

    private val permissionButtonFont: String =
        fontAssetPath(fontsBuilder?.getString("permissionButtonFont"))

    private val resultMessageFont: String = fontAssetPath(fontsBuilder?.getString("resultMessageFont"))

    private val resultRetryButtonFont: String =
        fontAssetPath(fontsBuilder?.getString("resultRetryButtonFont"))

    fun apply(): Map<IProovFontsKey, String> {
        return mapOf(
            IProovFontsKey.INSTRUCTIONS_TITLE_FONT to instructionsTitleFont,
            IProovFontsKey.INSTRUCTIONS_CAPTION_FONT to instructionsCaptionFont,
            IProovFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to instructionsDocumentTypesInstructionsFont,
            IProovFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to instructionsDocumentTipsInstructionsFont,
            IProovFontsKey.INSTRUCTIONS_BUTTON_FONT to instructionsButtonFont,
            IProovFontsKey.PERMISSION_TITLE_FONT to permissionTitleFont,
            IProovFontsKey.PERMISSION_CAPTION_FONT to permissionCaptionFont,
            IProovFontsKey.PERMISSION_BUTTON_FONT to permissionButtonFont,
            IProovFontsKey.RESULT_MESSAGE_FONT to resultMessageFont,
            IProovFontsKey.RESULT_RETRY_BUTTON_FONT to resultRetryButtonFont,
        )
    }
}
