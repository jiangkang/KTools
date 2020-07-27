package com.jiangkang.widget.view

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by jiangkang on 2017/12/27.
 * description：自定义View - 太极图
 */
class TaiChiView : View {
    private var mPaintWhite: Paint? = null
    private var mPaintBlack: Paint? = null

    //旋转角度
    private var mDegrees = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        mPaintBlack = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintWhite = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintWhite!!.color = Color.WHITE
        mPaintBlack!!.color = Color.BLACK

        //默认背景颜色
        setBackgroundColor(Color.GRAY)
    }

    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height

        //平移画布到View的中间
        canvas.translate(width / 2.toFloat(), height / 2.toFloat())

        //底色
        canvas.drawColor(Color.GRAY)

        //旋转，这里mDegrees与动画相关联
        canvas.rotate(mDegrees.toFloat(), 0f, 0f)

        //两个半圆
        val radius = Math.min(width, height) / 2 - 40
        val rectF = RectF((-radius).toFloat(), (-radius).toFloat(), radius.toFloat(), radius.toFloat())
        canvas.drawArc(rectF, 90f, 180f, true, mPaintBlack!!)
        canvas.drawArc(rectF, -90f, 180f, true, mPaintWhite!!)

        //两个小圆
        val smallRadius = radius / 2
        canvas.drawCircle(0f, -smallRadius.toFloat(), smallRadius.toFloat(), mPaintBlack!!)
        canvas.drawCircle(0f, smallRadius.toFloat(), smallRadius.toFloat(), mPaintWhite!!)

        //两个小点
        val dotRadius = smallRadius / 4
        canvas.drawCircle(0f, -smallRadius.toFloat(), dotRadius.toFloat(), mPaintWhite!!)
        canvas.drawCircle(0f, smallRadius.toFloat(), dotRadius.toFloat(), mPaintBlack!!)
    }

    /*
    * ，添加动画，让太极图动起来
    * */
    fun startRotate() {
        val animator = ValueAnimator.ofInt(0, 360)
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            mDegrees = animation.animatedValue as Int
            invalidate()
        }
        animator.start()
    }
}