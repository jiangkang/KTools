package com.jiangkang.ktools.device.step

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Binder
import android.os.IBinder
import com.jiangkang.ktools.device.step.StepRecordingService

class StepRecordingService : Service(), SensorEventListener {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {}
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    inner class StepBinder : Binder() {
        val service: StepRecordingService
            get() = this@StepRecordingService
    }
}