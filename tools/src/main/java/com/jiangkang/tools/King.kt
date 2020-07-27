package com.jiangkang.tools

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by jiangkang on 2017/9/6.
 */
object King {
    private lateinit var sContext: Application
    private val sActivityList: MutableList<Activity> = LinkedList()
    var topActivityWeakRef: WeakReference<Activity>? = null
        private set
    private val sCallbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            sActivityList.add(activity)
        }

        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {
            setTopActivity(activity)
        }

        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
        override fun onActivityDestroyed(activity: Activity) {
            sActivityList.remove(activity)
        }
    }

    fun init(context: Application) {
        sContext = context
        sContext.registerActivityLifecycleCallbacks(sCallbacks)
    }

    @JvmStatic
    val applicationContext: Context
        get() {
            return sContext
        }

    private fun setTopActivity(activity: Activity) {
        topActivityWeakRef = WeakReference(activity)
    }

    fun getsActivityList(): List<Activity> {
        return sActivityList
    }
}