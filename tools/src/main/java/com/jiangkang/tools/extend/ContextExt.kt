package com.jiangkang.tools.extend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

fun Context.runOnUiThread(f: Context.() -> Unit) {
    if (Looper.getMainLooper() === Looper.myLooper()) f() else Handler(Looper.getMainLooper()).post { f() }
}

inline fun <reified T: Activity> Context.startActivity(){
    startActivity(Intent(this,T::class.java))
}