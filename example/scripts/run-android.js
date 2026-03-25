const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');

const exampleRoot = path.join(__dirname, '..');
const localPropsPath = path.join(exampleRoot, 'android', 'local.properties');

function readSdkDir(filePath) {
  if (!fs.existsSync(filePath)) {
    return null;
  }
  const text = fs.readFileSync(filePath, 'utf8');
  for (const line of text.split(/\r?\n/)) {
    const t = line.trim();
    if (!t || t.startsWith('#')) {
      continue;
    }
    const eq = t.indexOf('=');
    if (eq === -1) {
      continue;
    }
    const key = t.slice(0, eq).trim();
    if (key !== 'sdk.dir') {
      continue;
    }
    let val = t.slice(eq + 1).trim();
    val = val.replace(/\\:/g, ':').replace(/\\\\/g, '\\');
    return val;
  }
  return null;
}

const sdkDir = readSdkDir(localPropsPath);
if (sdkDir && !process.env.ANDROID_HOME) {
  process.env.ANDROID_HOME = sdkDir;
}
if (sdkDir && !process.env.ANDROID_SDK_ROOT) {
  process.env.ANDROID_SDK_ROOT = sdkDir;
}

const cliJs = path.join(exampleRoot, 'node_modules', 'react-native', 'cli.js');
const extraArgs = process.argv.slice(2);
const result = spawnSync(
  process.execPath,
  [cliJs, 'run-android', ...extraArgs],
  { cwd: exampleRoot, stdio: 'inherit', env: process.env }
);

if (result.error) {
  throw result.error;
}
process.exit(result.status === null ? 1 : result.status);
