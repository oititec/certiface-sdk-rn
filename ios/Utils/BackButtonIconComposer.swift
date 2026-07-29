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
      let widthRatio = size.width / icon.size.width
      let heightRatio = size.height / icon.size.height
      let scale = min(widthRatio, heightRatio)
      let width = icon.size.width * scale
      let height = icon.size.height * scale
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
