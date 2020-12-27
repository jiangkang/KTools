package com.jiangkang.video.surfaceview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

open class SnowSurfaceView : SurfaceView, SurfaceHolder.Callback2, Runnable {

    private lateinit var mPaint: Paint
    private lateinit var mCanvas: Canvas
    private var canDraw = false

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initViews(context)
    }

    private fun initViews(context: Context) {
        holder.addCallback(this)
        focusable = View.FOCUSABLE
        keepScreenOn = true
        isFocusableInTouchMode = true

        mPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        canDraw = true
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        canDraw = false
    }

    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {

    }

    override fun run() {
//        while (canDraw) {
            drawContent()
//        }
    }

    private fun drawContent() {
        mCanvas = holder.lockCanvas()

        val r = 30
        val c = (randomY() + randomX())/ 2

        val path = Path().apply {
            moveTo(r + c, c)
            for (i in 1 until 15) {
                val a = 0.44879895f * i
                val b = r + r * (i %2)
                lineTo(c + b * cos(a),c + b * sin(a))
            }
        }


        mCanvas.drawPath(path, mPaint)

        holder.unlockCanvasAndPost(mCanvas)
    }

    private fun randomX(): Float {
        return width * Random.nextFloat()
    }

    private fun randomY(): Float {
        return height * Random.nextFloat()
    }

    private fun randomRadius(): Float {
        return 10 * Random.nextFloat()
    }

}