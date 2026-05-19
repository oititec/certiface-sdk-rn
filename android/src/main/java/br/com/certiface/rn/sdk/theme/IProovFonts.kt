package br.com.certiface.rn.sdk.theme

import br.com.certiface.manager.exports.IProovFontsKey
import com.facebook.react.bridge.ReadableMap

class IProovFonts(
    private val iproovFonts: ReadableMap?,
    private val instructionsFonts: ReadableMap? = null,
    private val permissionFonts: ReadableMap? = null
) {

    private fun resolveFont(
        iproovKey: String,
        instructionsKey: String? = null,
        permissionKey: String? = null
    ): String {
        val iproovValue = iproovFonts?.getString(iproovKey)?.trim().orEmpty()
        if (iproovValue.isNotEmpty()) return fontAssetPath(iproovValue)
        val permissionValue = permissionKey
            ?.let { permissionFonts?.getString(it)?.trim().orEmpty() }
            .orEmpty()
        if (permissionValue.isNotEmpty()) return fontAssetPath(permissionValue)
        val instructionsValue = instructionsKey
            ?.let { instructionsFonts?.getString(it)?.trim().orEmpty() }
            .orEmpty()
        if (instructionsValue.isNotEmpty()) return fontAssetPath(instructionsValue)
        return fontAssetPath(null)
    }

    private val instructionsTitleFont: String =
        resolveFont("instructionsTitleFont", "title")

    private val instructionsCaptionFont: String =
        resolveFont("instructionsCaptionFont", "caption")

    private val instructionsDocumentTypesInstructionsFont: String =
        resolveFont("instructionsDocumentTypesInstructionsFont", "firstInstructionTitle")

    private val instructionsDocumentTipsInstructionsFont: String =
        resolveFont("instructionsDocumentTipsInstructionsFont", "secondInstructionTitle")

    private val instructionsButtonFont: String =
        resolveFont("instructionsButtonFont", "continueButton")

    private val permissionTitleFont: String =
        resolveFont("permissionTitleFont", permissionKey = "title")

    private val permissionCaptionFont: String =
        resolveFont("permissionCaptionFont", permissionKey = "caption")

    private val permissionButtonFont: String =
        resolveFont("permissionButtonFont", permissionKey = "checkPermissionButton")

    private val resultMessageFont: String = resolveFont("resultMessageFont")

    private val resultRetryButtonFont: String = resolveFont("resultRetryButtonFont")

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
