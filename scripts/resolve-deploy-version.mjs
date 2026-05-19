import { readFileSync, writeFileSync, mkdirSync, existsSync } from 'node:fs';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';

const repoRoot = join(dirname(fileURLToPath(import.meta.url)), '..');
const pkgPath = join(repoRoot, 'example/package.json');
const statePath = join(repoRoot, 'example/.deploy-build-state.json');
const envPath = join(repoRoot, 'example/deploy-version.env');

const pkg = JSON.parse(readFileSync(pkgPath, 'utf8'));
const marketingVersion = String(pkg.version || '1.0.0');
const [major, minor, patch] = marketingVersion.split('.').map((n) => Number(n) || 0);
const semverBase = major * 1_000_000 + minor * 1_000 + patch * 10;

let lastVersionCode = 0;
let cachedBuildNumber = 0;

if (existsSync(statePath)) {
  try {
    const state = JSON.parse(readFileSync(statePath, 'utf8'));
    if (Number.isFinite(state.lastVersionCode)) {
      lastVersionCode = state.lastVersionCode;
    }
    if (state.version === marketingVersion && Number.isFinite(state.buildNumber)) {
      cachedBuildNumber = state.buildNumber;
    }
  } catch {
    lastVersionCode = 0;
    cachedBuildNumber = 0;
  }
}

let buildNumber = cachedBuildNumber > 0 ? cachedBuildNumber + 1 : 1;
let versionCode = semverBase + buildNumber;

if (lastVersionCode >= semverBase) {
  versionCode = Math.max(versionCode, lastVersionCode + 1);
}

const runNumber = Number(process.env.GITHUB_RUN_NUMBER || 0);
if (runNumber > 0) {
  versionCode = Math.max(versionCode, semverBase + runNumber);
}

const minOverride = Number(process.env.MIN_ANDROID_VERSION_CODE || 0);
if (minOverride > versionCode) {
  versionCode = minOverride;
}

const pkgFloor = Number(pkg.versionCode);
if (pkgFloor > versionCode) {
  versionCode = pkgFloor;
}

buildNumber = versionCode - semverBase;
const versionLabel = `${marketingVersion} (${buildNumber})`;

const shellQuote = (value) => `"${String(value).replace(/\\/g, '\\\\').replace(/"/g, '\\"')}"`;

mkdirSync(dirname(statePath), { recursive: true });
writeFileSync(
  statePath,
  JSON.stringify(
    {
      version: marketingVersion,
      buildNumber,
      lastVersionCode: versionCode,
    },
    null,
    2
  ) + '\n'
);

const jsonPath = join(repoRoot, 'example/deploy-version.json');
writeFileSync(
  jsonPath,
  JSON.stringify(
    {
      versionName: marketingVersion,
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
  `VERSION_NAME=${shellQuote(marketingVersion)}`,
  `VERSION_CODE=${versionCode}`,
  `BUILD_NUMBER=${buildNumber}`,
  `VERSION_LABEL=${shellQuote(versionLabel)}`,
  `SEMVER_BASE=${semverBase}`,
];
writeFileSync(envPath, envLines.join('\n') + '\n');

const githubEnv = process.env.GITHUB_ENV;
if (githubEnv) {
  writeFileSync(githubEnv, `VERSION_NAME=${marketingVersion}\n`, { flag: 'a' });
  writeFileSync(githubEnv, `VERSION_CODE=${versionCode}\n`, { flag: 'a' });
  writeFileSync(githubEnv, `BUILD_NUMBER=${buildNumber}\n`, { flag: 'a' });
  writeFileSync(githubEnv, `VERSION_LABEL=${versionLabel}\n`, { flag: 'a' });
  writeFileSync(githubEnv, `SEMVER_BASE=${semverBase}\n`, { flag: 'a' });
}

if (process.argv.includes('--github-output') && process.env.GITHUB_OUTPUT) {
  const output = [
    `version_name=${marketingVersion}`,
    `version_code=${versionCode}`,
    `build_number=${buildNumber}`,
    `version_label=${versionLabel}`,
  ].join('\n');
  writeFileSync(process.env.GITHUB_OUTPUT, output + '\n', { flag: 'a' });
}

console.log(`Deploy: ${versionLabel} → versionCode ${versionCode} (base ${semverBase})`);
