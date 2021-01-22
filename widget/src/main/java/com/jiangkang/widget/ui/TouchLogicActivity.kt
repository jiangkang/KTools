package com.jiangkang.widget.ui

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.widget.R

class TouchLogicActivity : AppCompatActivity() {

    private val tag = "TouchLogicActivity"
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e2.y - e1.y > 0){
                    ToastUtils.showShortToast("下滑")
                } else {
                    ToastUtils.showShortToast("上滑")
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }

        })
        setContentView(R.layout.activity_touch_logic)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        when (event.action) {

        }
        return super.onTouchEvent(event)
    }

}