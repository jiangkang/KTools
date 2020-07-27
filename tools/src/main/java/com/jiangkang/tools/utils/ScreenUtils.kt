package com.jiangkang.tools.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import org.jetbrains.anko.keyguardManager
import org.jetbrains.anko.windowManager

/**
 * Created by jiangkang on 2018/3/22.
 * description：屏幕相关工具类
 */

object ScreenUtils {

    /*
    * 获取系统的屏幕亮度，值在0~255之间，0最暗，255最亮
    *
    * @param context
    *
    * @return 亮度值（0~255）
    *
    * */
    fun getScreenBrightness(context: Context): Int {
        if (getScreenBrightnessMode(context) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            setScreenBrightnessManualMode(context)
        }
        return Settings.System.getInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS
        )
    }


    fun setScreenBrightness(context: Context, brightness: Int) {
        if (getScreenBrightnessMode(context) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            setScreenBrightnessManualMode(context)
        }
        var value = brightness
        if (brightness < 0) {
            value = 0
        } else if (brightness > 255) {
            value = 255
        }

        Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                value
        )
    }


    fun setCurrentWindowScreenBrightness(activity: Activity, brightness: Int) {
        var layoutParams: WindowManager.LayoutParams = activity.window.attributes
        layoutParams.screenBrightness = brightness / 255.0f
        activity.window.attributes = layoutParams
    }


    private fun getScreenBrightnessMode(context: Context): Int {
        return Settings.System.getInt(context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE
        )
    }


    private fun setScreenBrightnessManualMode(context: Context) {
        Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
    }


}


val Context.screenWidth: Int
    get() {
        val point = Point()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.defaultDisplay.getRealSize(point)
        } else {
            windowManager.defaultDisplay.getSize(point)
        }
        return point.x
    }

val Context.screenHeight: Int
    get() {
        val point = Point()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.defaultDisplay.getRealSize(point)
        } else {
            windowManager.defaultDisplay.getSize(point)
        }
        return point.y
    }

val Context.screenSize: Pair<Int, Int>
    get() {
        val point = Point()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.defaultDisplay.getRealSize(point)
        } else {
            windowManager.defaultDisplay.getSize(point)
        }
        return Pair(point.x, point.y)
    }

val screenDensity: Float = Resources.getSystem().displayMetrics.density

fun Activity.setScreenPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Activity.setScreenLanscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

val Context.isScreenLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.isScreenPortrait: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

val Context.isScreenLocked: Boolean
    get() = keyguardManager.isKeyguardLocked


