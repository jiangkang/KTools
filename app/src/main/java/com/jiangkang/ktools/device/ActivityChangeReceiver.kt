package com.jiangkang.ktools.device

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jiangkang.ktools.R
import com.jiangkang.tools.utils.currentActivity

class ActivityChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if ("com.jiangkang.ktools.ActivityChange" == intent.action) {
            createNotification(context, currentActivity)
        }
    }

    private fun createNotification(context: Context, info: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("当前Activity")
                .setContentText(info)
                .build()
        manager.notify(0, notification)
    }
}