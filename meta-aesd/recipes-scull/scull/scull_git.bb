# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-hogimn.git;protocol=ssh;branch=master \
           file://scull.patch \
           file://S97scull \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "dd63dfa5db7d5b2c8c5cf77a4b7436033bf22bef"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git"

# TODO: Ensure your recipe uses inherit module and ensure this is placed at the top
# of your recipe and before any FILES_${PN}.
inherit module

# Sets the target for installing kernel modules to "install"
MODULES_INSTALL_TARGET = "install"
# Adds additional makefile options for building kernel modules,
# setting the kernel source directory and the path to the scull module source files.
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR} M=${S}/scull"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${bindir}/scull_load"
FILES:${PN} += "${bindir}/scull_unload"
FILES:${PN} += "${sysconfdir}/init.d/S97scull"

# References class which handles install scripts
inherit update-rc.d
# Flag your package as one which uses init scripts
INITSCRIPT_PACKAGES = "${PN}"
# Your start script name
INITSCRIPT_NAME:${PN} = "S97scull"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
        install -d ${D}${bindir}
        install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${S}/scull/scull_load ${D}${bindir}
        install -m 0755 ${S}/scull/scull_unload ${D}${bindir}
        install -m 0755 ${S}/scull/scull.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}
        install -m 0755 ${WORKDIR}/S97scull ${D}${sysconfdir}/init.d
}
