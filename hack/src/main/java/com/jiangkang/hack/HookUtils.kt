package com.jiangkang.hack

import android.app.Instrumentation
import android.view.View
import com.jiangkang.hack.activity.LogInstrumentation
import com.jiangkang.tools.utils.LogUtils
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

    fun hookViewOnclickListener() {
        val listener = Proxy.newProxyInstance(
                View.OnClickListener::class.java.classLoader, arrayOf<Class<*>>(View.OnClickListener::class.java)
        ) { proxy, method, args ->
            LogUtils.d(TAG, "点击按钮之前")
            val result = method.invoke(proxy, *args)
            LogUtils.d(TAG, "点击按钮之后")
            result
        } as View.OnClickListener
    }
}