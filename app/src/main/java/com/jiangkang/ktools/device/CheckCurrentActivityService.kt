package com.jiangkang.ktools.device

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class CheckCurrentActivityService : Service() {
    var manager: NotificationManager? = null
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        registerReceiver(ActivityChangeReceiver(), IntentFilter("com.jiangkang.ktools.ActivityChange"))
    }
}