SUMMARY = "Full-featured implementation of NTP with NTS support"
DESCRIPTION = "ntpd-rs is a tool for synchronizing your computer's clock, implementing the NTP and NTS protocols. It is written in Rust, with a focus on security and stability. It includes both client and server support."

HOMEPAGE = "https://trifectatech.org/projects/ntpd-rs/"

LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "\
    file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
    file://LICENSE-MIT;md5=ae0a4dfb4bd6ae01c2bfdb05ef2972d3 \
"

inherit cargo_bin systemd useradd pkgconfig

DEPENDS = "openssl"

SRC_URI = "git://github.com/pendulum-project/ntpd-rs.git;protocol=https;tag=v1.9.0;nobranch=1;destsuffix=${S}"
SRC_URI[sha256sum] = "6cc79cd2743957296276c35db9a2328384e898ece6d9c53d561b8b36c8df8224"
SRCREV = "46ec9bb4d5b6cb24f814f5543d85b9138afb4cba"

CARGO_BUILD_FLAGS:append = " --no-default-features"
CARGO_FEATURES = "rustcrypto openssl"

EXTRA_RUSTFLAGS += " --remap-path-prefix=${WORKDIR}=/usr/src/debug/${PN}/${PV}"

do_compile[network] = "1"

do_install:append() {
    # Man
    install -D -m 0644 ${S}/docs/precompiled/man/ntp-ctl.8 ${D}${mandir}/man8/ntp-ctl.8
    install -m 0644 ${S}/docs/precompiled/man/ntp-daemon.8 ${D}${mandir}/man8/ntp-daemon.8
    install -m 0644 ${S}/docs/precompiled/man/ntp-metrics-exporter.8 ${D}${mandir}/man8/ntp-metrics-exporter.8
    install -D -m 0644 ${S}/docs/precompiled/man/ntp.toml.5 ${D}${mandir}/man5/ntp.toml.5

    # Config
    install -D -m 0644 ${S}/docs/examples/conf/ntp.toml.default ${D}${sysconfdir}/${PN}/ntp.toml

    # Systemd
    install -D -m 0644 ${S}/docs/examples/conf/${PN}.preset ${D}${systemd_unitdir}/system-preset/${PN}.preset
    install -D -m 0644 ${S}/docs/examples/conf/${PN}.service ${D}${systemd_unitdir}/system/${PN}.service
    install -m 0644 ${S}/docs/examples/conf/${PN}-metrics.service ${D}${systemd_unitdir}/system/${PN}-metrics.service

    # Docs
    install -D -m 0644 ${S}/docs/examples/conf/ntp.toml.default ${D}${docdir}/${PN}/ntp.toml.default
    install -m 0644  ${S}/COPYRIGHT ${D}${docdir}/${PN}/COPYRIGHT
    install -m 0644  ${S}/LICENSE-APACHE ${D}${docdir}/${PN}/LICENSE-APACHE
    install -m 0644  ${S}/LICENSE-MIT ${D}${docdir}/${PN}/LICENSE-MIT
    install -m 0644  ${S}/CHANGELOG.md ${D}${docdir}/${PN}/CHANGELOG.md
    install -m 0644  ${S}/README.md ${D}${docdir}/${PN}/README.md
}

PACKAGES =+ " ${PN}-metrics ${PN}-metrics-doc ${PN}-metrics-dbg"

FILES:${PN}:append = "\
    ${mandir}/man8/ntp-ctl.8 \
    ${mandir}/man8/ntp-daemon.8 \
    ${mandir}/man5/ntp-toml.5 \
    ${sysconfdir}/${PN}/ntp.toml \
    ${systemd_unitdir}/system-preset/${PN}.preset \
    ${systemd_unitdir}/system/${PN}.service \
    ${docdir}/${PN}/ntp.toml.default \
    ${docdir}/${PN}/COPYRIGHT \
    ${docdir}/${PN}/LICENSE-APACHE \
    ${docdir}/${PN}/LICENSE-MIT \
    ${docdir}/${PN}/CHANGELOG.md \
    ${docdir}/${PN}/README.md"

FILES:${PN}-metrics = "\
    ${systemd_unitdir}/system/${PN}-metrics.service \
    ${bindir}/ntp-metrics-exporter"
FILES:${PN}-metrics-doc = "${mandir}/man8/ntp-metrics-exporter.8"
FILES:${PN}-metrics-dbg = "${bindir}/.debug/ntp-metrics-exporter"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_PACKAGES = "${PN} ${PN}-metrics"
SYSTEMD_SERVICE:${PN} = "${PN}.service"
SYSTEMD_SERVICE:${PN}-metrics = "${PN}-metrics.service"

RCONFLICTS:${PN} = "ntp ntimed chrony"

USERADD_PACKAGES = "${PN} ${PN}-metrics"
USERADD_PARAM:${PN} = "--system --home ${localstatedir}/lib/${PN} --user-group ${PN}"
USERADD_PARAM:${PN}-metrics = "--system --home ${localstatedir}/lib/${PN}-metrics --user-group ${PN}-observe"
