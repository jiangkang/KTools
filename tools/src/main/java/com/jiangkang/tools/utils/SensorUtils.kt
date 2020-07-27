package com.jiangkang.tools.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.jiangkang.tools.King.applicationContext

/**
 * Created by jiangkang on 2017/12/4.
 * description：
 */
object SensorUtils {
    /*
    * 判断设备是否支持计步器
    * */
    val isSupportStepSensor: Boolean
        get() {
            val sensorManager = applicationContext!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            val detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            return counterSensor != null || detectorSensor != null
        }
}