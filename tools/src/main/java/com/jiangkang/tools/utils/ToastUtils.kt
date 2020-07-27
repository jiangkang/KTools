package com.jiangkang.tools.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.jiangkang.tools.King
import java.util.*

/**
 * Created by jiangkang on 2017/9/8.
 */
object ToastUtils {
    fun showShortToast(msg: String?) {
        Handler(Looper.getMainLooper()).post { Toast.makeText(King.applicationContext, msg, Toast.LENGTH_SHORT).show() }
    }

    fun showShortToast(context: Context, msg: String?) {
        Handler(Looper.getMainLooper()).post { Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT).show() }
    }

    fun showLongToast(msg: String?) {
        Handler(Looper.getMainLooper())
                .post { Toast.makeText(King.applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

    fun showToast(msg: String?, duration: Int) {
        val timer = Timer()
        val toast = Toast.makeText(King.applicationContext, msg, Toast.LENGTH_LONG)
        timer.schedule(object : TimerTask() {
            override fun run() {
                toast.show()
            }
        }, 0, 1000)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                toast.cancel()
                timer.cancel()
            }
        }, duration.toLong())
    }
}