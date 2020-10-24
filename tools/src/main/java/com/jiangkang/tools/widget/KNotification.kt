package com.jiangkang.tools.widget

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.jiangkang.tools.extend.notificationManager

/**
 * Created by jiangkang on 2017/9/23.
 * description：快速创建通知栏
 */
object KNotification {

    private const val channelIdNormal = "ktools_channel_normal"

    @JvmStatic
    fun createNotification(context: Context, @DrawableRes smallIconID: Int, title: String?, content: String?, intent: Intent?) {
        val pendingIntent = PendingIntent.getActivity(context, 1111, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, channelIdNormal)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIconID)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
        context.notificationManager.notify(0, notification)
    }

    fun createNotification(context: Context, @DrawableRes smallIconID: Int, bigView: RemoteViews?, intent: Intent?) {
        val pendingIntent = PendingIntent.getActivity(context, 1111, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification =  NotificationCompat.Builder(context, channelIdNormal)
                .setSmallIcon(smallIconID)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCustomBigContentView(bigView)
                .build()
        context.notificationManager.notify(0, notification)
    }


    fun createNotificationChannel(context: Context){
        val channel = NotificationChannel(channelIdNormal, channelIdNormal,NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "应用中的普通通知"
        }
        context.notificationManager.createNotificationChannel(channel)
    }

}