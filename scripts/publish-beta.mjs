import { execSync } from 'node:child_process';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const pkgPath = path.join(root, 'package.json');
const betaPath = path.join(root, 'package.beta.json');
const backupPath = path.join(root, '.package.json.publish-backup');

const pkg = JSON.parse(fs.readFileSync(pkgPath, 'utf8'));
const betaOverrides = JSON.parse(fs.readFileSync(betaPath, 'utf8'));

const publishPkg = { ...pkg, ...betaOverrides };

fs.writeFileSync(backupPath, `${JSON.stringify(pkg, null, 2)}\n`);
fs.writeFileSync(pkgPath, `${JSON.stringify(publishPkg, null, 2)}\n`);

try {
  execSync('yarn prepare', { cwd: root, stdio: 'inherit' });
  execSync('npm publish --access public', { cwd: root, stdio: 'inherit' });
  console.log(`Published ${publishPkg.name}@${publishPkg.version}`);
} finally {
  fs.writeFileSync(pkgPath, fs.readFileSync(backupPath, 'utf8'));
  fs.unlinkSync(backupPath);
}
