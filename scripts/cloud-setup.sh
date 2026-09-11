#!/usr/bin/env bash
# Idempotent setup for a Linux Cloud Agent / CI environment.
#
# CoastTrip is a native iOS (SwiftUI + SwiftData) app; a full build/run requires
# Xcode on macOS. On Linux we install the open-source Swift toolchain so the
# sources can be parsed, style-linted, and edited with LSP support.
set -euo pipefail

SWIFTLY_HOME="${HOME}/.local/share/swiftly"

ensure_system_libs() {
  # The Swift runtime needs a handful of shared libraries. They are normally
  # already present (and captured in the environment snapshot); only install
  # them if something is missing.
  if ldconfig -p 2>/dev/null | grep -q 'libncurses.so.6'; then
    return 0
  fi
  echo "==> Installing Swift runtime system libraries"
  # The default archive.ubuntu.com (plain HTTP) is not reachable from Cloud
  # Agent VMs; use an HTTPS mirror when available.
  if [ -f /etc/apt/sources.list.d/ubuntu.sources ] && \
     ! grep -q 'mirrors.edge.kernel.org' /etc/apt/sources.list.d/ubuntu.sources; then
    sudo sed -i \
      -e 's|http://archive.ubuntu.com/ubuntu/|https://mirrors.edge.kernel.org/ubuntu/|g' \
      -e 's|http://security.ubuntu.com/ubuntu/|https://mirrors.edge.kernel.org/ubuntu/|g' \
      /etc/apt/sources.list.d/ubuntu.sources
  fi
  sudo apt-get update -qq
  sudo apt-get install -y --no-install-recommends \
    gnupg2 libcurl4-openssl-dev libpython3-dev libxml2-dev libncurses-dev libz3-dev
}

ensure_swiftly() {
  if [ -x "${SWIFTLY_HOME}/bin/swiftly" ]; then
    return 0
  fi
  echo "==> Installing swiftly"
  local tmp
  tmp="$(mktemp -d)"
  curl -fsSL -o "${tmp}/swiftly.tar.gz" \
    "https://download.swift.org/swiftly/linux/swiftly-$(uname -m).tar.gz"
  tar xzf "${tmp}/swiftly.tar.gz" -C "${tmp}"
  "${tmp}/swiftly" init --assume-yes --skip-install
  rm -rf "${tmp}"
}

ensure_toolchain() {
  # shellcheck disable=SC1091
  . "${SWIFTLY_HOME}/env.sh"
  hash -r || true
  if ! command -v swiftc >/dev/null 2>&1; then
    echo "==> Installing latest Swift toolchain"
    swiftly install latest --assume-yes
    hash -r || true
  fi
}

ensure_system_libs
ensure_swiftly
ensure_toolchain

# shellcheck disable=SC1091
. "${SWIFTLY_HOME}/env.sh"
hash -r || true
echo "==> Swift ready: $(swift --version | head -n1)"
