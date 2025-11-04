#!/bin/bash

set -e

BUILD_TYPE=${1:-debug}
RELEASE_NOTES=${2:-"Automated build for testing"}

echo "🚀 Building and distributing Android app..."
echo "Build type: $BUILD_TYPE"
echo "Release notes: $RELEASE_NOTES"

cd "$(dirname "$0")/.."

if [ "$BUILD_TYPE" = "release" ]; then
    echo "📦 Building release APK..."
    npm run build:android:release
    
    echo "🚀 Uploading to Firebase App Distribution..."
    cd android && ./gradlew appDistributionUploadRelease --no-daemon --console=plain
else
    echo "📦 Building debug APK..."
    npm run build:android:debug
    
    echo "🚀 Uploading to Firebase App Distribution..."
    cd android && ./gradlew appDistributionUploadDebug --no-daemon --console=plain
fi

echo "✅ Distribution completed successfully!"
echo "📱 Check Firebase Console for the distributed app"
