package com.jiangkang.tools.extend

import android.content.Context
import android.os.Handler
import android.os.Looper

fun Context.runOnUiThread(f: Context.() -> Unit) {
    if (Looper.getMainLooper() === Looper.myLooper()) f() else Handler(Looper.getMainLooper()).post { f() }
}