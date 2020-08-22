package com.jiangkang.hack

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import com.jiangkang.hack.activity.LogInstrumentation
import com.jiangkang.hack.hook.ProxyInstrumentation
import com.jiangkang.tools.utils.LogUtils
import java.lang.reflect.Field
import java.lang.reflect.Proxy

/**
 * Created by jiangkang on 2018/3/14.
 * description：
 */
object HookUtils {
    private const val TAG = "Hook"

    @Throws(Exception::class)
    fun attachBaseContext() {
        //获取ActivityThread类
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread")
        currentActivityThreadMethod.isAccessible = true
        val currentActivityThread = currentActivityThreadMethod.invoke(null)
        val mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation")
        mInstrumentationField.isAccessible = true
        val mInstrumentation = mInstrumentationField[currentActivityThread] as Instrumentation
        val logInstrumentation: Instrumentation = LogInstrumentation(mInstrumentation)
        //替换
        mInstrumentationField[currentActivityThread] = logInstrumentation
    }

    @JvmStatic
    fun hookInstrumentation(sourceActivity:Activity){
        val instrumentationField = Activity::class.java.getDeclaredField("mInstrumentation")
        if (!instrumentationField.isAccessible){
            instrumentationField.isAccessible = true
        }
        val originInstrumentation:Instrumentation = instrumentationField.get(sourceActivity) as Instrumentation
        instrumentationField.set(sourceActivity,ProxyInstrumentation(originInstrumentation))
    }

}