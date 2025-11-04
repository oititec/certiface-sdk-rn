package br.com.oititec.rn.sdk.theme

import br.com.oiti.manager.exports.FacetecFontsKey
import com.facebook.react.bridge.ReadableMap

// readyScreenHeader?: string;
//  readyScreenSubtext?: string;
//  resultScreenMessage?: string;
//  retryScreenHeader?: string;
//  retryScreenSubtext?: string;
//  feedbackMessage?: string;
//  guidanceHeader?: string;
//  guidanceSubtext?: string;
//  guidanceButton?: string;

class FacetecFonts(
    private val instructionsFonts: ReadableMap?,
    private val permissionFonts: ReadableMap?,
    private val facetecFonts: ReadableMap?
) {
    private val instructionsTitleFont: String =
        "fonts/" + (instructionsFonts?.getString("title")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val instructionsCaptionFont: String =
        "fonts/" + (instructionsFonts?.getString("caption")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val instructionsFirstInstructionTitleFont: String =
        "fonts/" + (instructionsFonts?.getString("firstInstructionTitle")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val instructionsSecondInstructionTitleFont: String =
        "fonts/" + (instructionsFonts?.getString("secondInstructionTitle")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val instructionsContinueButtonFont: String =
        "fonts/" + (instructionsFonts?.getString("continueButton")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val permissionTitleFont: String =
        "fonts/" + (permissionFonts?.getString("title")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val permissionCaptionFont: String =
        "fonts/" + (permissionFonts?.getString("caption")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val permissionButtonFont: String =
        "fonts/" + (permissionFonts?.getString("checkPermissionButton")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val guidanceHeaderFont: String =
        "fonts/" + (facetecFonts?.getString("guidanceHeader")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val guidanceSubtextFont: String =
        "fonts/" + (facetecFonts?.getString("guidanceSubtext")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val guidanceButtonFont: String =
        "fonts/" + (facetecFonts?.getString("guidanceButton")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val readyScreenHeaderFont: String =
        "fonts/" + (facetecFonts?.getString("readyScreenHeader")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val readyScreenSubtextFont: String =
        "fonts/" + (facetecFonts?.getString("readyScreenSubtext")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val retryScreenHeaderFont: String =
        "fonts/" + (facetecFonts?.getString("retryScreenHeader")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val retryScreenSubtextFont: String =
        "fonts/" + (facetecFonts?.getString("retryScreenSubtext")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val resultScreenMessageFont: String =
        "fonts/" + (facetecFonts?.getString("resultScreenMessage")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    private val feedbackMessageFont: String =
        "fonts/" + (facetecFonts?.getString("feedbackMessage")?.lowercase() ?: "ubuntu_regular") + ".ttf"

    fun apply(): HashMap<FacetecFontsKey, String> {
        return hashMapOf(
            FacetecFontsKey.INSTRUCTIONS_TITLE_FONT to instructionsTitleFont,
            FacetecFontsKey.INSTRUCTIONS_CAPTION_FONT to instructionsCaptionFont,
            FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TYPES_INSTRUCTIONS_FONT to instructionsFirstInstructionTitleFont,
            FacetecFontsKey.INSTRUCTIONS_DOCUMENT_TIPS_INSTRUCTIONS_FONT to instructionsSecondInstructionTitleFont,
//            FacetecFontsKey.INSTRUCTIONS_BUTTON_FONT to instructionsButtonFont,

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
