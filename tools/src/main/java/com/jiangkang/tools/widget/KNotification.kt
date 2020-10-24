package com.jiangkang.tools.widget

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.jiangkang.tools.extend.notificationManager
import kotlin.random.Random

/**
 * Created by jiangkang on 2017/9/23.
 * description：快速创建通知栏
 */
object KNotification {

    const val channelIdNormal = "ktools_channel_normal"
    const val shortcutInfoId = "ktools_shortcut_info_id"

    private const val requestCode = 1111

    @JvmStatic
    fun createNotification(context: Context, @DrawableRes smallIconID: Int, title: String, content: String, intent: Intent): Int {
        val pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, channelIdNormal)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIconID)
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .addAction(android.R.drawable.ic_btn_speak_now, "Voice", pendingIntent)
                .build()
        val id = Random(System.currentTimeMillis()).nextInt()
        context.notificationManager.notify(id, notification)
        return id
    }

    fun createNotification(context: Context, @DrawableRes smallIconID: Int, bitmap: Bitmap, intent: Intent): Int {
        val pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, channelIdNormal)
                .setSmallIcon(smallIconID)
                .setAutoCancel(true)
                .setContentTitle("大图Notification")
                .setContentText("一个简单的描述")
                .setLargeIcon(bitmap)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
        val id = Random(System.currentTimeMillis()).nextInt()
        context.notificationManager.notify(id, notification)
        return id
    }


    /**
     * 应用启动时调用
     */
    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(channelIdNormal, channelIdNormal, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "应用中的普通通知"
        }
        context.notificationManager.createNotificationChannel(channel)
    }

    fun openSettingsPage(context: Context) {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, context.notificationManager.getNotificationChannel(channelIdNormal).id)
        }
        context.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    inline fun <reified T : Activity> createBubble(context: Context) {
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, T::class.java), 0)
        val bubbleData = Notification.BubbleMetadata.Builder(shortcutInfoId)
                .setDesiredHeight(600)
                .setIcon(Icon.createWithResource(context, android.R.drawable.btn_star))
                .setIntent(pendingIntent)
                .build()
        val chatBot = Person.Builder()
                .setBot(true)
                .setName("BubbleBot")
                .setImportant(true)
                .build()
        val notification = Notification.Builder(context, channelIdNormal)
                .setBubbleMetadata(bubbleData)
                .addPerson(chatBot)
                .build()
        val id = Random(System.currentTimeMillis()).nextInt()
        context.notificationManager.notify(id, notification)
    }

}