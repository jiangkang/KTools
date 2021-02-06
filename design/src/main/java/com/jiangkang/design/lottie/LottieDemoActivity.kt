package com.jiangkang.design.lottie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.design.R
import com.jiangkang.design.databinding.ActivityLottieDemoBinding

class LottieDemoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLottieDemoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleClick()
    }

    private fun handleClick() {
        binding.btnLoadFromXml.setOnClickListener {
            binding.lottieView.setAnimation(R.raw.bullseye)
        }

        binding.btnLoadFromAssets.setOnClickListener {

        }

    }
}
