package com.jiangkang.ktools

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.annotations.apt.Router
import com.jiangkang.hybrid.Khybrid

@Router
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        findViewById<TextView>(R.id.tv_git).setOnClickListener { Khybrid().loadUrl(this@AboutActivity, "https://github.com/jiangkang/KTools") }
    }
}