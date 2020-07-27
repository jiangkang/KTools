package com.jiangkang.widget.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jiangkang.widget.R

/**
 * Created by jiangkang on 2018/1/4.
 * description：约束最大高度，最小高度的垂直线性布局
 */

open class ConstraintLinearLayout : LinearLayout {

    private var mMaxHeight: Int = 0

    private var mMinHeight: Int = 0

    constructor(context: Context?) : super(context) {
        initAttributes(context, null)
    }


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttributes(context, attrs)
    }


    private fun initAttributes(context: Context?, attrs: AttributeSet?) {
        if (attrs != null) {
            val array: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.ConstraintLinearLayout)
            mMaxHeight = array.getDimensionPixelSize(R.styleable.ConstraintLinearLayout_max_height, 0)
            mMinHeight = array.getDimensionPixelSize(R.styleable.ConstraintLinearLayout_min_height, 0)
            array.recycle()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (heightSize < mMinHeight) {
            heightSize = mMinHeight
        }

        if (heightSize > mMaxHeight) {
            heightSize = mMaxHeight
        }

        var constraintHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode)

        super.onMeasure(widthMeasureSpec, constraintHeightMeasureSpec)
    }


}