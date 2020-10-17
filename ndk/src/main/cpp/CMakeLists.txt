project(ktools)
cmake_minimum_required(VERSION 3.4.1)
set(DIR_INCLUDE ${CMAKE_CURRENT_SOURCE_DIR}/include)
set(LIB_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs)

include_directories(
        ${DIR_INCLUDE}/${ANDROID_ABI}
)

message("Android_ABI:${ANDROID_ABI}")
message("LIB_PATH:${LIB_PATH}")

add_library(LIB_SKIA SHARED IMPORTED)
set_target_properties(LIB_SKIA PROPERTIES IMPORTED_LOCATION
        ${LIB_PATH}/${ANDROID_ABI}/libskia.so
        )

#find_library(LIB_SKIA skia ${LIB_PATH}/${ANDROID_ABI})
if(LIB_SKIA-NOTFOUND)
    message("没有找到Skia")
endif(LIB_SKIA-NOTFOUND)

add_library( # Sets the name of the library.
        ktools

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ktools.cpp
        skia_demo.cpp
        utils.cpp
        skia_viewer.cpp
        )

target_include_directories(ktools PRIVATE
        ${DIR_INCLUDE}/${ANDROID_ABI}
        )

target_link_libraries( # Specifies the target library.
        ktools
        android
        jnigraphics
        log
        nativewindow
        LIB_SKIA
        )