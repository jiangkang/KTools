package com.jiangkang.ktools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.tools.extend.startActivity
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
            this@CrashInfoActivity.startActivity<MainActivity>()
            finish()
        }
    }
}