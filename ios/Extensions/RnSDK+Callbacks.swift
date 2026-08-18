import CertifaceSDK
import UIKit

extension RnSdkImpl: LivenessCallback {
  public func onSuccess(_ resultData: LivenessResult) {
    let response: [String: Any] = [
      "status": "success",
      "result": [
        "valid": resultData.valid,
        "codID": "\(resultData.codId)",
        "cause": resultData.cause ?? "",
        "protocol": "\(resultData.protocol)",
        "scanResultBlob": resultData.scanResultBlob ?? "",
      ],
    ]

    if let jsonData = try? JSONSerialization.data(withJSONObject: response),
      let jsonString = String(data: jsonData, encoding: .utf8)
    {
      deliverSuccess(jsonString)
    } else {
      deliverError(
        NativeErrorPayload.serialize(
          code: "PARSE_ERROR",
          message: "Failed to serialize response"
        )
      )
    }
  }

  public func onError(_ error: LivenessError) {
    deliverError(
      NativeErrorPayload.serialize(
        code: "\(error.code)",
        message: error.message
      )
    )
  }

  func getRootViewController() -> UIViewController? {
    let windowScene = UIApplication.shared.connectedScenes
      .compactMap { $0 as? UIWindowScene }
      .first { $0.activationState == .foregroundActive }
      ?? UIApplication.shared.connectedScenes
        .compactMap { $0 as? UIWindowScene }
        .first

    let keyWindow = windowScene?.windows.first { $0.isKeyWindow }
      ?? windowScene?.windows.first

    guard var controller = keyWindow?.rootViewController else {
      return nil
    }

    while let presented = controller.presentedViewController {
      controller = presented
    }

    return controller
  }
}
