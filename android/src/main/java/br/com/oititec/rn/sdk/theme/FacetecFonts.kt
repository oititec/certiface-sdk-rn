package br.com.oititec.rn.sdk.theme

import br.com.oiti.manager.exports.FacetecFontsKey
import com.facebook.react.bridge.ReadableMap

class FacetecFonts(private val fontsBuilder: ReadableMap?) {
    
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
    
    private val guidanceCustomizationHeaderFont: String = 
        "fonts/" + (fontsBuilder?.getString("guidanceCustomizationHeaderFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val guidanceCustomizationSubtextFont: String = 
        "fonts/" + (fontsBuilder?.getString("guidanceCustomizationSubtextFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val guidanceCustomizationButtonFont: String = 
        "fonts/" + (fontsBuilder?.getString("guidanceCustomizationButtonFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val guidanceCustomizationReadyScreenHeaderFont: String = 
        "fonts/" + (fontsBuilder?.getString("guidanceCustomizationReadyScreenHeaderFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val guidanceCustomizationReadyScreenSubtextFont: String = 
        "fonts/" + (fontsBuilder?.getString("guidanceCustomizationReadyScreenSubtextFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val guidanceCustomizationRetryScreenHeaderFont: String = 
        "fonts/" + (fontsBuilder?.getString("guidanceCustomizationRetryScreenHeaderFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val guidanceCustomizationRetryScreenSubtextFont: String = 
        "fonts/" + (fontsBuilder?.getString("guidanceCustomizationRetryScreenSubtextFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val resultScreenCustomizationMessageFont: String = 
        "fonts/" + (fontsBuilder?.getString("resultScreenCustomizationMessageFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"
    
    private val feedbackCustomizationTextFont: String = 
        "fonts/" + (fontsBuilder?.getString("feedbackCustomizationTextFont")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    fun apply(): HashMap<FacetecFontsKey, String> {
        return hashMapOf(
            FacetecFontsKey.INSTRUCTIONS_TITLE_FONT to instructionsTitleFont,
            FacetecFontsKey.INSTRUCTIONS_CAPTION_FONT to instructionsCaptionFont,
            FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to instructionsDocumentTypesInstructionsFont,
            FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to instructionsDocumentTipsInstructionsFont,
            FacetecFontsKey.INSTRUCTIONS_BUTTON_FONT to instructionsButtonFont,
            
            FacetecFontsKey.PERMISSION_TITLE_FONT to permissionTitleFont,
            FacetecFontsKey.PERMISSION_CAPTION_FONT to permissionCaptionFont,
            FacetecFontsKey.PERMISSION_BUTTON_FONT to permissionButtonFont,
            
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_HEADER_FONT to guidanceCustomizationHeaderFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_SUBTEXT_FONT to guidanceCustomizationSubtextFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_BUTTON_FONT to guidanceCustomizationButtonFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_HEADER_FONT to guidanceCustomizationReadyScreenHeaderFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_SUBTEXT_FONT to guidanceCustomizationReadyScreenSubtextFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_HEADER_FONT to guidanceCustomizationRetryScreenHeaderFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_SUBTEXT_FONT to guidanceCustomizationRetryScreenSubtextFont,
            FacetecFontsKey.RESULT_SCREEN_CUSTOMIZATION_MESSAGE_FONT to resultScreenCustomizationMessageFont,
            FacetecFontsKey.FEEDBACK_CUSTOMIZATION_TEXT_FONT to feedbackCustomizationTextFont,
        )
    }
}
