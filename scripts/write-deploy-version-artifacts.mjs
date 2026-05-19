import { writeFileSync, mkdirSync } from 'node:fs';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';

const repoRoot = join(dirname(fileURLToPath(import.meta.url)), '..');
const exampleDir = join(repoRoot, 'example');

const versionName = process.env.VERSION_NAME ?? '';
const versionCode = Number(process.env.VERSION_CODE ?? 0);
const buildNumber = Number(process.env.BUILD_NUMBER ?? 0);
const versionLabel = process.env.VERSION_LABEL ?? '';
const semverBase = versionCode - buildNumber;

if (!versionName || !versionCode || !versionLabel) {
  console.error('VERSION_NAME, VERSION_CODE e VERSION_LABEL são obrigatórios.');
  process.exit(1);
}

const shellQuote = (value) => `"${String(value).replace(/\\/g, '\\\\').replace(/"/g, '\\"')}"`;

mkdirSync(exampleDir, { recursive: true });

writeFileSync(
  join(exampleDir, 'deploy-version.json'),
  JSON.stringify(
    {
      versionName,
      versionCode,
      buildNumber,
      versionLabel,
      semverBase,
    },
    null,
    2
  ) + '\n'
);

const envLines = [
  `VERSION_NAME=${shellQuote(versionName)}`,
  `VERSION_CODE=${versionCode}`,
  `BUILD_NUMBER=${buildNumber}`,
  `VERSION_LABEL=${shellQuote(versionLabel)}`,
  `SEMVER_BASE=${semverBase}`,
];
writeFileSync(join(exampleDir, 'deploy-version.env'), envLines.join('\n') + '\n');

console.log(`Versão aplicada: ${versionLabel} (code ${versionCode})`);
