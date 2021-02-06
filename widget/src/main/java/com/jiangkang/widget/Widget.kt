package com.jiangkang.widget

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

/**
 * 拓展View的点击区域
 */
fun View.extendClickArea(topOffset: Int = 0, rightOffset: Int = 0, bottomOffset: Int = 0, leftOffset: Int = 0) {
    post {
        val bounds = Rect()
        getHitRect(bounds)
        bounds.left -= leftOffset
        bounds.top -= topOffset
        bounds.right += rightOffset
        bounds.bottom += bottomOffset
        (parent as View?)?.touchDelegate = TouchDelegate(bounds, this)
    }
}


