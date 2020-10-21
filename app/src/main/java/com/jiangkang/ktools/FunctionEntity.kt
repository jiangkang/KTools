package com.jiangkang.ktools

import android.app.Activity
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

/**
 * Created by jiangkang on 2017/9/5.
 * description：
 */
class FunctionEntity : Serializable {
    var name: String? = null
    var activity: Class<out Activity?>? = null
    @IdRes
    var resId = 0

    constructor(name: String?, activity: Class<out Activity?>?, resId: Int) {
        this.name = name
        this.activity = activity
        this.resId = resId
    }
}