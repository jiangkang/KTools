package com.jiangkang.tools.utils

object SoLibUtils {

    fun loadLibraryDynamic(name: String, url: String) {
        System.loadLibrary(name)
        System.load(name)
    }

}