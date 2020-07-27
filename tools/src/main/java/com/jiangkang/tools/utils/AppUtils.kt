package com.jiangkang.tools.utils

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.jiangkang.tools.King
import java.util.*

/**
 * Created by jiangkang on 2017/9/8.
 *
 * 与App相关的工具类
 */

object AppUtils {

}

/**
 * 判断当前APP是否为Debug模式
 * */
val isDebug: Boolean
    get() {
        val appInfo: ApplicationInfo = King.applicationContext.applicationInfo
        if ((appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            return true
        }
        return false
    }

/**
 * 回退栈中的Activity 列表
 * */
val activityListInStack: List<String>
    get() {
        val activities = King.getsActivityList()
        val list = ArrayList<String>()
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next() as Activity
            list.add(activity.componentName.className)
        }
        return list
    }

/**
 * 当前的Activity
 * */
val currentActivity: String
    get() {
        return King.topActivityWeakRef?.get()!!.componentName.className
    }

/**
 * App 版本名
 */
val appVersionName: String
    get() {
        try {
            val packageInfo = packageInfo
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "unknown"
    }

private val packageInfo: PackageInfo
    @Throws(PackageManager.NameNotFoundException::class)
    get() {
        val pm = King.applicationContext.packageManager
        return pm.getPackageInfo(King.applicationContext.packageName, PackageManager.GET_CONFIGURATIONS)
    }

