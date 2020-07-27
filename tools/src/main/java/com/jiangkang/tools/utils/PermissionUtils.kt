package com.jiangkang.tools.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by jiangkang on 2017/9/14.
 */
class PermissionUtils private constructor(private val context: Context) {
    private var callback: PermissionCallback? = null
    fun requestPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            if (callback != null) {
                callback!!.success()
            }
        } else {
            ActivityCompat.requestPermissions((context as Activity), arrayOf(permission), REQUEST_CODE)
        }
    }

    fun setCallback(callback: PermissionCallback?): PermissionUtils {
        this.callback = callback
        return this
    }

    interface PermissionCallback {
        fun success()
        fun fail()

        companion object {
            const val REQUEST_CODE = 1234
        }
    }

    companion object {
        private const val REQUEST_CODE = 1111
        fun getInstance(context: Context): PermissionUtils {
            return PermissionUtils(context)
        }
    }

}