package com.jiangkang.widget.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View

/**
 * Created by jiangkang on 2018/1/11.
 * description：利用Path绘制多边形
 */
class ShapePathView : View {

    private var centerX = 0

    private var centerY = 0

    private var mSides: Int = 3

    private var mRadius = 0.0f

    private var mProgress = 0.0f

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : super(context) {
        initVar()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initVar()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initVar()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initVar()
    }


    private fun initVar() {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 6.0f
    }


    override fun onDraw(canvas: Canvas?) {

        centerX = width / 2
        centerY = height / 2

        mRadius = Math.min(centerX, centerY).toFloat()

        val path = createShapePath(mSides, mRadius)

        val length = PathMeasure(path,false).length

        mPaint.pathEffect = DashPathEffect(floatArrayOf(length,length),mProgress)

        canvas?.drawPath(path, mPaint)

    }

    private fun createShapePath(sides: Int, radius: Float): Path {
        return Path().apply {
            val angle = Math.PI * 2 / sides
            moveTo(centerX + (radius * Math.cos(0.0)).toFloat(),
                    centerY + (radius * Math.sin(0.0).toFloat()))
            for (i in 1 until sides) {
                lineTo(centerX + (radius * Math.cos(i * angle)).toFloat(),
                        centerY + (radius * Math.sin(i * angle).toFloat()))
            }
            close()
        }
    }


    fun updateSides(sides: Int) {
        mSides = sides
        invalidate()
    }


    fun updateRadius(radius: Float) {
        mRadius = radius
        invalidate()
    }

    fun updateProgress(progress:Float){
        mProgress = progress / 100 * PathMeasure(createShapePath(mSides,mRadius),false).length
        invalidate()
    }


}