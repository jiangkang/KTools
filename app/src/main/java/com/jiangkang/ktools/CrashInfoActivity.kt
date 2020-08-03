package com.jiangkang.ktools

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CrashInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_info)

        val crashInfo = intent.getStringExtra("crash_info")
        crashInfo?.let {
            findViewById<TextView>(R.id.tv_crash_info).text = it
        }

        findViewById<Button>(R.id.btn_crash_restart).setOnClickListener {
            MainActivity.launch(this)
            finish()
        }
    }
}