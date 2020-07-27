package com.jiangkang.tools.widget

import android.Manifest
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresPermission
import com.jiangkang.tools.R

/**
 * Created by jiangkang on 2017/9/23.
 */
object FloatingWindow {
    private var sWindowManager: WindowManager? = null
    private var sWindowLayoutParams: WindowManager.LayoutParams? = null
    private var sView: View? = null
    fun init(context: Context) {
        sWindowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        sWindowLayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
        )
        sWindowLayoutParams!!.gravity = Gravity.TOP + Gravity.RIGHT
        sView = LayoutInflater.from(context).inflate(R.layout.layout_floating_window, null)
    }

    @RequiresPermission(allOf = [Manifest.permission.SYSTEM_ALERT_WINDOW])
    fun show(context: Context, content: String?) {
        if (sWindowManager == null) {
            init(context)
        }
        val tvContent = sView!!.findViewById<View>(R.id.tv_window_content) as TextView
        tvContent.text = content
        if (!sView!!.isAttachedToWindow) {
            sWindowManager!!.addView(sView, sWindowLayoutParams)
        }
    }

    fun dismiss() {
        if (sWindowManager != null && sView != null && sView!!.isAttachedToWindow) {
            sWindowManager!!.removeView(sView)
        }
    }
}