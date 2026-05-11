package br.com.certiface.rn.sdk.theme

import br.com.certiface.manager.exports.FacetecFontsKey
import com.facebook.react.bridge.ReadableMap

class FacetecFonts(
    private val instructionsFonts: ReadableMap?,
    private val permissionFonts: ReadableMap?,
    private val facetecFonts: ReadableMap?
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

    private val guidanceHeaderFont: String = fontAssetPath(facetecFonts?.getString("guidanceHeader"))

    private val guidanceSubtextFont: String = fontAssetPath(facetecFonts?.getString("guidanceSubtext"))

    private val guidanceButtonFont: String = fontAssetPath(facetecFonts?.getString("guidanceButton"))

    private val readyScreenHeaderFont: String = fontAssetPath(facetecFonts?.getString("readyScreenHeader"))

    private val readyScreenSubtextFont: String = fontAssetPath(facetecFonts?.getString("readyScreenSubtext"))

    private val retryScreenHeaderFont: String = fontAssetPath(facetecFonts?.getString("retryScreenHeader"))

    private val retryScreenSubtextFont: String = fontAssetPath(facetecFonts?.getString("retryScreenSubtext"))

    private val resultScreenMessageFont: String =
        fontAssetPath(facetecFonts?.getString("resultScreenMessage"))

    private val feedbackMessageFont: String = fontAssetPath(facetecFonts?.getString("feedbackMessage"))

    fun apply(): HashMap<FacetecFontsKey, String> {
        return hashMapOf(
            FacetecFontsKey.INSTRUCTIONS_TITLE_FONT to instructionsTitleFont,
            FacetecFontsKey.INSTRUCTIONS_CAPTION_FONT to instructionsCaptionFont,
            FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to instructionsFirstInstructionTitleFont,
            FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to instructionsSecondInstructionTitleFont,
            FacetecFontsKey.INSTRUCTIONS_BUTTON_FONT to instructionsContinueButtonFont,
            
            FacetecFontsKey.PERMISSION_TITLE_FONT to permissionTitleFont,
            FacetecFontsKey.PERMISSION_CAPTION_FONT to permissionCaptionFont,
            FacetecFontsKey.PERMISSION_BUTTON_FONT to permissionButtonFont,
            
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_HEADER_FONT to guidanceHeaderFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_SUBTEXT_FONT to guidanceSubtextFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_BUTTON_FONT to guidanceButtonFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_HEADER_FONT to readyScreenHeaderFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_READY_SCREEN_SUBTEXT_FONT to readyScreenSubtextFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_HEADER_FONT to retryScreenHeaderFont,
            FacetecFontsKey.GUIDANCE_CUSTOMIZATION_RETRY_SCREEN_SUBTEXT_FONT to retryScreenSubtextFont,
            FacetecFontsKey.RESULT_SCREEN_CUSTOMIZATION_MESSAGE_FONT to resultScreenMessageFont,
            FacetecFontsKey.FEEDBACK_CUSTOMIZATION_TEXT_FONT to feedbackMessageFont,
        )
    }
}
