package com.jiangkang.widget.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by jiangkang on 2018/2/16.
 * description：View滑动Demo
 */

open class SlideView : View {

    private var currentX: Float = 0f

    private var currentY: Float = 0f

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                currentX = event.x
                currentY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val startX = currentX
                val startY = currentY

                currentX = event.x
                currentY = event.y

                val dx = currentX - startX
                val dy = currentY - startY

                //移动本View
                layout((left + dx).toInt(), (top + dy).toInt(), (right + dx).toInt(), (bottom + dy).toInt())

                //移动父View
//                (parent as View).scrollBy(-dx.toInt(), -dy.toInt())


                //ObjectAnimator,通过这种方法，View的实际位置其实并没有改变，可通过left + translationX来获取准确位置
//                ObjectAnimator.ofFloat(this,"translationX",translationX + dx).setDuration(0).start()
//                ObjectAnimator.ofFloat(this,"translationY",translationY + dy).setDuration(0).start()

            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return true
    }


}