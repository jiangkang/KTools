package com.jiangkang.ktools

import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_crash_info.*

class CrashInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_info)

        val crashInfo = intent.getStringExtra("crash_info")
        crashInfo?.let {
            tv_crash_info.text = it
        }

        btn_crash_restart.setOnClickListener {
            MainActivity.launch(this)
            finish()
        }
    }
}