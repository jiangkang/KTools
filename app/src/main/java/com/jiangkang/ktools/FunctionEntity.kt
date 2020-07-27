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
        private set
    var activity: Class<out Activity?>? = null
        private set

    @IdRes
    var resId = 0
        private set

    constructor(name: String?, activity: Class<out Activity?>?, resId: Int) {
        this.name = name
        this.activity = activity
        this.resId = resId
    }

    fun setName(name: String?): FunctionEntity {
        this.name = name
        return this
    }

    fun setActivity(activity: Class<out AppCompatActivity?>?): FunctionEntity {
        this.activity = activity
        return this
    }

    fun setResId(resId: Int): FunctionEntity {
        this.resId = resId
        return this
    }
}