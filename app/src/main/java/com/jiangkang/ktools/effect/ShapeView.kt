package com.jiangkang.ktools.effect

import android.annotation.TargetApi
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.jiangkang.ktools.R
import java.util.*

/**
 * Created by jiangkang on 2017/10/14.
 */
class ShapeView : View {
    private var mPaint: Paint? = null
    private var mDividerPaint: Paint? = null

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
        if (mPaint == null) {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        }
        if (mDividerPaint == null) {
            mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mDividerPaint!!.color = Color.parseColor("#00796B")
        }
        setBackgroundColor(Color.WHITE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //mask
        canvas.drawColor(Color.parseColor("#12FF9800"))
        val height = measuredHeight

        //line
        var y = 200
        while (y < height) {
            canvas.drawLine(0f, y.toFloat(), measuredWidth.toFloat(), y.toFloat(), mDividerPaint!!)
            y = y + 200
        }
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = 2f
        mPaint!!.color = Color.parseColor("#f44336")

        //circle:50
        canvas.drawCircle(100f, 100f, 50f, mPaint!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(200f, 50f, 300f, 150f, 30f, 60f, true, mPaint!!)
            canvas.drawArc(350f, 50f, 450f, 150f, 30f, 60f, false, mPaint!!)
        }

        //rect:250
        canvas.drawRect(50f, 250f, 150f, 300f, mPaint!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(200f, 250f, 300f, 300f, 10f, 10f, mPaint!!)
        }

        //text:450
        mPaint!!.textSize = 64f
        mPaint!!.textLocale = Locale.CHINA
        canvas.drawText("Android", 50f, 500f, mPaint!!)

        //bitmap:650
        canvas.drawBitmap(BitmapFactory.decodeResource(this.context.resources, R.drawable.ic_device), 50f, 650f, mPaint)
    }
}