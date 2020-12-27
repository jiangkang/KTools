package com.jiangkang.video.surfaceview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.video.R
import kotlinx.android.synthetic.main.activity_k_video.*

open class SurfaceViewDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_view_demo)
        handleClick()
    }

    private fun handleClick() {

    }

}