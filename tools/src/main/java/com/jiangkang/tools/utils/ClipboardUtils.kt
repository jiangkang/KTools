package com.jiangkang.tools.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.jiangkang.tools.King

/**
 * Created by jiangkang on 2017/9/12.
 * 剪贴板相关
 */
object ClipboardUtils {
    private val TAG = ClipboardUtils::class.java.simpleName
    private var sClipboardManager: ClipboardManager? = null
    fun putStringToClipboard(content: String) {
        val data = ClipData.newPlainText(null, content)
        sClipboardManager!!.setPrimaryClip(data)
    }

    val stringFromClipboard: String
        get() {
            val clipData = sClipboardManager!!.primaryClip
            if (clipData != null) {
                Log.d(TAG, "getStringFromClipboard: \nclipData = $clipData")
                if (clipData.itemCount > 0) {
                    return clipData.getItemAt(0).text.toString()
                }
            }
            return ""
        }

    init {
        sClipboardManager = King.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
}