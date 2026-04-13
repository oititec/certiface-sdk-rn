package br.com.certiface.rn.sdk.processors

import android.content.Context
import android.util.Log
import br.com.certiface.manager.exports.FacetecDrawablesKey
import br.com.certiface.manager.exports.IProovDrawablesKey
import com.facebook.react.bridge.ReadableMap

object AssetProcessor {
    private const val TAG = "AssetProcessor"
    
    fun getDrawableResourceId(context: Context, drawableName: String): Int {
        Log.d(TAG, "Tentando encontrar drawable: '$drawableName' no package: ${context.packageName}")
        val resourceId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
        if (resourceId != 0) {
            Log.d(TAG, "✅ Drawable '$drawableName' encontrado com ID: $resourceId")
        } else {
            Log.w(TAG, "❌ Drawable '$drawableName' NÃO encontrado no package ${context.packageName}")
        }
        return resourceId
    }
    
    fun processFacetecAssets(theme: ReadableMap?): Map<FacetecDrawablesKey, Any> {
        Log.d(TAG, "🔍 Iniciando processamento de assets do Facetec...")
        val facetecDrawables = mutableMapOf<FacetecDrawablesKey, Any>()
        
        if (theme == null) {
            Log.w(TAG, "❌ Theme é null - nenhum asset será processado")
            return facetecDrawables
        }
        
        val facetecTheme = theme.getMap("facetec")
        val facetecAssets = facetecTheme?.getMap("assets")
        
        val instructionsTheme = theme.getMap("instructions")
        val instructionsAssets = instructionsTheme?.getMap("assets")
        
        val permissionTheme = theme.getMap("permission")
        val permissionAssets = permissionTheme?.getMap("assets")
        
        Log.d(TAG, "📦 Facetec assets disponível: ${facetecAssets != null}")
        Log.d(TAG, "📦 Instructions assets disponível: ${instructionsAssets != null}")
        Log.d(TAG, "📦 Permission assets disponível: ${permissionAssets != null}")
        
        facetecAssets?.getString("overlayBrandImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado overlayBrandImage: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.FACETEC_OVERLAY_SHOW_BRANDING_IMAGE] = assetName
        } ?: Log.d(TAG, "⚠️ overlayBrandImage não encontrado em facetec.assets")
        
        facetecAssets?.getString("cancelButtonIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado cancelButtonIcon: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.FACETEC_CANCEL_BUTTON_CUSTOM_IMAGE] = assetName
        } ?: Log.d(TAG, "⚠️ cancelButtonIcon não encontrado em facetec.assets")
        
        facetecAssets?.getString("resultScreenCustomActivityIndicatorImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado resultScreenCustomActivityIndicatorImage: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.FACETEC_RESULT_CUSTOM_ACTIVITY_INDICATOR_IMAGE] = assetName
        } ?: Log.d(TAG, "⚠️ resultScreenCustomActivityIndicatorImage não encontrado em facetec.assets")
        
        instructionsAssets?.getString("firstInstructionIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado firstInstructionIcon: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ firstInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("secondInstructionIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado secondInstructionIcon: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ secondInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("contextImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado contextImage: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE] = assetName
        } ?: Log.d(TAG, "⚠️ contextImage não encontrado em instructions.assets")
        
        instructionsAssets?.getString("backButtonIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado instructions backButtonIcon: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG] = assetName
        } ?: Log.d(TAG, "⚠️ backButtonIcon não encontrado em instructions.assets")
        
        permissionAssets?.getString("cameraImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado cameraImage: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.PERMISSION_CAMERA_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ cameraImage não encontrado em permission.assets")
        
        permissionAssets?.getString("backButtonIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado permission backButtonIcon: '$assetName'")
            facetecDrawables[FacetecDrawablesKey.PERMISSION_BACK_BUTTON_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ backButtonIcon não encontrado em permission.assets")
        
        Log.d(TAG, "🏁 Processamento Facetec finalizado: ${facetecDrawables.size} assets encontrados")
        facetecDrawables.forEach { (key, value) ->
            Log.d(TAG, "   📎 $key = '$value'")
        }
        return facetecDrawables
    }

    fun processIProovAssets(theme: ReadableMap?): Map<IProovDrawablesKey, Any> {
        Log.d(TAG, "🔍 Iniciando processamento de assets do IProov...")
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
        
        Log.d(TAG, "📦 IProov assets disponível: ${iproovAssets != null}")
        Log.d(TAG, "📦 Instructions assets disponível: ${instructionsAssets != null}")
        Log.d(TAG, "📦 Permission assets disponível: ${permissionAssets != null}")
        Log.d(TAG, "📦 Result assets disponível: ${resultAssets != null}")
        
        iproovAssets?.getString("logoImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado logoImage: '$assetName'")
            iproovDrawables[IProovDrawablesKey.IPROOV_LOGO] = assetName
        } ?: Log.d(TAG, "⚠️ logoImage não encontrado em iproov.assets")
        
        iproovAssets?.getString("closeButtonIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado closeButtonIcon: '$assetName'")
            iproovDrawables[IProovDrawablesKey.IPROOV_CLOSE_BUTTON] = assetName
        } ?: Log.d(TAG, "⚠️ closeButtonIcon não encontrado em iproov.assets")
        
        instructionsAssets?.getString("firstInstructionIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado firstInstructionIcon: '$assetName'")
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_FIRST_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ firstInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("secondInstructionIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado secondInstructionIcon: '$assetName'")
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_SECOND_INSTRUCTION_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ secondInstructionIcon não encontrado em instructions.assets")
        
        instructionsAssets?.getString("contextImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado contextImage: '$assetName'")
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_CONTEXT_IMAGE] = assetName
        } ?: Log.d(TAG, "⚠️ contextImage não encontrado em instructions.assets")
        
        instructionsAssets?.getString("backButtonIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado instructions backButtonIcon: '$assetName'")
            iproovDrawables[IProovDrawablesKey.INSTRUCTIONS_BACK_BUTTON_IMG] = assetName
        } ?: Log.d(TAG, "⚠️ backButtonIcon não encontrado em instructions.assets")
        
        permissionAssets?.getString("cameraImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado cameraImage: '$assetName'")
            iproovDrawables[IProovDrawablesKey.PERMISSION_CAMERA_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ cameraImage não encontrado em permission.assets")
        
        permissionAssets?.getString("backButtonIcon")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado permission backButtonIcon: '$assetName'")
            iproovDrawables[IProovDrawablesKey.PERMISSION_BACK_BUTTON_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ backButtonIcon não encontrado em permission.assets")
        
        resultAssets?.getString("successImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado successImage: '$assetName'")
            iproovDrawables[IProovDrawablesKey.RESULT_SUCCESS_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ successImage não encontrado em result.assets")
        
        resultAssets?.getString("errorImage")?.let { assetName ->
            Log.d(TAG, "✅ Encontrado errorImage: '$assetName'")
            iproovDrawables[IProovDrawablesKey.RESULT_ERROR_ICON] = assetName
        } ?: Log.d(TAG, "⚠️ errorImage não encontrado em result.assets")
        
        Log.d(TAG, "🏁 Processamento IProov finalizado: ${iproovDrawables.size} assets encontrados")
        iproovDrawables.forEach { (key, value) ->
            Log.d(TAG, "   📎 $key = '$value'")
        }
        return iproovDrawables
    }
}
