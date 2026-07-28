import type { CertifaceTheme } from '@certiface/sdk';
import { customTheme } from './customTheme';

export const invalidCustomTheme: CertifaceTheme = {
  ...customTheme,
  instructions: {
    ...customTheme.instructions,
    colors: {
      ...customTheme.instructions?.colors,
      background: 'not-a-color',
    },
  },
};
