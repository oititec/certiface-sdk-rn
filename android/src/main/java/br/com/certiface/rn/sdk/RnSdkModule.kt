package br.com.certiface.rn.sdk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import br.com.certiface.rn.sdk.executor.LivenessExecutor
import br.com.certiface.rn.sdk.model.Features
import br.com.certiface.rn.sdk.utils.AssetProcessor
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

@ReactModule(name = RnSdkModule.NAME)
class RnSdkModule(reactContext: ReactApplicationContext) :
  NativeRnSdkSpec(reactContext), LifecycleEventListener {

  private var cameraPermissionPromise: Promise? = null
  private val journeyInFlight = AtomicBoolean(false)
  private val mainHandler = Handler(Looper.getMainLooper())
  private var journeyTimeoutRunnable: Runnable? = null

  private val cameraPermissionListener = PermissionListener { requestCode, _, grantResults ->
    if (requestCode != CAMERA_PERMISSION_REQUEST_CODE) {
      return@PermissionListener false
    }

    val promise = cameraPermissionPromise ?: return@PermissionListener false
    cameraPermissionPromise = null
    val granted =
      grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    promise.resolve(granted)
    true
  }

  init {
    reactContext.addLifecycleEventListener(this)
  }

  override fun getName(): String {
    return NAME
  }

  override fun onHostResume() {}

  override fun onHostPause() {}

  override fun onHostDestroy() {
    val promise = cameraPermissionPromise
    cameraPermissionPromise = null
    promise?.reject("ERROR", "Activity destroyed")
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

    if (cameraPermissionPromise != null) {
      promise.reject("ERROR", "Camera permission request already in progress")
      return
    }

    val permissionActivity = currentActivity as? PermissionAwareActivity
    if (permissionActivity == null) {
      promise.reject("ERROR", "Activity does not support permission requests")
      return
    }

    cameraPermissionPromise = promise
    permissionActivity.requestPermissions(
      arrayOf(Manifest.permission.CAMERA),
      CAMERA_PERMISSION_REQUEST_CODE,
      cameraPermissionListener
    )
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
    val delivered = AtomicBoolean(false)

    if (!journeyInFlight.compareAndSet(false, true)) {
      invokeOnceOnUiThread(
        delivered,
        onError,
        serializeBridgeError(
          "JOURNEY_IN_PROGRESS",
          "Uma jornada de liveness já está em andamento."
        )
      )
      return
    }

    if (appKey.isNullOrEmpty()) {
      releaseJourneyAndInvoke(delivered, onError, serializeBridgeError("APP_KEY_NULO", "APP_KEY_NULO"))
      return
    }

    if (environment.isNullOrEmpty()) {
      releaseJourneyAndInvoke(
        delivered,
        onError,
        serializeBridgeError("ENVIRONMENT_NULO", "ENVIRONMENT_NULO")
      )
      return
    }

    if (provider.isNullOrEmpty()) {
      releaseJourneyAndInvoke(delivered, onError, serializeBridgeError("PROVIDER_NULO", "PROVIDER_NULO"))
      return
    }

    val selectedFeature = when (provider) {
      "FACETEC" -> Features.Facetec
      "IPROOV" -> Features.IProov
      else -> {
        releaseJourneyAndInvoke(
          delivered,
          onError,
          serializeBridgeError("PROVIDER_INVALIDO", "PROVIDER_INVALIDO: $provider")
        )
        return
      }
    }

    val activity = currentActivity
    if (activity == null) {
      releaseJourneyAndInvoke(delivered, onError, serializeBridgeError("NO_ACTIVITY", "NO_ACTIVITY"))
      return
    }

    armJourneyTimeout(delivered, onError)

    LivenessExecutor(appKey, selectedFeature).executeLiveness(
      context = activity,
      environment = environment,
      execOnSuccess = { livenessResult ->
        cleanupThemeCache(activity)
        val jsonResult = convertLivenessResultToJson(livenessResult)
        releaseJourneyAndInvoke(delivered, onSuccess, jsonResult)
      },
      execOnError = { error ->
        cleanupThemeCache(activity)
        releaseJourneyAndInvoke(delivered, onError, error)
      },
      isCustomEnabled = customEnabled,
      theme = theme
    )
  }

  private fun armJourneyTimeout(delivered: AtomicBoolean, onError: Callback?) {
    cancelJourneyTimeout()
    val runnable = Runnable {
      if (journeyInFlight.get()) {
        releaseJourneyAndInvoke(
          delivered,
          onError,
          serializeBridgeError(
            "JOURNEY_TIMEOUT",
            "A jornada de liveness expirou sem resposta."
          )
        )
      }
    }
    journeyTimeoutRunnable = runnable
    mainHandler.postDelayed(runnable, JOURNEY_TIMEOUT_MS)
  }

  private fun cancelJourneyTimeout() {
    journeyTimeoutRunnable?.let { mainHandler.removeCallbacks(it) }
    journeyTimeoutRunnable = null
  }

  private fun cleanupThemeCache(context: android.content.Context) {
    try {
      AssetProcessor(context).cleanupCache()
    } catch (_: Exception) {
    }
  }

  private fun releaseJourneyAndInvoke(
    delivered: AtomicBoolean,
    callback: Callback?,
    payload: String?
  ) {
    cancelJourneyTimeout()
    journeyInFlight.set(false)
    invokeOnceOnUiThread(delivered, callback, payload)
  }

  private fun invokeOnceOnUiThread(
    delivered: AtomicBoolean,
    callback: Callback?,
    payload: String?
  ) {
    if (!delivered.compareAndSet(false, true)) {
      return
    }

    UiThreadUtil.runOnUiThread {
      callback?.invoke(payload)
    }
  }

  private fun convertLivenessResultToJson(livenessResult: br.com.certiface.manager.exports.LivenessResult?): String {
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
      errorObject.put("code", "PARSE_ERROR")
      errorObject.put("message", "Failed to serialize result: ${e.message}")
      errorObject.toString()
    }
  }

  private fun serializeBridgeError(code: String, message: String): String {
    return JSONObject()
      .put("code", code)
      .put("message", message)
      .toString()
  }

  companion object {
    const val NAME = "CertifaceRnSdk"
    const val CAMERA_PERMISSION_REQUEST_CODE = 100
    private const val JOURNEY_TIMEOUT_MS = 5 * 60 * 1000L
  }
}
