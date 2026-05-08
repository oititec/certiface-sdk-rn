const path = require('path');
const pkg = require('../package.json');

module.exports = {
  assets: ['./node_modules/react-native-vector-icons/Fonts'],
  project: {
    ios: {
      automaticPodsInstallation: true,
    },
  },
  dependencies: {
    [pkg.name]: {
      root: path.join(__dirname, '..'),
      platforms: {
        ios: {},
        android: {
          packageImportPath: 'import br.com.certiface.rn.sdk.RnSdkPackage;',
          packageInstance: 'new RnSdkPackage()',
        },
      },
    },
  },
};
