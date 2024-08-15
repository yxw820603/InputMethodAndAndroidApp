cmake_minimum_required(VERSION 3.10)
project(UDPInput)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
# set(CMAKE_INSTALL_PREFIX "/usr") # 自己编译安装用这个
set(CMAKE_INSTALL_PREFIX "${CMAKE_BINARY_DIR}/package")  # 在github上用CI编译用这个

find_package(Fcitx5Core REQUIRED)
find_package(Fcitx5Config REQUIRED)
find_package(Fcitx5Utils REQUIRED)

include("${FCITX_INSTALL_CMAKECONFIG_DIR}/Fcitx5Utils/Fcitx5CompilerSettings.cmake")

add_subdirectory(src)


set(CPACK_GENERATOR "DEB")
set(CPACK_PACKAGE_NAME "UDPInput")
set(CPACK_PACKAGE_VERSION "1.0.0")
set(CPACK_PACKAGE_CONTACT "yxw820603@gmail.com")
set(CPACK_DEBIAN_PACKAGE_MAINTAINER "yxw820603")
set(CPACK_DEBIAN_PACKAGE_DEPENDS "fcitx5, libfcitx5core7")

# set(CPACK_INCLUDE_TOPLEVEL_DIRECTORY OFF)
# set(CPACK_INSTALL_CMAKE_PROJECTS "${CMAKE_BINARY_DIR}/package;UDPInput;ALL;/")


include(CPack)
