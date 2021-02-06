package com.jiangkang.video.surfaceview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.video.R

open class SurfaceViewDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_view_demo)
        handleClick()
    }

    private fun handleClick() {

    }

}