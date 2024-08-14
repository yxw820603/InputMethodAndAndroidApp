
# so library
add_library(udpinput SHARED
    main.cpp
)

target_link_libraries(udpinput PRIVATE Fcitx5::Core)
set_target_properties(udpinput PROPERTIES PREFIX "")
install(TARGETS udpinput DESTINATION "${FCITX_INSTALL_LIBDIR}/fcitx5")

# target_include_directories(InputMethod PUBLIC fcitx5core fcitx5utils)



# config file
configure_file(udpinput-addon.conf.in udpinput-addon.conf)
install(FILES "${CMAKE_CURRENT_BINARY_DIR}/udpinput-addon.conf" RENAME udpinput.conf DESTINATION "${FCITX_INSTALL_PKGDATADIR}/addon")

# registration file
install(FILES "udpinput.conf" DESTINATION "${FCITX_INSTALL_PKGDATADIR}/inputmethod")




