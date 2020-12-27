package com.jiangkang.video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.video.surfaceview.SurfaceViewDemoActivity
import kotlinx.android.synthetic.main.activity_k_video.*

open class KVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_k_video)
        handleClick()
    }

    private fun handleClick() {
        btn_surface_view.setOnClickListener {
            startActivity<SurfaceViewDemoActivity>()
        }
    }
}