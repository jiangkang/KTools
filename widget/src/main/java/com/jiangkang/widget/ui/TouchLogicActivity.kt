package com.jiangkang.widget.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.widget.databinding.ActivityTouchLogicBinding

class TouchLogicActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTouchLogicBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivCenter.apply {
            post {
                val gap = 500
                val bounds = Rect()
                getHitRect(bounds)
                bounds.left -= gap
                bounds.top -= gap
                bounds.right += gap
                bounds.bottom += gap
                setOnClickListener { ToastUtils.showShortToast("Click") }
                (binding.ivCenter.parent as View).touchDelegate = TouchDelegate(bounds, binding.ivCenter)
            }
        }

        binding.btnRight.setOnClickListener {
            ToastUtils.showShortToast("Btn Click")
        }

    }

}