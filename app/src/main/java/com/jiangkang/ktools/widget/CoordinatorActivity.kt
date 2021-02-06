package com.jiangkang.ktools.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivityCoordinatorBinding

class CoordinatorActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCoordinatorBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        binding.toolbar.title = "Demo"
        binding.toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.toolbar)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl("https://jiangkang.tech")
        binding.fab.setOnClickListener {
            binding.webView.reload()
        }
    }

    companion object {
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, CoordinatorActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}