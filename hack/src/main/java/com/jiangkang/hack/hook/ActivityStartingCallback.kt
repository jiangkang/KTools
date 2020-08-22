package com.jiangkang.hack.hook

import android.app.Activity
import android.content.Context
import android.content.Intent

interface ActivityStartingCallback {
    fun activityStarting(source: Context, target: Activity,intent:Intent)
}