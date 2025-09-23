import { Image } from 'react-native';

export class AssetProcessor {
  static async processThemeAssets(theme: any): Promise<any> {
    if (!theme) return theme;

    const processedTheme = JSON.parse(JSON.stringify(theme));

    if (processedTheme.instructions?.assets) {
      processedTheme.instructions.assets = await this.processInstructionsAssets(
        processedTheme.instructions.assets
      );
    }

    if (processedTheme.iproov?.assets) {
      processedTheme.iproov.assets = await this.processIProovAssets(
        processedTheme.iproov.assets
      );
    }

    if (processedTheme.facetec?.assets) {
      processedTheme.facetec.assets = await this.processFacetecAssets(
        processedTheme.facetec.assets
      );
    }

    return processedTheme;
  }

  private static async processInstructionsAssets(assets: any): Promise<any> {
    const processedAssets: any = {};

    if (assets.logo) {
      processedAssets.logo = await this.convertAssetToBase64(assets.logo);
    }

    return processedAssets;
  }

  private static async processIProovAssets(assets: any): Promise<any> {
    const processedAssets: any = {};

    if (assets.logo) {
      processedAssets.logo = await this.convertAssetToBase64(assets.logo);
    }

    if (assets.closeButton) {
      processedAssets.closeButton = await this.convertAssetToBase64(
        assets.closeButton
      );
    }

    return processedAssets;
  }

  private static async processFacetecAssets(assets: any): Promise<any> {
    const processedAssets: any = {};

    if (assets.overlayBrandingImage) {
      processedAssets.overlayBrandingImage = await this.convertAssetToBase64(
        assets.overlayBrandingImage
      );
    }

    if (assets.cancelButtonCustomImage) {
      processedAssets.cancelButtonCustomImage = await this.convertAssetToBase64(
        assets.cancelButtonCustomImage
      );
    }

    if (assets.resultScreenCustomActivityIndicatorImage) {
      processedAssets.resultScreenCustomActivityIndicatorImage = await this.convertAssetToBase64(
        assets.resultScreenCustomActivityIndicatorImage
      );
    }

    return processedAssets;
  }

  private static async convertAssetToBase64(asset: any): Promise<string> {
    return new Promise((resolve, reject) => {
      if (typeof asset === 'string') {
        resolve(asset);
        return;
      }

      const assetSource = Image.resolveAssetSource(asset);
      if (!assetSource) {
        reject(new Error('Failed to resolve asset source'));
        return;
      }

      fetch(assetSource.uri)
        .then((response) => response.blob())
        .then((blob) => {
          const reader = new FileReader();
          reader.onload = () => {
            if (typeof reader.result === 'string') {
              resolve(reader.result);
            } else {
              reject(new Error('Failed to convert asset to base64'));
            }
          };
          reader.onerror = () => reject(new Error('Failed to read asset blob'));
          reader.readAsDataURL(blob);
        })
        .catch((error) => {
          console.warn('Failed to convert asset to base64, using URI:', error);
          resolve(assetSource.uri);
        });
    });
  }
}
