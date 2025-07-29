package br.com.oititec.rn.sdk

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import br.com.oititec.rn.sdk.NativeRnSdkSpec
import br.com.oititec.rn.sdk.executor.LivenessExecutor

@ReactModule(name = RnSdkModule.NAME)
class RnSdkModule(reactContext: ReactApplicationContext) :
  NativeRnSdkSpec(reactContext) {

  override fun getName(): String {
    return NAME
  }

  override fun multiply(a: Double, b: Double): Double {
    return a * b
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
    onSuccess: Callback?,
    onError: Callback?
  ) {
    val isCustomEnabled = false
    // val theme: Map<String, Any>? = call.argument("theme")

    if (appKey.isNullOrEmpty()) {
      onError?.invoke("APP_KEY_NULO")
      return
    }

    val activity = currentActivity ?: run {
      onError?.invoke("NO_ACTIVITY")
      return
    }

    LivenessExecutor(appKey).executeLiveness(
      context = activity,
      execOnSuccess = { livenessResult ->
        onSuccess?.invoke(livenessResult)
      },
      execOnError = { error ->
        onError?.invoke(error)
      },
      isCustomEnabled = isCustomEnabled
      // , theme = theme
    )
  }

  override fun testString(appKey: String?): String {
    TODO("Not yet implemented")
  }

  companion object {
    const val NAME = "RnSdk"
    const val CAMERA_PERMISSION_REQUEST_CODE = 100
  }
}
