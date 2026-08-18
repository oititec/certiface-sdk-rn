import UIKit

enum FontNameResolver {
  private static let lock = NSLock()
  private static var cache: [String: String?] = [:]

  static func resolve(_ fontName: String) -> String? {
    let trimmed = fontName.trimmingCharacters(in: .whitespacesAndNewlines)
    guard !trimmed.isEmpty else { return nil }

    lock.lock()
    if let cached = cache[trimmed] {
      lock.unlock()
      return cached
    }
    lock.unlock()

    let resolved = resolveUncached(trimmed)

    lock.lock()
    cache[trimmed] = resolved
    lock.unlock()
    return resolved
  }

  private static func resolveUncached(_ trimmed: String) -> String? {
    if UIFont(name: trimmed, size: UIFont.systemFontSize) != nil {
      return trimmed
    }

    let pathComponent = (trimmed as NSString).lastPathComponent
    if UIFont(name: pathComponent, size: UIFont.systemFontSize) != nil {
      return pathComponent
    }

    let baseName = (pathComponent as NSString).deletingPathExtension
    if UIFont(name: baseName, size: UIFont.systemFontSize) != nil {
      return baseName
    }

    let wanted = baseName.lowercased()
    for family in UIFont.familyNames {
      if family.lowercased().contains(wanted) {
        let familyCandidates = UIFont.fontNames(forFamilyName: family)
        if let first = familyCandidates.first {
          return first
        }
      }
      for candidate in UIFont.fontNames(forFamilyName: family) {
        let candidateNormalized = candidate.lowercased()
        if candidateNormalized == wanted || candidateNormalized.contains(wanted) {
          return candidate
        }
      }
    }

    return nil
  }
}
