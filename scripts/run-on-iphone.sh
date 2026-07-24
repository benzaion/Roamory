#!/bin/bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
SCHEME="CoastTrip"
BUNDLE_ID="com.benzyg.CoastTrip"

find_device_id() {
  # Prefer Xcode's view of connected devices
  local xcode_device
  xcode_device="$(xcodebuild -showdestinations -scheme "${SCHEME}" -project "${PROJECT_DIR}/CoastTrip.xcodeproj" 2>/dev/null \
    | awk -F'id: |, name:' '/platform:iOS/ && /name:iPhone/ { print $2; exit }' \
    | tr -d ' ')"
  if [[ -n "${xcode_device}" && "${xcode_device}" != *placeholder* ]]; then
    echo "${xcode_device}"
    return 0
  fi

  # Fall back to USB serial number from ioreg
  python3 - <<'PY'
import re, subprocess
out = subprocess.check_output(["ioreg", "-p", "IOUSB", "-l"], text=True, stderr=subprocess.DEVNULL)
blocks = out.split("+o ")
for block in blocks:
    if '"kUSBProductString" = "iPhone"' in block or '"USB Product Name" = "iPhone"' in block:
        m = re.search(r'"USB Serial Number" = "([^"]+)"', block)
        if m:
            print(m.group(1))
            break
PY
}

echo "Looking for a connected iPhone..."
DEVICE_ID="$(find_device_id || true)"

if [[ -z "${DEVICE_ID}" ]]; then
  echo "No iPhone detected."
  echo "1. Connect your iPhone via USB"
  echo "2. Unlock it and tap Trust This Computer"
  echo "3. Open Xcode once and confirm the device appears in the toolbar"
  exit 1
fi

echo "Found iPhone: ${DEVICE_ID}"
echo "Building ${SCHEME}..."

cd "${PROJECT_DIR}"
xcodebuild \
  -scheme "${SCHEME}" \
  -destination "id=${DEVICE_ID}" \
  -allowProvisioningUpdates \
  -derivedDataPath "${PROJECT_DIR}/build" \
  build

APP_PATH="${PROJECT_DIR}/build/Build/Products/Debug-iphoneos/${SCHEME}.app"
if [[ ! -d "${APP_PATH}" ]]; then
  echo "Build finished but app bundle not found at: ${APP_PATH}"
  exit 1
fi

echo "Installing on device..."
xcrun devicectl device install app --device "${DEVICE_ID}" "${APP_PATH}"

echo "Launching ${BUNDLE_ID}..."
xcrun devicectl device process launch --device "${DEVICE_ID}" "${BUNDLE_ID}"

echo "Done. CoastTrip should now be running on your iPhone."
