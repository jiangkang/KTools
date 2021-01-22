package com.jiangkang.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

class TouchLinearLayout : LinearLayout {


    val TAG = "TouchLogic-LinearLayout"

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"dispatchTouchEvent: ${ev?.action}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG,"onTouchEvent: ${event?.action}")
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"onInterceptTouchEvent: ${ev?.action}")
        return false
    }

}