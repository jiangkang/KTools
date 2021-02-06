package com.jiangkang.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.video.databinding.ActivityKVideoBinding
import com.jiangkang.video.surfaceview.SurfaceViewDemoActivity

open class KVideoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityKVideoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleClick()
    }

    private fun handleClick() {
        binding.btnSurfaceView.setOnClickListener {
            startActivity<SurfaceViewDemoActivity>()
        }
    }
}