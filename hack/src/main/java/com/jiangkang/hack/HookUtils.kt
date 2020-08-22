package com.jiangkang.hack

import android.app.Activity
import android.app.Instrumentation
import com.jiangkang.hack.hook.ActivityStartingCallback
import com.jiangkang.hack.hook.ProxyInstrumentation


/**
 * Created by jiangkang on 2018/3/14.
 * description：
 */
object HookUtils {
    private const val TAG = "Hook"

    @Throws(Exception::class)
    fun attachBaseContext(callback: ActivityStartingCallback) {
        //获取ActivityThread类
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread")
        currentActivityThreadMethod.isAccessible = true
        val currentActivityThread = currentActivityThreadMethod.invoke(null)
        val mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation")
        mInstrumentationField.isAccessible = true
        val mInstrumentation = mInstrumentationField[currentActivityThread] as Instrumentation
        val logInstrumentation: Instrumentation = ProxyInstrumentation(mInstrumentation,callback)
        //替换
        mInstrumentationField[currentActivityThread] = logInstrumentation
    }

    @JvmStatic
    fun hookInstrumentationWithActivity(sourceActivity:Activity, callback: ActivityStartingCallback){
        val instrumentationField = Activity::class.java.getDeclaredField("mInstrumentation")
        if (!instrumentationField.isAccessible){
            instrumentationField.isAccessible = true
        }
        val originInstrumentation:Instrumentation = instrumentationField.get(sourceActivity) as Instrumentation
        instrumentationField.set(sourceActivity,ProxyInstrumentation(originInstrumentation,callback))
    }

}