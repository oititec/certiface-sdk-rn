package br.com.oititec.rn.sdk

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Arguments
import com.facebook.react.module.annotations.ReactModule
import br.com.oititec.rn.sdk.executor.LivenessExecutor
import br.com.oititec.rn.sdk.model.Features
import org.json.JSONObject

@ReactModule(name = RnSdkModule.NAME)
class RnSdkModule(reactContext: ReactApplicationContext) :
  NativeRnSdkSpec(reactContext) {

  override fun getName(): String {
    return NAME
  }
  override fun checkCameraPermission(promise: Promise?) {
    val permission = ContextCompat.checkSelfPermission(reactApplicationContext, Manifest.permission.CAMERA)
    promise?.resolve(permission == PackageManager.PERMISSION_GRANTED)
  }

  override fun requestCameraPermission(promise: Promise) {
    val currentActivity = currentActivity
    if (currentActivity == null) {
      promise.reject("ERROR", "Activity is null")
      return
    }

    val permission = ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CAMERA)
    if (permission == PackageManager.PERMISSION_GRANTED) {
      promise.resolve(true)
      return
    }

    ActivityCompat.requestPermissions(currentActivity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    promise.resolve(false)
  }

  override fun startJourney(
    appKey: String?,
    environment: String?,
    provider: String?,
    onSuccess: Callback?,
    onError: Callback?,
    isCustomEnabled: Boolean?,
    theme: ReadableMap?
  ) {
    val customEnabled = isCustomEnabled ?: false

    if (appKey.isNullOrEmpty()) {
      onError?.invoke("APP_KEY_NULO")
      return
    }

    if (environment.isNullOrEmpty()) {
      onError?.invoke("ENVIRONMENT_NULO")
      return
    }

    if (provider.isNullOrEmpty()) {
      onError?.invoke("PROVIDER_NULO")
      return
    }

    val selectedFeature = when (provider) {
      "FACETEC" -> Features.Facetec
      "IPROOV" -> Features.IProov
      else -> {
        onError?.invoke("PROVIDER_INVALIDO: $provider")
        return
      }
    }

    val activity = reactApplicationContext ?: run {
      onError?.invoke("NO_ACTIVITY")
      return
    }

    LivenessExecutor(appKey, selectedFeature).executeLiveness(
      context = activity,
      environment = environment,
      execOnSuccess = { livenessResult ->
        val jsonResult = convertLivenessResultToJson(livenessResult)
        onSuccess?.invoke(jsonResult)
      },
      execOnError = { error ->
        onError?.invoke(error)
      },
      isCustomEnabled = customEnabled,
      theme = theme
    )
  }

  private fun convertLivenessResultToJson(livenessResult: br.com.oiti.manager.exports.LivenessResult?): String {
    return try {
      val jsonObject = JSONObject()
      jsonObject.put("status", "success")
      
      val resultObject = JSONObject()
      resultObject.put("valid", livenessResult?.valid ?: false)
      resultObject.put("codID", livenessResult?.codID ?: "")
      resultObject.put("cause", livenessResult?.cause ?: "")
      resultObject.put("protocol", livenessResult?.protocol ?: "")
      resultObject.put("scanResultBlob", livenessResult?.scanResultBlob ?: "")
      
      jsonObject.put("result", resultObject)
      jsonObject.toString()
    } catch (e: Exception) {
      val errorObject = JSONObject()
      errorObject.put("status", "error")
      errorObject.put("message", "Failed to serialize result: ${e.message}")
      errorObject.toString()
    }
  }

  companion object {
    const val NAME = "RnSdk"
    const val CAMERA_PERMISSION_REQUEST_CODE = 100
  }
}
