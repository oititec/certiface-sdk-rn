import UIKit

final class BackButtonIconComposer {
  private static let canvasSide: CGFloat = 24

  static func prepare(_ icon: UIImage) -> UIImage {
    let size = CGSize(width: canvasSide, height: canvasSide)
    let format = UIGraphicsImageRendererFormat.default()
    format.scale = icon.scale > 0 ? icon.scale : UIScreen.main.scale
    format.opaque = false
    let renderer = UIGraphicsImageRenderer(size: size, format: format)

    return renderer.image { _ in
      let iconWidth = max(icon.size.width, 1)
      let iconHeight = max(icon.size.height, 1)
      let widthRatio = size.width / iconWidth
      let heightRatio = size.height / iconHeight
      let scale = min(widthRatio, heightRatio)
      let width = iconWidth * scale
      let height = iconHeight * scale
      let drawRect = CGRect(
        x: (size.width - width) / 2,
        y: (size.height - height) / 2,
        width: width,
        height: height
      )
      icon.draw(in: drawRect)
    }.withRenderingMode(.alwaysTemplate)
  }
}
