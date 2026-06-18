const fs = require('fs');
const path = require('path');

const exampleRoot = path.resolve(__dirname, '..');
const dest = path.join(exampleRoot, 'src/config/credentials.ts');
const src = path.join(exampleRoot, 'src/config/credentials.example.ts');

if (!fs.existsSync(dest)) {
  fs.copyFileSync(src, dest);
}
