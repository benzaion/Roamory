#!/usr/bin/env bash
# Lightweight Swift validation that runs on Linux (no Xcode / macOS required).
#
# CoastTrip is an iOS app that only fully builds with Xcode on macOS, but the
# Swift sources can still be syntax-checked and style-linted on Linux with the
# open-source Swift toolchain. This is what CI / Cloud Agents can verify.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SRC_DIR="${REPO_ROOT}/CoastTrip"

# Make the swiftly-managed toolchain available if it is installed.
if [ -f "${HOME}/.local/share/swiftly/env.sh" ]; then
  # shellcheck disable=SC1091
  . "${HOME}/.local/share/swiftly/env.sh"
  hash -r || true
fi

if ! command -v swiftc >/dev/null 2>&1; then
  echo "error: swiftc not found. Run scripts/cloud-setup.sh first." >&2
  exit 1
fi

echo "Swift toolchain: $(swift --version | head -n1)"
echo

echo "==> Parse-checking Swift sources"
parse_failures=0
while IFS= read -r file; do
  if swiftc -parse "$file" >/tmp/coasttrip-parse.log 2>&1; then
    echo "  ok    ${file#"${REPO_ROOT}/"}"
  else
    echo "  FAIL  ${file#"${REPO_ROOT}/"}"
    cat /tmp/coasttrip-parse.log
    parse_failures=$((parse_failures + 1))
  fi
done < <(find "$SRC_DIR" -name '*.swift' | sort)

echo
echo "==> Style lint (swift format lint)"
lint_output="$(swift format lint --recursive "$SRC_DIR" 2>&1 || true)"
if [ -n "$lint_output" ]; then
  echo "$lint_output"
  lint_count="$(printf '%s\n' "$lint_output" | grep -c 'warning:' || true)"
  echo "  ${lint_count} style warning(s) (advisory; does not fail the check)"
else
  echo "  no style warnings"
fi

echo
if [ "$parse_failures" -ne 0 ]; then
  echo "RESULT: ${parse_failures} file(s) failed to parse" >&2
  exit 1
fi
echo "RESULT: all Swift sources parsed successfully"
