//
//  RnSDK+Callbacks.swift
//  Pods
//
//  Created by Gabriel Catelli Goulart on 21/07/25.
//

import CertifaceSDK

extension RnSdkImpl: LivenessCallback {
  public func onSuccess(_ resultData: LivenessResult) {
    let response: [String: Any] = [
      "status": "success",
      "result": [
        "valid": resultData.valid,
        "codId": resultData.codId as Any,
        "protocol": resultData.protocol as Any,
      ],
    ]

    if let jsonData = try? JSONSerialization.data(withJSONObject: response),
      let jsonString = String(data: jsonData, encoding: .utf8)
    {
      onSuccessCallback?(jsonString)
    } else {
      onErrorCallback?("Failed to serialize response")
    }

    onSuccessCallback = nil
    onErrorCallback = nil
  }

  public func onError(_ error: LivenessError) {
    let response: [String: Any] = [
      "status": "error",
      "message": "[\(error.code)]: \(error.message)",
    ]

    if let jsonData = try? JSONSerialization.data(withJSONObject: response),
      let jsonString = String(data: jsonData, encoding: .utf8)
    {
      onErrorCallback?(jsonString)
    } else {
      onErrorCallback?("Failed to serialize error response")
    }

    onSuccessCallback = nil
    onErrorCallback = nil
  }

  func getRootViewController() -> UIViewController? {
    let windowScene = UIApplication.shared.connectedScenes
      .compactMap { $0 as? UIWindowScene }
      .first { $0.activationState == .foregroundActive }

    let keyWindow = windowScene?.windows.first { $0.isKeyWindow }

    return keyWindow?.rootViewController
  }
}
