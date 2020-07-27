package com.jiangkang.ktools.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.RemoteException
import com.jiangkang.ktools.IComputation

/**
 * Created by jiangkang on 2018/1/18.
 * description：Android 服务端 IComputation 的Service
 */
class ComputationService : Service() {
    private val mBinder: Binder = object : IComputation.Stub() {
        @Throws(RemoteException::class)
        override fun add(a: Float, b: Float): Float {
            return a + b
        }

        @Throws(RemoteException::class)
        override fun sub(a: Float, b: Float): Float {
            return a - b
        }

        @Throws(RemoteException::class)
        override fun mul(a: Float, b: Float): Float {
            return a * b
        }

        @Throws(RemoteException::class)
        override fun del(a: Float, b: Float): Float {
            if (b == 0f) throw RemoteException("不能除以0")
            return a / b
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

}