package com.jiangkang.ktools.device

import android.app.Service
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.provider.MediaStore

class ScreenShotMonitorService : Service() {
    private var mHandlerThread: HandlerThread? = null
    private var mHandler: Handler? = null
    private var mInternalObserver: ContentObserver? = null
    private var mExternalObserver: ContentObserver? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        initHandler()
        initObserver()
        addObservers()
    }

    private fun addObservers() {
        contentResolver.registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver!!)
        contentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver!!
        )
    }

    private fun initHandler() {
        mHandlerThread = HandlerThread("ScreenShot")
        mHandlerThread!!.start()
        mHandler = Handler(mHandlerThread!!.looper)
    }

    private fun initObserver() {
        mInternalObserver = MediaContentObserver(this, mHandler, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        mExternalObserver = MediaContentObserver(this, mHandler, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    private fun removeObservers() {
        contentResolver.unregisterContentObserver(mInternalObserver!!)
        contentResolver.unregisterContentObserver(mExternalObserver!!)
    }
}