cmake_minimum_required(VERSION 3.4.1)
set(DIR_INCLUDE ${CMAKE_CURRENT_SOURCE_DIR}/include)
set(LIB_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs)

include_directories(
        ${DIR_INCLUDE}/${ANDROID_ABI}
#        ${DIR_INCLUDE}/${ANDROID_ABI}/libavcodec
#        ${DIR_INCLUDE}/${ANDROID_ABI}/libavdevice
#        ${DIR_INCLUDE}/${ANDROID_ABI}/libavfilter
#        ${DIR_INCLUDE}/${ANDROID_ABI}/libavformat
#        ${DIR_INCLUDE}/${ANDROID_ABI}/libavutil
#        ${DIR_INCLUDE}/${ANDROID_ABI}/libswresample
#        ${DIR_INCLUDE}/${ANDROID_ABI}/libswscale
)

find_library(lib_lib_swscale ${LIB_PATH}/${ANDROID_ABI}/liblib_swscale.so)
find_library(lib_swresample ${LIB_PATH}/${ANDROID_ABI}/libswresample.so)
find_library(lib_avutil ${LIB_PATH}/${ANDROID_ABI}/libavutil.so)
find_library(lib_avformat ${LIB_PATH}/${ANDROID_ABI}/libavformat.so)
find_library(lib_avfilter ${LIB_PATH}/${ANDROID_ABI}/libavfilter.so)
find_library(lib_avdevice ${LIB_PATH}/${ANDROID_ABI}/libavdevice.so)
find_library(lib_avcodec ${LIB_PATH}/${ANDROID_ABI}/libavcodec.so)


add_library( # Sets the name of the library.
        ktools

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ktools.cpp)

target_include_directories(ktools PRIVATE
        ${DIR_INCLUDE}/${ANDROID_ABI}/libavcodec
        ${DIR_INCLUDE}/${ANDROID_ABI}/libavdevice
        ${DIR_INCLUDE}/${ANDROID_ABI}/libavfilter
        ${DIR_INCLUDE}/${ANDROID_ABI}/libavformat
        ${DIR_INCLUDE}/${ANDROID_ABI}/libavutil
        ${DIR_INCLUDE}/${ANDROID_ABI}/libswresample
        ${DIR_INCLUDE}/${ANDROID_ABI}/libswscale
        )

target_link_libraries( # Specifies the target library.
        ktools
        android
        ${OPENGL_LIB}
        jnigraphics
        # Links the target library to the log library
        # included in the NDK.
        log
        lib_avcodec
        lib_avdevice
        lib_avfilter
        lib_avformat
        lib_avutil
        lib_swresample
        lib_swscale
        )