package com.jiangkang.tools.utils
import android.content.Context
import android.content.res.AssetFileDescriptor
import java.io.FileDescriptor

object AssetsUtils {

    fun openAssetFd(context: Context,fileName:String):FileDescriptor?{
        return context.assets.openFd(fileName).fileDescriptor
    }

    fun testAssets(context: Context){
        val assetManager = context.assets

    }

}