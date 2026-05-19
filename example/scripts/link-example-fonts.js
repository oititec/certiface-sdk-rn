const fs = require('fs');
const path = require('path');

const root = path.join(__dirname, '..');

const copies = [
  {
    source: path.join(
      root,
      'node_modules',
      'react-native-vector-icons',
      'Fonts',
      'Ionicons.ttf'
    ),
    target: path.join(
      root,
      'android',
      'app',
      'src',
      'main',
      'assets',
      'fonts',
      'Ionicons.ttf'
    ),
    label: 'Ionicons.ttf',
  },
  {
    source: path.join(root, 'ios', 'RnSdkExample', 'fonts', 'sixty.ttf'),
    targets: [
      path.join(
        root,
        'android',
        'app',
        'src',
        'main',
        'assets',
        'fonts',
        'sixty.ttf'
      ),
      path.join(
        root,
        'android',
        'app',
        'src',
        'main',
        'res',
        'font',
        'sixty.ttf'
      ),
    ],
    label: 'sixty.ttf',
  },
];

for (const entry of copies) {
  if (!fs.existsSync(entry.source)) {
    console.warn(
      `[link-example-fonts] ${entry.label} não encontrado em ${entry.source}; pulando.`
    );
    continue;
  }

  const targets = entry.targets ?? [entry.target];
  for (const target of targets) {
    fs.mkdirSync(path.dirname(target), { recursive: true });
    fs.copyFileSync(entry.source, target);
  }
}
