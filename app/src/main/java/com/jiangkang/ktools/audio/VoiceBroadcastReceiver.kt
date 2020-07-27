package com.jiangkang.ktools.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 模拟接到推送通知
 * @author jiangkang
 * @date 2017/10/18
 */
class VoiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val list = VoiceTemplate()
                .prefix("success")
                .numString("15.00")
                .suffix("yuan")
                .gen()
        VoiceSpeaker.speak(list)
    }
}