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

function tryLaunchFallbackActivity() {
  const sdkRoot = process.env.ANDROID_SDK_ROOT || process.env.ANDROID_HOME;
  if (!sdkRoot) {
    return false;
  }

  const adb = path.join(sdkRoot, 'platform-tools', 'adb');
  if (!fs.existsSync(adb)) {
    return false;
  }

  const devices = spawnSync(adb, ['devices'], { encoding: 'utf8' });
  const rows = (devices.stdout || '').split(/\r?\n/);
  const onlineDevice = rows
    .map((row) => row.trim())
    .find((row) => row.endsWith('\tdevice'));

  if (!onlineDevice) {
    return false;
  }

  const deviceId = onlineDevice.split('\t')[0];
  const packageCandidates = [
    'br.com.oititec.rn.sdk.example',
    'br.com.oititec.rncertifacesdk.example',
  ];

  for (const pkg of packageCandidates) {
    const start = spawnSync(
      adb,
      [
        '-s',
        deviceId,
        'shell',
        'am',
        'start',
        '-n',
        `${pkg}/${pkg}.MainActivity`,
        '-a',
        'android.intent.action.MAIN',
        '-c',
        'android.intent.category.LAUNCHER',
      ],
      { encoding: 'utf8' }
    );

    if (start.status === 0) {
      console.log(`Fallback launch succeeded with package ${pkg}`);
      return true;
    }
  }

  return false;
}

if (result.error) {
  throw result.error;
}
if (result.status && result.status !== 0) {
  const fallbackOk = tryLaunchFallbackActivity();
  process.exit(fallbackOk ? 0 : result.status);
}
process.exit(result.status === null ? 1 : result.status);
