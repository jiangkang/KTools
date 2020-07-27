package com.jiangkang.kanimation

import android.animation.ObjectAnimator
import android.view.View

object KAnimation {


}

fun View.rotate(from: Float = 0f, to: Float, duration: Long = 200) {
    ObjectAnimator
            .ofFloat(this, "rotation", from, to)
            .setDuration(duration)
            .start()
}