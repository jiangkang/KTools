package com.jiangkang.ktools.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import com.jiangkang.tools.extend.audioManager
import com.jiangkang.tools.utils.ToastUtils

class HeadSetBroadcastReceiver : BroadcastReceiver() {

    private val EXTRA_KEY_STATE = "state"

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_HEADSET_PLUG -> {
                val hasState = intent.hasExtra(EXTRA_KEY_STATE)
                if (!hasState) {
                    return
                }
                val state = intent.getIntExtra(EXTRA_KEY_STATE, -1)
                if (state == 1 && hasWiredHeadSetOn(context)) {
                    ToastUtils.showShortToast("检测到有线耳机插入")
                } else if (state == 0 && !hasWiredHeadSetOn(context)) {
                    ToastUtils.showShortToast("检测到有线耳机拔出")
                }
            }
        }
    }

    private fun hasWiredHeadSetOn(context: Context): Boolean {
        val outputs = context.audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        return outputs.contains(AudioDeviceInfo.TYPE_WIRED_HEADSET)
    }
    
}