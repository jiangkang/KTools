package com.jiangkang.tools.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import com.jiangkang.tools.widget.FloatingWindow
import org.jetbrains.anko.activityManager
import java.util.*

class WatchingTopActivityService : Service() {

    private var info = ""

    var timer = Timer()

    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer.scheduleAtFixedRate(WatchingTask(this), 0, 500)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext,
                this.javaClass)
        restartServiceIntent.`package` = packageName

        val restartServicePendingIntent = PendingIntent.getService(
                applicationContext, 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT)
        val alarmService = applicationContext
                .getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 500,
                restartServicePendingIntent)
        super.onTaskRemoved(rootIntent)
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    inner class WatchingTask(context: Context) : TimerTask() {

        private val mContext = context

        override fun run() {

            val runningTasks = mContext.activityManager.getRunningTasks(1)

            val topActivity = "${runningTasks[0].topActivity?.packageName}\n${runningTasks[0].topActivity?.className}"

            if (info !== topActivity) {
                info = topActivity
                Handler(Looper.getMainLooper()).post {
                    FloatingWindow.show(mContext, info)
                }

            }

        }

    }

}
