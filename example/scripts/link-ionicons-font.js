const fs = require('fs');
const path = require('path');

const root = path.join(__dirname, '..');
const source = path.join(
  root,
  'node_modules',
  'react-native-vector-icons',
  'Fonts',
  'Ionicons.ttf'
);

const androidTarget = path.join(
  root,
  'android',
  'app',
  'src',
  'main',
  'assets',
  'fonts',
  'Ionicons.ttf'
);

if (!fs.existsSync(source)) {
  console.warn(
    '[link-ionicons-font] Ionicons.ttf não encontrado em node_modules; pulando cópia.'
  );
  process.exit(0);
}

fs.mkdirSync(path.dirname(androidTarget), { recursive: true });
fs.copyFileSync(source, androidTarget);
