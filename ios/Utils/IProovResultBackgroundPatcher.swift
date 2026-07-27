import CertifaceIProov
import UIKit

enum IProovResultBackgroundPatcher {
  private static let customizationBaseOffset = 16
  private static let errorBackgroundOffset = 8
  private static let retryBackgroundOffset = 16

  static func applyErrorAndRetryBackgrounds(
    to builder: IProovResultCustomizationBuilder,
    errorBackground: UIColor?,
    retryBackground: UIColor?
  ) -> IProovResultCustomizationBuilder {
    guard errorBackground != nil || retryBackground != nil else {
      return builder
    }

    let customizationBase = Unmanaged.passUnretained(builder as AnyObject)
      .toOpaque()
      .advanced(by: customizationBaseOffset)

    writeUIColor(errorBackground, at: customizationBase.advanced(by: errorBackgroundOffset))
    writeUIColor(retryBackground, at: customizationBase.advanced(by: retryBackgroundOffset))

    return builder
  }

  private static func writeUIColor(_ color: UIColor?, at address: UnsafeMutableRawPointer) {
    let slot = address.assumingMemoryBound(to: UInt.self)
    if slot.pointee != 0, let existing = UnsafeRawPointer(bitPattern: slot.pointee) {
      Unmanaged<AnyObject>.fromOpaque(existing).release()
      slot.pointee = 0
    }
    guard let color else { return }
    slot.pointee = UInt(bitPattern: Unmanaged.passRetained(color).toOpaque())
  }
}
