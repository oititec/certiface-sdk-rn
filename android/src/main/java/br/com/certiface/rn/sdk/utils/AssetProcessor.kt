package br.com.certiface.rn.sdk.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import java.lang.reflect.Field

class AssetProcessor(private val context: Context) {

    companion object {
        private const val TAG = "AssetProcessor"
        private const val ASSETS_CACHE_DIR = "theme_assets"
    }

    fun processBase64ToDrawable(base64String: String?, fallbackDrawableRes: Int): Drawable? {
        if (base64String.isNullOrEmpty()) {
            Log.d(TAG, "Base64 string is null or empty, using fallback")
            return context.getDrawable(fallbackDrawableRes)
        }

        return try {
            Log.d(TAG, "Processing base64 string: ${base64String.take(50)}...")
            
            val cleanBase64 = if (base64String.contains("base64,")) {
                base64String.substringAfter("base64,")
            } else {
                base64String
            }
            
            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            Log.d(TAG, "Decoded bytes size: ${decodedBytes.size}")
            
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            
            if (bitmap != null) {
                Log.d(TAG, "Successfully created bitmap: ${bitmap.width}x${bitmap.height}")
                BitmapDrawable(context.resources, bitmap)
            } else {
                Log.w(TAG, "Failed to decode bitmap from base64")
                context.getDrawable(fallbackDrawableRes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing base64 to drawable: ${e.message}")
            e.printStackTrace()
            context.getDrawable(fallbackDrawableRes)
        }
    }

    fun saveBase64AsDrawableResource(base64String: String?): Int? {
        if (base64String.isNullOrEmpty()) return null

        return try {
            Log.d(TAG, "Saving base64 as drawable resource...")
            
            val cleanBase64 = if (base64String.contains("base64,")) {
                base64String.substringAfter("base64,")
            } else {
                base64String
            }
            
            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            
            if (bitmap != null) {
                Log.d(TAG, "Successfully created bitmap from base64: ${bitmap.width}x${bitmap.height}")
                
                val cacheDir = File(context.cacheDir, ASSETS_CACHE_DIR)
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }
                
                val fileName = "asset_${System.currentTimeMillis()}.png"
                val file = File(cacheDir, fileName)
                
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                
                Log.d(TAG, "Saved asset to: ${file.absolutePath}")
                
                System.currentTimeMillis().toInt()
            } else {
                Log.w(TAG, "Failed to create bitmap from base64")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving base64 as drawable resource: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun createDrawableFromCachedFile(resourceId: Int?, fallbackDrawableRes: Int): Drawable? {
        if (resourceId == null) {
            return context.getDrawable(fallbackDrawableRes)
        }

        return try {
            val cacheDir = File(context.cacheDir, ASSETS_CACHE_DIR)
            val files = cacheDir.listFiles() ?: return context.getDrawable(fallbackDrawableRes)
            
            val targetFile = files.find { it.absolutePath.hashCode() == resourceId }
            
            if (targetFile?.exists() == true) {
                val bitmap = BitmapFactory.decodeFile(targetFile.absolutePath)
                if (bitmap != null) {
                    BitmapDrawable(context.resources, bitmap)
                } else {
                    context.getDrawable(fallbackDrawableRes)
                }
            } else {
                Log.w(TAG, "Cached file not found for resource ID: $resourceId")
                context.getDrawable(fallbackDrawableRes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating drawable from cached file: ${e.message}")
            context.getDrawable(fallbackDrawableRes)
        }
    }

    fun createDynamicResourceId(drawable: Drawable?): Int? {
        if (drawable == null) return null
        
        return try {
            val drawableHash = drawable.hashCode()
            
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                if (bitmap != null) {
                    val cacheDir = File(context.cacheDir, ASSETS_CACHE_DIR)
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs()
                    }
                    
                    val fileName = "dynamic_${drawableHash}.png"
                    val file = File(cacheDir, fileName)
                    
                    if (!file.exists()) {
                        FileOutputStream(file).use { out ->
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        }
                        Log.d(TAG, "Created dynamic resource file: ${file.absolutePath}")
                    }
                    
                    file.absolutePath.hashCode()
                } else {
                    Log.w(TAG, "BitmapDrawable has null bitmap")
                    null
                }
            } else {
                Log.d(TAG, "Drawable is not BitmapDrawable, using hash as ID")
                drawableHash
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating dynamic resource ID: ${e.message}")
            null
        }
    }

    fun cleanupCache() {
        try {
            val cacheDir = File(context.cacheDir, ASSETS_CACHE_DIR)
            if (cacheDir.exists()) {
                cacheDir.deleteRecursively()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up cache: ${e.message}")
        }
    }
}

