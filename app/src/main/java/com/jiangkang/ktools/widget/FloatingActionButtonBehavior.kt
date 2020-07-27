package com.jiangkang.ktools.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

/**
 * Created by jiangkang on 2017/10/7.
 */
class FloatingActionButtonBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<ImageView>(context, attrs) {
    override fun layoutDependsOn(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        val translationY = dependency.translationY - dependency.height
        child.translationY = translationY
        return true
    }
}