package com.jiangkang.tools.utils

import android.content.Context
import android.telephony.PhoneStateListener
import com.jiangkang.tools.extend.telephonyManager

object PhoneUtils {

    fun getPhoneInfo(context: Context) {
        val telephonyManager = context.telephonyManager
        telephonyManager.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

}