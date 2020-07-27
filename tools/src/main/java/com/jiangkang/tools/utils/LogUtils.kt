package com.jiangkang.tools.utils

import com.orhanobut.logger.Logger

/**
 * Created by jiangkang on 2017/9/28.
 */
object LogUtils {
    private const val LOG_DEFAULT = "LogUtils"
    fun d(msg: Any?) {
        Logger.d(msg)
    }

    fun d(format: String?, vararg msg: Any?) {
        Logger.d(format!!, *msg)
    }

    fun json(jsonString: String?) {
        Logger.json(jsonString)
    }
}