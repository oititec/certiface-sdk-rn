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
        "codID": "\(resultData.codId)",
        "cause": resultData.cause ?? "",
        "protocol": resultData.protocol,
        "scanResultBlob": resultData.scanResultBlob ?? "",
      ],
    ]

    if let jsonData = try? JSONSerialization.data(withJSONObject: response),
      let jsonString = String(data: jsonData, encoding: .utf8)
    {
      onSuccessCallback?(jsonString)
    } else {
      onErrorCallback?(
        NativeErrorPayload.serialize(
          code: "PARSE_ERROR",
          message: "Failed to serialize response"
        )
      )
    }

    onSuccessCallback = nil
    onErrorCallback = nil
  }

  public func onError(_ error: LivenessError) {
    onErrorCallback?(
      NativeErrorPayload.serialize(
        code: "\(error.code)",
        message: error.message
      )
    )

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
