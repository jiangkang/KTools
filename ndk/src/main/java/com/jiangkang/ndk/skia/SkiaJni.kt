package com.jiangkang.ndk.skia

import android.content.res.AssetManager
import com.jiangkang.tools.utils.LogUtils

object SkiaJni {

    init {
        LogUtils.d("load libskia")
        System.loadLibrary("ktools")
    }

    external fun drawText(filename:String)

    external fun drawShape(filename:String)

    external fun createNativeApp(assetManager: AssetManager): Long
    external fun destroyNativeApp(handle: Long)

}