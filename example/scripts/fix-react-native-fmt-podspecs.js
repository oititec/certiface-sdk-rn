const fs = require('fs');
const path = require('path');

const dir = path.join(
  __dirname,
  '..',
  'node_modules',
  'react-native',
  'third-party-podspecs'
);

function patchIfNeeded(file, replace) {
  const p = path.join(dir, file);
  if (!fs.existsSync(p)) {
    return;
  }
  let c = fs.readFileSync(p, 'utf8');
  if (!c.includes('12.1.0')) {
    return;
  }
  fs.writeFileSync(p, replace(c));
}

patchIfNeeded('fmt.podspec', (c) => c.replace(/12\.1\.0/g, '11.0.2'));
patchIfNeeded('RCT-Folly.podspec', (c) =>
  c.replace(
    'spec.dependency "fmt", "12.1.0"',
    'spec.dependency "fmt", "11.0.2"'
  )
);
