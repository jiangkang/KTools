package com.jiangkang.ktools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.annotations.apt.Router
import com.jiangkang.hybrid.Khybrid
import com.jiangkang.ktools.databinding.ActivityAboutBinding

@Router
class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvGit.setOnClickListener { Khybrid().loadUrl(this@AboutActivity, "https://github.com/jiangkang/KTools") }
    }
}