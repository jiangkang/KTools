package com.jiangkang.hack.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log

/**
 * Created by jiangkang on 2018/3/14.
 * description：
 */
class LogInstrumentation(var mBaseInstrumentation: Instrumentation) : Instrumentation() {
    fun execStartActivity(
            who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?,
            intent: Intent?, requestCode: Int, options: Bundle?): ActivityResult {
        Log.d(TAG, String.format("\nwho = %s \n contextThread = %s, \n token = %s, \n target = %s,\n intent = %s,\nrequestCode = %s,\n options = %s",
                who, contextThread, token, target, intent, requestCode, options))
        return try {
            val exeStartActivity = Instrumentation::class.java.getDeclaredMethod(
                    "execStartActivity",
                    Context::class.java,
                    IBinder::class.java,
                    IBinder::class.java,
                    Activity::class.java,
                    Intent::class.java,
                    Int::class.javaPrimitiveType,
                    Bundle::class.java
            )
            exeStartActivity.isAccessible = true
            exeStartActivity.invoke(mBaseInstrumentation,
                    who,
                    contextThread,
                    token,
                    target,
                    intent,
                    requestCode,
                    options
            ) as ActivityResult
        } catch (e: Exception) {
            throw RuntimeException("you have to solve the bug")
        }
    }

    companion object {
        private const val TAG = "Hook"
    }

}