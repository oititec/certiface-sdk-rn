import CertifaceSDK
import UIKit

@objc public class RnSdkImpl: NSObject {
  private let callbackLock = NSLock()
  private var onSuccessCallback: ((String) -> Void)?
  private var onErrorCallback: ((String) -> Void)?
  private var journeyInFlight = false
  private var journeyTimeoutWorkItem: DispatchWorkItem?
  private static let journeyTimeoutSeconds: TimeInterval = 300

  @objc public func startJourney(
    appKey: String,
    environment: String,
    provider: String,
    isCustomEnabled: Bool,
    theme: [String: Any]?,
    onSuccess: @escaping (String) -> Void,
    onError: @escaping (String) -> Void
  ) {
    guard beginJourney(onSuccess: onSuccess, onError: onError) else {
      onError(
        NativeErrorPayload.serialize(
          code: "JOURNEY_IN_PROGRESS",
          message: "Uma jornada de liveness já está em andamento."
        )
      )
      return
    }

    guard provider == "IPROOV" else {
      if provider == "FACETEC" {
        deliverError(
          NativeErrorPayload.serialize(
            code: "UNSUPPORTED_OPERATION",
            message: "FaceTec agora usa fluxo SaaS com journeyToken. Use CertifaceSDK.startSaasJourney(token, environment, ...) ao invés de startJourney(appKey, environment, 'FACETEC', ...)."
          )
        )
        return
      }

      deliverError(
        NativeErrorPayload.serialize(
          code: "PROVIDER_INVALIDO",
          message: "PROVIDER_INVALIDO: \(provider)"
        )
      )
      return
    }

    let sdkEnvironment = resolveEnvironment(environment)

    DispatchQueue.main.async { [self] in
      var iproovCustomization = IProovCustomization.builder().build()
      var showInstructionsScreen = true

      if isCustomEnabled {
        do {
          iproovCustomization = try ThemeFactory.createIProovCustomization(from: theme)
        } catch let error as ThemeCustomizationError {
          deliverError(NativeErrorPayload.fromThemeError(error))
          return
        } catch {
          deliverError(
            NativeErrorPayload.serialize(
              code: "INVALID_PARAMS",
              message: "Parâmetros de customização inválidos."
            )
          )
          return
        }

        showInstructionsScreen = resolveShowInstructionsScreen(from: theme)
      }

      let options = LivenessManagerOptions
        .builder(appKey: appKey, environment: sdkEnvironment)
        .setShowInstructionsScreen(showInstructionsScreen)
        .setIProovCustomization(iproovCustomization)
        .build()
      let manager = CertifaceSDKFactory.createLivenessManager(for: .iproov)

      start(manager: manager, options: options)
    }
  }

  @objc public func startSaasJourney(
    token: String,
    environment: String,
    isCustomEnabled: Bool,
    theme: [String: Any]?,
    onSuccess: @escaping (String) -> Void,
    onError: @escaping (String) -> Void
  ) {
    guard beginJourney(onSuccess: onSuccess, onError: onError) else {
      onError(
        NativeErrorPayload.serialize(
          code: "JOURNEY_IN_PROGRESS",
          message: "Uma jornada de liveness já está em andamento."
        )
      )
      return
    }

    let sdkEnvironment = resolveEnvironment(environment)

    DispatchQueue.main.async { [self] in
      var facetecCustomization = FacetecCustomization.builder().build()
      var fortfaceCustomization = FortfaceCustomization.builder().build()
      var saasCustomization = SaasCustomization.builder().build()
      var showInstructionsScreen = true

      if isCustomEnabled {
        do {
          facetecCustomization = try ThemeFactory.createFacetecCustomization(from: theme)
          fortfaceCustomization = try ThemeFactory.createFortfaceCustomization(from: theme)
          saasCustomization = try ThemeFactory.createSaasCustomization(from: theme)
        } catch let error as ThemeCustomizationError {
          deliverError(NativeErrorPayload.fromThemeError(error))
          return
        } catch {
          deliverError(
            NativeErrorPayload.serialize(
              code: "INVALID_PARAMS",
              message: "Parâmetros de customização inválidos."
            )
          )
          return
        }

        showInstructionsScreen = resolveShowInstructionsScreen(from: theme)
      }

      let options = LivenessManagerOptions
        .builder(token: token, environment: sdkEnvironment)
        .setShowInstructionsScreen(showInstructionsScreen)
        .setFacetecCustomization(facetecCustomization)
        .setFortfaceCustomization(fortfaceCustomization)
        .setSaasCustomization(saasCustomization)
        .build()
      let manager = CertifaceSDKFactory.createLivenessManager(for: .saas)

      start(manager: manager, options: options)
    }
  }

  func deliverSuccess(_ payload: String) {
    let callback = takeSuccessCallback()
    callback?(payload)
  }

  func deliverError(_ payload: String) {
    let callback = takeErrorCallback()
    callback?(payload)
  }

  private func beginJourney(
    onSuccess: @escaping (String) -> Void,
    onError: @escaping (String) -> Void
  ) -> Bool {
    callbackLock.lock()
    defer { callbackLock.unlock() }
    guard !journeyInFlight else { return false }
    journeyInFlight = true
    onSuccessCallback = onSuccess
    onErrorCallback = onError
    armJourneyTimeoutLocked()
    return true
  }

  private func takeSuccessCallback() -> ((String) -> Void)? {
    callbackLock.lock()
    defer { callbackLock.unlock() }
    cancelJourneyTimeoutLocked()
    let callback = onSuccessCallback
    onSuccessCallback = nil
    onErrorCallback = nil
    journeyInFlight = false
    return callback
  }

  private func takeErrorCallback() -> ((String) -> Void)? {
    callbackLock.lock()
    defer { callbackLock.unlock() }
    cancelJourneyTimeoutLocked()
    let callback = onErrorCallback
    onSuccessCallback = nil
    onErrorCallback = nil
    journeyInFlight = false
    return callback
  }

  private func armJourneyTimeoutLocked() {
    cancelJourneyTimeoutLocked()
    let workItem = DispatchWorkItem { [weak self] in
      self?.deliverError(
        NativeErrorPayload.serialize(
          code: "JOURNEY_TIMEOUT",
          message: "A jornada de liveness expirou sem resposta."
        )
      )
    }
    journeyTimeoutWorkItem = workItem
    DispatchQueue.main.asyncAfter(
      deadline: .now() + Self.journeyTimeoutSeconds,
      execute: workItem
    )
  }

  private func cancelJourneyTimeoutLocked() {
    journeyTimeoutWorkItem?.cancel()
    journeyTimeoutWorkItem = nil
  }

  private func start(
    manager: LivenessManager,
    options: LivenessManagerOptions
  ) {
    guard let viewController = getRootViewController() else {
      deliverError(
        NativeErrorPayload.serialize(
          code: "NO_ACTIVITY",
          message: "Cannot get rootViewController"
        )
      )
      return
    }

    manager.start(at: viewController, options: options, callback: self)
  }

  private func resolveEnvironment(_ environment: String) -> CertifaceSDK.Environment {
    environment == "PRD" ? .prd : .hml
  }

  private func resolveShowInstructionsScreen(from theme: [String: Any]?) -> Bool {
    guard let themeData = theme,
          let instructionsTheme = themeData["instructions"] as? [String: Any],
          let configuration = instructionsTheme["configuration"] as? [String: Any],
          let showInstruction = configuration["showInstructionScreen"] as? Bool else {
      return true
    }
    return showInstruction
  }
}
