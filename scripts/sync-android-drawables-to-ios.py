#!/usr/bin/env python3
import re
import subprocess
from pathlib import Path

SDK_DRAWABLE = Path(
    "/Users/gukiub/AndroidStudioProjects/android-certiface-sdk/CertifaceDesignSystem/src/main/res/drawable"
)
SDK_APP_DRAWABLE = Path(
    "/Users/gukiub/AndroidStudioProjects/android-certiface-sdk/app/src/main/res/drawable"
)
RN_ROOT = Path(__file__).resolve().parent.parent
IOS_TARGETS = [
    RN_ROOT / "example/ios/RnSdkExample/Images.xcassets",
    RN_ROOT / "ios/Resources/Media.xcassets",
]
ANDROID_EXAMPLE = RN_ROOT / "example/android/app/src/main/res/drawable"

VECTOR_EXPORTS = [
    (SDK_DRAWABLE / "fc_arrow_left.xml", "fc_arrow_left"),
    (SDK_DRAWABLE / "arraow_left_black.xml", "arraow_left_black"),
    (SDK_DRAWABLE / "face.xml", "face"),
    (SDK_DRAWABLE / "env.xml", "env"),
    (SDK_DRAWABLE / "close_icon.xml", "close_icon"),
    (SDK_DRAWABLE / "success_icon.xml", "success_icon"),
    (SDK_DRAWABLE / "error_icon.xml", "error_icon"),
    (SDK_DRAWABLE / "return_button.xml", "return_button"),
    (SDK_DRAWABLE / "camera_permission.xml", "camera_permission"),
    (SDK_DRAWABLE / "cancel_button.xml", "cancel_button"),
    (SDK_DRAWABLE / "people.xml", "people"),
]

PNG_EXPORTS = [
    (SDK_APP_DRAWABLE / "woman_liveness_example.png", "woman_liveness_example"),
    (SDK_APP_DRAWABLE / "lamp_example.png", "lamp_example"),
]


def parse_vector(xml_text: str):
    viewport_w = float(re.search(r'viewportWidth="([0-9.]+)"', xml_text).group(1))
    viewport_h = float(re.search(r'viewportHeight="([0-9.]+)"', xml_text).group(1))
    paths = re.findall(
        r'<path[^>]*android:fillColor="([^"]+)"[^>]*android:pathData="([^"]+)"'
        r'|<path[^>]*android:pathData="([^"]+)"[^>]*android:fillColor="([^"]+)"',
        xml_text,
        re.DOTALL,
    )
    if not paths:
        paths = [(m.group(1), m.group(2)) for m in re.finditer(
            r'android:fillColor="([^"]+)"[^>]*android:pathData="([^"]+)"', xml_text
        )]
    else:
        normalized = []
        for p in paths:
            if p[0] and p[1]:
                normalized.append((p[0], p[1]))
            elif p[2] and p[3]:
                normalized.append((p[3], p[2]))
        paths = normalized
    if not paths:
        raise ValueError("no paths found")
    return viewport_w, viewport_h, paths


def vector_to_png(xml_path: Path, png_path: Path, pixel_width: int):
    xml_text = xml_path.read_text(encoding="utf-8")
    viewport_w, viewport_h, paths = parse_vector(xml_text)
    pixel_height = round(pixel_width * viewport_h / viewport_w)
    path_tags = "".join(
        f'<path fill="{fill}" d="{data}"/>' for fill, data in paths
    )
    svg = f'''<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 {viewport_w} {viewport_h}" width="{pixel_width}" height="{pixel_height}">
{path_tags}
</svg>'''
    svg_path = png_path.with_suffix(".svg")
    svg_path.write_text(svg, encoding="utf-8")
    subprocess.run(
        [
            "npx",
            "--yes",
            "@resvg/resvg-js-cli",
            "--fit-width",
            str(pixel_width),
            str(svg_path),
            str(png_path),
        ],
        check=True,
        cwd=RN_ROOT,
    )
    svg_path.unlink(missing_ok=True)


def write_imageset(target_root: Path, name: str, scale_files: dict[int, Path]):
    imageset = target_root / f"{name}.imageset"
    imageset.mkdir(parents=True, exist_ok=True)
    image_entries = []
    for scale, source in sorted(scale_files.items()):
        suffix = "" if scale == 1 else f"@{scale}x"
        filename = f"{name}{suffix}.png"
        (imageset / filename).write_bytes(source.read_bytes())
        image_entries.append(
            f'    {{\n      "filename": "{filename}",\n      "idiom": "universal",\n      "scale": "{scale}x"\n    }}'
        )
    contents = "{\n  \"images\": [\n" + ",\n".join(image_entries) + "\n  ],\n  \"info\": {\n    \"author\": \"xcode\",\n    \"version\": 1\n  }\n}\n"
    (imageset / "Contents.json").write_text(contents, encoding="utf-8")


def export_vector(xml_path: Path, name: str, base_width_1x: int):
    scale_files = {}
    for scale in (1, 2, 3):
        tmp = RN_ROOT / "scripts" / f".tmp_{name}_{scale}x.png"
        vector_to_png(xml_path, tmp, base_width_1x * scale)
        scale_files[scale] = tmp
    for target in IOS_TARGETS:
        write_imageset(target, name, scale_files)
    for tmp in scale_files.values():
        tmp.unlink(missing_ok=True)
    android_dest = ANDROID_EXAMPLE / f"{name}.xml"
    if not android_dest.exists() and xml_path.exists():
        android_dest.write_text(xml_path.read_text(encoding="utf-8"), encoding="utf-8")
    print(f"exported vector {name}")


def export_png(png_path: Path, name: str):
    for target in IOS_TARGETS:
        write_imageset(target, name, {1: png_path})
    android_dest = ANDROID_EXAMPLE / f"{name}.png"
    if png_path.exists():
        android_dest.write_bytes(png_path.read_bytes())
    print(f"exported png {name}")


def main():
    widths = {
        "fc_arrow_left": 11,
        "arraow_left_black": 10,
        "face": 64,
        "env": 64,
        "close_icon": 24,
        "success_icon": 64,
        "error_icon": 64,
        "return_button": 40,
        "camera_permission": 56,
        "cancel_button": 40,
        "people": 64,
    }

    for xml_path, name in VECTOR_EXPORTS:
        if not xml_path.exists():
            print(f"skip missing {xml_path}")
            continue
        export_vector(xml_path, name, widths.get(name, 32))

    for png_path, name in PNG_EXPORTS:
        if not png_path.exists():
            print(f"skip missing {png_path}")
            continue
        export_png(png_path, name)


if __name__ == "__main__":
    main()
