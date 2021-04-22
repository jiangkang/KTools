package com.jiangkang.ktools.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jiangkang.tools.utils.ToastUtils

class LogLifecycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun testOnCreate() {
//        ToastUtils.showShortToast("onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun testOnStart() {
//        ToastUtils.showShortToast("onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun testOnResume() {
//        ToastUtils.showShortToast("onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun testOnPause() {
//        ToastUtils.showShortToast("onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun testOnStop() {
//        ToastUtils.showShortToast("onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun testOnDestory() {
//        ToastUtils.showShortToast("onDestroy")
    }

}