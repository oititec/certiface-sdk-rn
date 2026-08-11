package br.com.certiface.rn.sdk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class AssetProcessor(private val context: Context) {

    companion object {
        private const val TAG = "AssetProcessor"
        private const val ASSETS_CACHE_DIR = "theme_assets"
        private const val MAX_BASE64_CHARS = 2_000_000
        private const val MAX_DECODED_BYTES = 1_500_000
        private const val MAX_BITMAP_DIMENSION = 2048
    }

    fun processBase64ToDrawable(base64String: String?, fallbackDrawableRes: Int): Drawable? {
        if (base64String.isNullOrEmpty()) {
            return context.getDrawable(fallbackDrawableRes)
        }

        return try {
            val bitmap = decodeBase64ToBitmap(base64String)
            if (bitmap != null) {
                BitmapDrawable(context.resources, bitmap)
            } else {
                context.getDrawable(fallbackDrawableRes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing base64 to drawable")
            context.getDrawable(fallbackDrawableRes)
        }
    }

    fun saveBase64AsDrawableResource(base64String: String?): Int? {
        if (base64String.isNullOrEmpty()) return null

        return try {
            val bitmap = decodeBase64ToBitmap(base64String) ?: return null

            val cacheDir = File(context.cacheDir, ASSETS_CACHE_DIR)
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val fileName = "asset_${System.currentTimeMillis()}.png"
            val file = File(cacheDir, fileName)

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            }

            file.absolutePath.hashCode()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving base64 as drawable resource")
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
                val bitmap = decodeFileDownsampled(targetFile.absolutePath)
                if (bitmap != null) {
                    BitmapDrawable(context.resources, bitmap)
                } else {
                    context.getDrawable(fallbackDrawableRes)
                }
            } else {
                context.getDrawable(fallbackDrawableRes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating drawable from cached file")
            context.getDrawable(fallbackDrawableRes)
        }
    }

    fun createDynamicResourceId(drawable: Drawable?): Int? {
        if (drawable == null) return null

        return try {
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap ?: return null
                val cacheDir = File(context.cacheDir, ASSETS_CACHE_DIR)
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }

                val fileName = "dynamic_${bitmap.hashCode()}.png"
                val file = File(cacheDir, fileName)

                if (!file.exists()) {
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                    }
                }

                file.absolutePath.hashCode()
            } else {
                drawable.hashCode()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating dynamic resource ID")
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
            Log.e(TAG, "Error cleaning up cache")
        }
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        if (base64String.length > MAX_BASE64_CHARS) {
            Log.w(TAG, "Base64 payload exceeds size limit")
            return null
        }

        val cleanBase64 = if (base64String.contains("base64,")) {
            base64String.substringAfter("base64,")
        } else {
            base64String
        }

        if (cleanBase64.length > MAX_BASE64_CHARS) {
            Log.w(TAG, "Base64 payload exceeds size limit")
            return null
        }

        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        if (decodedBytes.size > MAX_DECODED_BYTES) {
            Log.w(TAG, "Decoded image exceeds size limit")
            return null
        }

        return decodeByteArrayDownsampled(decodedBytes)
    }

    private fun decodeByteArrayDownsampled(bytes: ByteArray): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
            return null
        }

        val options = BitmapFactory.Options().apply {
            inSampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, MAX_BITMAP_DIMENSION)
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    private fun decodeFileDownsampled(path: String): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
            return null
        }

        val options = BitmapFactory.Options().apply {
            inSampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, MAX_BITMAP_DIMENSION)
        }
        return BitmapFactory.decodeFile(path, options)
    }

    private fun calculateInSampleSize(width: Int, height: Int, maxDimension: Int): Int {
        var inSampleSize = 1
        while (width / inSampleSize > maxDimension || height / inSampleSize > maxDimension) {
            inSampleSize *= 2
        }
        return inSampleSize
    }
}
