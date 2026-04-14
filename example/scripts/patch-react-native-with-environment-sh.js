const fs = require('fs');
const path = require('path');

function patchFile(filePath, needle, fixed) {
  if (!fs.existsSync(filePath)) {
    return;
  }
  const content = fs.readFileSync(filePath, 'utf8');
  if (!content.includes(needle)) {
    return;
  }
  fs.writeFileSync(filePath, content.replace(needle, fixed));
}

const rnScriptsDirs = [
  path.join(__dirname, '..', 'node_modules', 'react-native', 'scripts'),
  path.join(__dirname, '..', '..', 'node_modules', 'react-native', 'scripts'),
];

for (const rnDir of rnScriptsDirs) {
  patchFile(
    path.join(rnDir, 'xcode', 'with-environment.sh'),
    `if [ -n "$1" ]; then
  $1
fi`,
    `if [ -n "$1" ]; then
  "$1"
fi`
  );

  patchFile(
    path.join(rnDir, 'react_native_pods_utils', 'script_phases.rb'),
    '/bin/sh -c "$WITH_ENVIRONMENT $SCRIPT_PHASES_SCRIPT"',
    '"$WITH_ENVIRONMENT" "$SCRIPT_PHASES_SCRIPT"'
  );
}
