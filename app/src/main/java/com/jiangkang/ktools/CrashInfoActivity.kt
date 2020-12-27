package com.jiangkang.ktools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivityCrashInfoBinding
import com.jiangkang.tools.extend.startActivity

class CrashInfoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCrashInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val crashInfo = intent.getStringExtra("crash_info")
        crashInfo?.let {
            binding.tvCrashInfo.text = it
        }

        binding.btnCrashRestart.setOnClickListener {
            this@CrashInfoActivity.startActivity<MainActivity>()
            finish()
        }
    }
}