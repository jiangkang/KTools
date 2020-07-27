package com.jiangkang.tools.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.AlarmClock
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

/**
 * 拍照Intent
 * */
val cameraIntent: Intent
    get() {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }
/**
 * 拍视频Intent
 * */
val takeVideoIntent: Intent
    get() {
        return Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    }

/**
 * 获取指定包名的启动Intent
 * @param packageName 包名
 * @param isNewTask 是否以New Task 启动模式启动
 * @return Intent
 */
fun Context.getLaunchAppIntent(packageName: String, isNewTask: Boolean = false): Intent? {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    if (isNewTask) {
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return intent
}

/**
 * 获取卸载指定包名的Intent
 * @param packageName 包名
 * @param isNewTask 是否以New Task 启动模式启动
 * @return Intent
 */
fun getUninstallAppIntent(packageName: String, isNewTask: Boolean = false): Intent? {
    val intent = Intent(Intent.ACTION_DELETE)
    intent.data = Uri.parse("package:$packageName")
    return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
}


/**
 * 获取安装指定APK文件的Intent
 * @param appFile 要安装的APK文件
 * @param isNewTask 是否以New Task 模式启动
 * @return Intent
 */
fun Context.getInstallAppIntent(appFile: File, isNewTask: Boolean = false): Intent {
    val intent = Intent(Intent.ACTION_VIEW)
    val data: Uri
    val type = "application/vnd.android.package-archive"
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        data = Uri.fromFile(appFile)
    } else {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val authority = "$packageName.utilcode.provider"
        data = FileProvider.getUriForFile(this, authority, appFile)
    }
    intent.setDataAndType(data, type)
    return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
}

/**
 * 创建闹铃
 * 需要<uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
 * */
fun createAlarm(context: Context, msg: String, hour: Int, min: Int) {
    val intent = Intent(AlarmClock.ACTION_SET_ALARM)
    intent.putExtra(AlarmClock.EXTRA_MESSAGE, msg)
    intent.putExtra(AlarmClock.EXTRA_HOUR, hour)
    intent.putExtra(AlarmClock.EXTRA_MINUTES, min)
    intent.resolveActivity(context.packageManager)?.let {
        context.startActivity(intent)
    }
}

/**
 * 创建定时器
 * 需要<uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
 * */
fun startTimer(context: Context, msg: String, seconds: Int) {
    val intent = Intent(AlarmClock.ACTION_SET_TIMER)
    intent.putExtra(AlarmClock.EXTRA_MESSAGE, msg)
    intent.putExtra(AlarmClock.EXTRA_LENGTH, seconds)
    intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)//设置定时器时跳过UI
    intent.resolveActivity(context.packageManager)?.let {
        context.startActivity(intent)
    }
}