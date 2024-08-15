
# so library
add_library(UDPInput SHARED
    main.cpp
)

target_link_libraries(UDPInput PRIVATE Fcitx5::Core)
set_target_properties(UDPInput PROPERTIES PREFIX "") #确保生成的文件不是  libxxx.so,而是xxx.so
install(TARGETS UDPInput DESTINATION "${FCITX_INSTALL_LIBDIR}/fcitx5")

# config file
configure_file(UDPInput-addon.conf.in UDPInput-addon.conf)
install(FILES "${CMAKE_CURRENT_BINARY_DIR}/UDPInput-addon.conf" RENAME UDPInput.conf DESTINATION "${FCITX_INSTALL_PKGDATADIR}/addon")

# registration file
install(FILES "UDPInput.conf" DESTINATION "${FCITX_INSTALL_PKGDATADIR}/inputmethod")
