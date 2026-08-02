//
//  RnSdkImpl.swift
//  RnSdk
//
//  Created by Gabriel Catelli Goulart on 21/07/25.
//

import CertifaceSDK
import UIKit

@objc public class RnSdkImpl: NSObject {
  var onSuccessCallback: ((String) -> Void)?
  var onErrorCallback: ((String) -> Void)?

  @objc public func startJourney(
    appKey: String,
    environment: String,
    provider: String,
    isCustomEnabled: Bool,
    theme: [String: Any]?,
    onSuccess: @escaping (String) -> Void,
    onError: @escaping (String) -> Void
  ) {
    self.onSuccessCallback = onSuccess
    self.onErrorCallback = onError

    guard provider == "IPROOV" else {
      if provider == "FACETEC" {
        onError(
          NativeErrorPayload.serialize(
            code: "UNSUPPORTED_OPERATION",
            message: "FaceTec agora usa fluxo SaaS com journeyToken. Use CertifaceSDK.startSaasJourney(token, environment, ...) ao invés de startJourney(appKey, environment, 'FACETEC', ...)."
          )
        )
        return
      }

      onError(
        NativeErrorPayload.serialize(
          code: "PROVIDER_INVALIDO",
          message: "PROVIDER_INVALIDO: \(provider)"
        )
      )
      return
    }

    let sdkEnvironment = resolveEnvironment(environment)
    var iproovCustomization = IProovCustomization.builder().build()
    var showInstructionsScreen = true

    if isCustomEnabled {
      do {
        iproovCustomization = try ThemeFactory.createIProovCustomization(from: theme)
      } catch let error as ThemeCustomizationError {
        onError(NativeErrorPayload.fromThemeError(error))
        return
      } catch {
        onError(
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

    start(manager: manager, options: options, onError: onError)
  }

  @objc public func startSaasJourney(
    token: String,
    environment: String,
    isCustomEnabled: Bool,
    theme: [String: Any]?,
    onSuccess: @escaping (String) -> Void,
    onError: @escaping (String) -> Void
  ) {
    self.onSuccessCallback = onSuccess
    self.onErrorCallback = onError

    let sdkEnvironment = resolveEnvironment(environment)
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
        onError(NativeErrorPayload.fromThemeError(error))
        return
      } catch {
        onError(
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

    start(manager: manager, options: options, onError: onError)
  }

  private func start(
    manager: LivenessManager,
    options: LivenessManagerOptions,
    onError: @escaping (String) -> Void
  ) {
    DispatchQueue.main.async { [weak self] in
      guard let self, let viewController = getRootViewController() else {
        onError(
          NativeErrorPayload.serialize(
            code: "NO_ACTIVITY",
            message: "Cannot get rootViewController"
          )
        )
        return
      }

      manager.start(at: viewController, options: options, callback: self)
    }
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
