package com.jiangkang.ktools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.annotations.apt.Router
import com.jiangkang.hybrid.Khybrid
import kotlinx.android.synthetic.main.activity_about.*

@Router
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        tv_git.setOnClickListener { Khybrid().loadUrl(this@AboutActivity, "https://github.com/jiangkang/KTools") }
    }
}