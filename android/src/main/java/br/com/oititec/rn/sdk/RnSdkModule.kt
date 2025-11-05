package br.com.oititec.rn.sdk

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import br.com.oititec.rn.sdk.executor.LivenessExecutor
import br.com.oititec.rn.sdk.model.Features

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
    onSuccess: Callback?,
    onError: Callback?,
    isCustomEnabled: Boolean?,
    theme: ReadableMap?
  ) {
    val features = Features.entries
    var selectedFeature = features.first()
    val customEnabled = isCustomEnabled ?: false

    if (appKey.isNullOrEmpty()) {
      onError?.invoke("APP_KEY_NULO")
      return
    }

    if (environment.isNullOrEmpty()) {
      onError?.invoke("ENVIRONMENT_NULO")
      return
    }

    val activity = reactApplicationContext ?: run {
      onError?.invoke("NO_ACTIVITY")
      return
    }

    LivenessExecutor(appKey, selectedFeature).executeLiveness(
      context = activity,
      environment = environment,
      execOnSuccess = { livenessResult ->
        onSuccess?.invoke(livenessResult)
      },
      execOnError = { error ->
        onError?.invoke(error)
      },
      isCustomEnabled = customEnabled,
      theme = theme
    )
  }


  companion object {
    const val NAME = "RnSdk"
    const val CAMERA_PERMISSION_REQUEST_CODE = 100
  }
}
