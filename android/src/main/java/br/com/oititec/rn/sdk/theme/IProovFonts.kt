package br.com.oititec.rn.sdk.theme

import br.com.oiti.manager.exports.IProovFontsKey
import com.facebook.react.bridge.ReadableMap

class IProovFonts(private val fontsBuilder: ReadableMap?) {
    
    private val instructionsTitleFont: String = 
        "fonts/" + (fontsBuilder?.getString("instructionsTitleFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val instructionsCaptionFont: String = 
        "fonts/" + (fontsBuilder?.getString("instructionsCaptionFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val instructionsDocumentTypesInstructionsFont: String = 
        "fonts/" + (fontsBuilder?.getString("instructionsDocumentTypesInstructionsFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val instructionsDocumentTipsInstructionsFont: String = 
        "fonts/" + (fontsBuilder?.getString("instructionsDocumentTipsInstructionsFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val instructionsButtonFont: String = 
        "fonts/" + (fontsBuilder?.getString("instructionsButtonFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val permissionTitleFont: String = 
        "fonts/" + (fontsBuilder?.getString("permissionTitleFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val permissionCaptionFont: String = 
        "fonts/" + (fontsBuilder?.getString("permissionCaptionFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val permissionButtonFont: String = 
        "fonts/" + (fontsBuilder?.getString("permissionButtonFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val resultMessageFont: String = 
        "fonts/" + (fontsBuilder?.getString("resultMessageFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val resultRetryButtonFont: String = 
        "fonts/" + (fontsBuilder?.getString("resultRetryButtonFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"

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
