package com.jiangkang.tools.log

import android.util.Log
import com.jiangkang.tools.King.applicationContext
import com.jiangkang.tools.widget.KNotification.createNotification

/**
 * Created by jiangkang on 2018/1/31.
 * description：
 */
object KLog {
    var isDebug = true
    var enableLocalLog = false
    fun d(tag: String, message: String) {
        if (isDebug) {
            if (enableLocalLog) {
                enqueueMessage(tag, message)
            }
            Log.d(tag, message)
        }
    }

    //存在文件即可，入队就插入数据，出队删除数据，仿照chuck
    private fun enqueueMessage(tag: String, message: String) {
        createNotification(applicationContext, -1, "Ktools", String.format("%s:%s", tag, message), null)
    }
}