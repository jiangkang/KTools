package com.jiangkang.tools.utils

import android.util.Log

/**
 * Created by jiangkang on 2017/9/28.
 */
object LogUtils {
    @JvmStatic
    fun d(msg: Any?) {
        Log.d("KTools", msg.toString())
    }

    @JvmStatic
    fun d(format: String?, vararg msg: Any?) {
        Log.d("KTools", msg.toString())
    }

    @JvmStatic
    fun json(jsonString: String?) {
        Log.d("KTools",jsonString)
    }
}