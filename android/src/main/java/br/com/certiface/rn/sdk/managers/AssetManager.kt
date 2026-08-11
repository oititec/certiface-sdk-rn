package br.com.certiface.rn.sdk.managers

import android.content.Context
import android.util.Log
import br.com.certiface.rn.sdk.utils.AssetProcessor
import com.facebook.react.bridge.ReadableMap
import java.util.concurrent.ConcurrentHashMap

object AssetManager {
    private const val TAG = "AssetManager"
    private val processedAssets = ConcurrentHashMap<String, Int?>()
    private var isInitialized = false
    
    fun initialize(context: Context, theme: ReadableMap?) {
        if (isInitialized) {
            Log.d(TAG, "AssetManager already initialized")
            return
        }
                
        theme?.let { processThemeAssets(context, it) }
        
        isInitialized = true
    }
    
    private fun processThemeAssets(context: Context, theme: ReadableMap) {
        val assetProcessor = AssetProcessor(context)
        
        val instructionsTheme = theme.getMap("instructions")
        val instructionsAssets = instructionsTheme?.getMap("assets")
        
        instructionsAssets?.let { assets ->
            assets.getString("logo")?.let { logoBase64 ->
                val logoKey = "instructions_logo"
                Log.d(TAG, "Processing instructions logo asset: $logoKey")
                
                val drawable = assetProcessor.processBase64ToDrawable(
                    logoBase64, 
                    br.com.certiface.designsystem.R.drawable.error_icon
                )
                
                val resourceId = assetProcessor.createDynamicResourceId(drawable)
                
                processedAssets[logoKey] = resourceId
                Log.d(TAG, "Instructions logo processed successfully: ${resourceId != null}")
            }
        }
        
        val iproovTheme = theme.getMap("iproov")
        val iproovAssets = iproovTheme?.getMap("assets")
        
        iproovAssets?.let { assets ->
            assets.getString("logo")?.let { logoBase64 ->
                val logoKey = "iproov_logo"
                Log.d(TAG, "Processing iproov logo asset: $logoKey")
                
                val drawable = assetProcessor.processBase64ToDrawable(
                    logoBase64, 
                    br.com.certiface.designsystem.R.drawable.error_icon
                )
                
                val resourceId = assetProcessor.createDynamicResourceId(drawable)
                
                processedAssets[logoKey] = resourceId
                Log.d(TAG, "IProov logo processed successfully: ${resourceId != null}")
            }
            
            assets.getString("closeButton")?.let { closeButtonBase64 ->
                val closeButtonKey = "iproov_close_button"
                Log.d(TAG, "Processing iproov close button asset: $closeButtonKey")
                
                val drawable = assetProcessor.processBase64ToDrawable(
                    closeButtonBase64, 
                    br.com.certiface.designsystem.R.drawable.close_icon
                )
                
                val resourceId = assetProcessor.createDynamicResourceId(drawable)
                
                processedAssets[closeButtonKey] = resourceId
                Log.d(TAG, "IProov close button processed successfully: ${resourceId != null}")
            }
        }
        
        val facetecTheme = theme.getMap("facetec")
        val facetecAssets = facetecTheme?.getMap("assets")
        
        facetecAssets?.let { assets ->
            assets.getString("overlayBrandingImage")?.let { overlayBase64 ->
                val overlayKey = "facetec_overlay_branding"
                Log.d(TAG, "Processing facetec overlay branding asset: $overlayKey")
                
                val drawable = assetProcessor.processBase64ToDrawable(
                    overlayBase64, 
                    br.com.certiface.designsystem.R.drawable.neutral_face
                )
                
                val resourceId = assetProcessor.createDynamicResourceId(drawable)
                
                processedAssets[overlayKey] = resourceId
                Log.d(TAG, "Facetec overlay branding processed successfully: ${resourceId != null}")
            }
            
            assets.getString("cancelButtonCustomImage")?.let { cancelButtonBase64 ->
                val cancelButtonKey = "facetec_cancel_button"
                Log.d(TAG, "Processing facetec cancel button asset: $cancelButtonKey")
                
                val drawable = assetProcessor.processBase64ToDrawable(
                    cancelButtonBase64, 
                    br.com.certiface.designsystem.R.drawable.close_icon
                )
                
                val resourceId = assetProcessor.createDynamicResourceId(drawable)
                
                processedAssets[cancelButtonKey] = resourceId
                Log.d(TAG, "Facetec cancel button processed successfully: ${resourceId != null}")
            }
            
            assets.getString("resultScreenCustomActivityIndicatorImage")?.let { activityIndicatorBase64 ->
                val activityIndicatorKey = "facetec_activity_indicator"
                Log.d(TAG, "Processing facetec activity indicator asset: $activityIndicatorKey")
                
                val drawable = assetProcessor.processBase64ToDrawable(
                    activityIndicatorBase64, 
                    br.com.certiface.designsystem.R.drawable.success_icon
                )
                
                val resourceId = assetProcessor.createDynamicResourceId(drawable)
                
                processedAssets[activityIndicatorKey] = resourceId
                Log.d(TAG, "Facetec activity indicator processed successfully: ${resourceId != null}")
            }
        }
    }
    
    fun getProcessedAsset(key: String): Int? {
        return processedAssets[key]
    }
    
    fun hasAsset(key: String): Boolean {
        return processedAssets.containsKey(key)
    }
    
    fun clear(context: Context? = null) {
        processedAssets.clear()
        isInitialized = false
        context?.let { AssetProcessor(it).cleanupCache() }
    }
    
    fun isReady(): Boolean = isInitialized
}
