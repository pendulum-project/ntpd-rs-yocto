ntpd-rs
============
This layer provides ntpd-rs for yocto.
ntpd-rs is a tool for synchronizing your computer's clock, implementing the NTP and NTS protocols.
It is written in Rust, with a focus on security and stability. It includes both client and server support.

Dependencies
============

  URI: git://git.openembedded.org/openembedded-core
  branch: wrynose

  URI: git://github.com/rust-embedded/meta-rust-bin
  branch: master

packages
============
ntpd-rs provides the following packages:

- `ntpd-rs`: The main ntp-rs daemon: `ntp-daemon` and the control tool `ntp-ctl`
- `ntpd-rs-doc`: The man pages and doc files
- `ntpd-rs-dbg`: The debug symbols for `ntp-daemon` and `ntp-ctl`
- `ntpd-rs-metrics`: The metrics daemon: `ntp-metrics-exporter`
- `ntpd-rs-metrics-doc`: The metrics man page
- `ntpd-rs-metrics-dbg`: The debug symbols for `ntp-metrics-exporter`

Add these to your `IMAGE_INSTALL` depending on what you need.
