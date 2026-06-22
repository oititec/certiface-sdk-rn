package br.com.certiface.rn.sdk.processors

import android.content.Context
import android.util.Log
import br.com.certiface.manager.exports.FacetecDrawablesKey
import br.com.certiface.manager.exports.IProovDrawablesKey
import com.facebook.react.bridge.ReadableMap

object AssetProcessor {
    private const val TAG = "AssetProcessor"

    fun getDrawableResourceId(context: Context, drawableName: String): Int {
        val resourceId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
        if (resourceId != 0) {
            Log.d(TAG, "Drawable '$drawableName' encontrado com ID: $resourceId")
        } else {
            Log.w(TAG, "Drawable '$drawableName' não encontrado no package ${context.packageName}")
        }
        return resourceId
    }

    fun resolveDrawableResourceId(context: Context?, value: Any?): Int? {
        if (context == null || value == null) return null
        return when (value) {
            is Int -> value.takeIf { it != 0 }
            is String -> {
                val name = value.trim()
                if (name.isEmpty()) return null
                getDrawableResourceId(context, name).takeIf { it != 0 }
            }
            else -> null
        }
    }
    
    fun processFacetecAssets(theme: ReadableMap?): Map<FacetecDrawablesKey, Any> {
        val facetecDrawables = mutableMapOf<FacetecDrawablesKey, Any>()
        
        if (theme == null) {
            Log.w(TAG, "Theme é null - nenhum asset será processado")
            return facetecDrawables
        }
        
        val facetecTheme = theme.getMap("facetec")
        val facetecAssets = facetecTheme?.getMap("assets")
        
        val instructionsTheme = theme.getMap("instructions")
        val instructionsAssets = instructionsTheme?.getMap("assets")
        
        val permissionTheme = theme.getMap("permission")
        val permissionAssets = permissionTheme?.getMap("assets")
        
        facetecAssets?.getString("overlayBrandImage")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_OVERLAY_SHOW_BRANDING_IMAGE] = assetName
        } ?: Log.d(TAG, "overlayBrandImage não encontrado em facetec.assets")
        
        facetecAssets?.getString("cancelButtonIcon")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_CANCEL_BUTTON_CUSTOM_IMAGE] = assetName
        } ?: Log.d(TAG, "cancelButtonIcon não encontrado em facetec.assets")
        
        facetecAssets?.getString("resultScreenCustomActivityIndicatorImage")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ACTIVITY_INDICATOR_IMAGE] = assetName
        } ?: Log.d(TAG, "resultScreenCustomActivityIndicatorImage não encontrado em facetec.assets")

        facetecAssets?.getString("resultScreenCustomActivityIndicatorAnimation")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ACTIVITY_INDICATOR_ANIMATION] = assetName
        } ?: Log.d(TAG, "resultScreenCustomActivityIndicatorAnimation não encontrado em facetec.assets")
        
        instructionsAssets?.getString("firstInstructionIcon")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "firstInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("secondInstructionIcon")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "secondInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("contextImage")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE] = assetName
        } ?: Log.d(TAG, "contextImage não encontrado em instructions.assets")
        
        instructionsAssets?.getString("backButtonIcon")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG] = assetName
        } ?: Log.d(TAG, "backButtonIcon não encontrado em instructions.assets")
        
        permissionAssets?.getString("cameraImage")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.PERMISSION_CAMERA_ICON] = assetName
        } ?: Log.d(TAG, "cameraImage não encontrado em permission.assets")
        
        permissionAssets?.getString("backButtonIcon")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.PERMISSION_BACK_BUTTON_ICON] = assetName
        } ?: Log.d(TAG, "backButtonIcon não encontrado em permission.assets")

        val resultTheme = theme.getMap("result")
        val resultAssets = resultTheme?.getMap("assets")

        val successImageName = facetecAssets?.getString("resultScreenSuccessImage")
            ?: resultAssets?.getString("successImage")
        val errorImageName = facetecAssets?.getString("resultScreenErrorImage")
            ?: resultAssets?.getString("errorImage")

        successImageName?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_STATIC_RESULT_ANIMATION_SUCCESS] = assetName
        }

        errorImageName?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_STATIC_RESULT_ANIMATION_UNSUCCESS] = assetName
        }

        facetecAssets?.getString("resultScreenSuccessBackgroundImage")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_ANIMATION_SUCCESS_BACKGROUND_IMAGE] = assetName
        }

        facetecAssets?.getString("resultScreenErrorBackgroundImage")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_ANIMATION_UNSUCESS_BACKGROUND_IMAGE] = assetName
        }

        facetecAssets?.getString("resultScreenSuccessAnimation")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ANIMATION_SUCCESS] = assetName
        } ?: Log.d(TAG, "resultScreenSuccessAnimation não encontrado em facetec.assets")

        facetecAssets?.getString("resultScreenErrorAnimation")?.let { assetName ->
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ANIMATION_UNSUCCESS] = assetName
        } ?: Log.d(TAG, "resultScreenErrorAnimation não encontrado em facetec.assets")

        return facetecDrawables
    }

    fun resolveFacetecDrawables(
        context: Context?,
        drawables: Map<FacetecDrawablesKey, Any>
    ): Map<FacetecDrawablesKey, Int> {
        if (context == null || drawables.isEmpty()) return emptyMap()
        return drawables.mapNotNull { (key, value) ->
            val resourceId = resolveDrawableResourceId(context, value) ?: return@mapNotNull null
            key to resourceId
        }.toMap()
    }

    fun processIProovAssets(theme: ReadableMap?): Map<IProovDrawablesKey, Any> {
        val iproovDrawables = mutableMapOf<IProovDrawablesKey, Any>()
        
        if (theme == null) {
            Log.w(TAG, "❌ Theme é null - nenhum asset IProov será processado")
            return iproovDrawables
        }
        
        val iproovTheme = theme.getMap("iproov")
        val iproovAssets = iproovTheme?.getMap("assets")
        
        val instructionsTheme = theme.getMap("instructions")
        val instructionsAssets = instructionsTheme?.getMap("assets")
        
        val permissionTheme = theme.getMap("permission")
        val permissionAssets = permissionTheme?.getMap("assets")
        
        val resultTheme = theme.getMap("result")
        val resultAssets = resultTheme?.getMap("assets")
        
        
        iproovAssets?.getString("logoImage")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.IPROOV_LOGO] = assetName
        } ?: Log.d(TAG, "ogoImage não encontrado em iproov.assets")
        
        iproovAssets?.getString("closeButtonIcon")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.IPROOV_CLOSE_BUTTON] = assetName
        } ?: Log.d(TAG, "closeButtonIcon não encontrado em iproov.assets")
        
        instructionsAssets?.getString("firstInstructionIcon")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "firstInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("secondInstructionIcon")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "secondInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("contextImage")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE] = assetName
        } ?: Log.d(TAG, "contextImage não encontrado em instructions.assets")
        
        instructionsAssets?.getString("backButtonIcon")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG] = assetName
        } ?: Log.d(TAG, "backButtonIcon não encontrado em instructions.assets")
        
        permissionAssets?.getString("cameraImage")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.PERMISSION_CAMERA_ICON] = assetName
        } ?: Log.d(TAG, "cameraImage não encontrado em permission.assets")
        
        permissionAssets?.getString("backButtonIcon")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.PERMISSION_BACK_BUTTON_ICON] = assetName
        } ?: Log.d(TAG, "backButtonIcon não encontrado em permission.assets")
        
        resultAssets?.getString("successImage")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.RESULT_SUCCESS_ICON] = assetName
        } ?: Log.d(TAG, "successImage não encontrado em result.assets")
        
        resultAssets?.getString("errorImage")?.let { assetName ->
            iproovDrawables[IProovDrawablesKey.RESULT_ERROR_ICON] = assetName
        } ?: Log.d(TAG, "errorImage não encontrado em result.assets")
        
        return iproovDrawables
    }
}
