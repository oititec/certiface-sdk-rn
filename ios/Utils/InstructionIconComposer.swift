import UIKit

enum InstructionIconScaleMode {
  case fit
  case fillBounds
  case crop

  static func from(_ value: String?) -> InstructionIconScaleMode {
    switch value?.lowercased().replacingOccurrences(of: "_", with: "") {
    case "fillbounds":
      return .fillBounds
    case "crop":
      return .crop
    default:
      return .fit
    }
  }
}

final class InstructionIconComposer {
  static func compose(
    icon: UIImage,
    backgroundColor: UIColor,
    borderColor: UIColor?,
    size: CGFloat,
    scaleMode: InstructionIconScaleMode
  ) -> UIImage {
    let dimension = max(size, 1)
    let rect = CGRect(x: 0, y: 0, width: dimension, height: dimension)
    let format = UIGraphicsImageRendererFormat.default()
    format.scale = UIScreen.main.scale
    let renderer = UIGraphicsImageRenderer(size: rect.size, format: format)

    return renderer.image { context in
      let cgContext = context.cgContext
      let circleRect = rect.insetBy(dx: 0.5, dy: 0.5)

      cgContext.addEllipse(in: circleRect)
      backgroundColor.setFill()
      cgContext.fillPath()

      if let borderColor {
        cgContext.addEllipse(in: circleRect)
        cgContext.setStrokeColor(borderColor.cgColor)
        cgContext.setLineWidth(1)
        cgContext.strokePath()
      }

      cgContext.saveGState()
      cgContext.addEllipse(in: circleRect)
      cgContext.clip()

      switch scaleMode {
      case .fit:
        let inset = dimension * (10.0 / 60.0)
        let iconRect = rect.insetBy(dx: inset, dy: inset)
        draw(icon: icon, in: iconRect, contentMode: .scaleAspectFit)
      case .fillBounds:
        draw(icon: icon, in: rect, contentMode: .scaleToFill)
      case .crop:
        draw(icon: icon, in: rect, contentMode: .scaleAspectFill)
      }

      cgContext.restoreGState()
    }
  }

  private static func draw(icon: UIImage, in rect: CGRect, contentMode: UIView.ContentMode) {
    guard rect.width > 0, rect.height > 0 else { return }

    let drawRect: CGRect
    let iconWidth = max(icon.size.width, 1)
    let iconHeight = max(icon.size.height, 1)
    switch contentMode {
    case .scaleToFill:
      drawRect = rect
    case .scaleAspectFill:
      let widthRatio = rect.width / iconWidth
      let heightRatio = rect.height / iconHeight
      let scale = max(widthRatio, heightRatio)
      let width = iconWidth * scale
      let height = iconHeight * scale
      drawRect = CGRect(
        x: rect.midX - width / 2,
        y: rect.midY - height / 2,
        width: width,
        height: height
      )
    default:
      let widthRatio = rect.width / iconWidth
      let heightRatio = rect.height / iconHeight
      let scale = min(widthRatio, heightRatio)
      let width = iconWidth * scale
      let height = iconHeight * scale
      drawRect = CGRect(
        x: rect.midX - width / 2,
        y: rect.midY - height / 2,
        width: width,
        height: height
      )
    }

    icon.draw(in: drawRect)
  }
}
