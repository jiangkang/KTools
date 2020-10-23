package com.jiangkang.tools.widget

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import com.jiangkang.tools.King.applicationContext

/**
 * Created by jiangkang on 2017/9/23.
 * description：快速创建通知栏
 */
object KNotification {
    private var sManager: NotificationManager? = null

    @JvmStatic
    fun createNotification(context: Context?, @DrawableRes smallIconID: Int, title: String?, content: String?, intent: Intent?) {
        val pendingIntent = PendingIntent.getActivity(context, 1111, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = Notification.Builder(context,NotificationChannel.DEFAULT_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIconID)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        sManager?.notify(0, notification)
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun createNotification(context: Context?, @DrawableRes smallIconID: Int, bigView: RemoteViews?, intent: Intent?) {
        val pendingIntent = PendingIntent.getActivity(context, 1111, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification =  Notification.Builder(context,NotificationChannel.DEFAULT_CHANNEL_ID)
                .setSmallIcon(smallIconID)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCustomBigContentView(bigView)
                .build()
        sManager?.notify(0, notification)
    }

    init {
        sManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}