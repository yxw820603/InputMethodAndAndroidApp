cmake_minimum_required(VERSION 3.10)
project(UDPInput)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_INSTALL_PREFIX "/usr")

find_package(Fcitx5Core REQUIRED)
find_package(Fcitx5Config REQUIRED)
find_package(Fcitx5Utils REQUIRED)

include("${FCITX_INSTALL_CMAKECONFIG_DIR}/Fcitx5Utils/Fcitx5CompilerSettings.cmake")

add_subdirectory(src)

include(CPack)

set(CPACK_GENERATOR "DEB")
set(CPACK_PACKAGE_NAME "UDPInput")
set(CPACK_PACKAGE_VERSION "1.0.0")
set(CPACK_PACKAGE_CONTACT "your-email@example.com")
set(CPACK_DEBIAN_PACKAGE_MAINTAINER "Your Name")
set(CPACK_DEBIAN_PACKAGE_DEPENDS "fcitx5, libfcitx5core7")
