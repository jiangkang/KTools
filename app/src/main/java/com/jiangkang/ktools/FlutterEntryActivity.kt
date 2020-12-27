package com.jiangkang.ktools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.ktools.databinding.ActivityFlutterEntryBinding
import io.flutter.embedding.android.FlutterActivity

class FlutterEntryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFlutterEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlutterEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnFlutter.setOnClickListener {
            startActivity(FlutterActivity.createDefaultIntent(this@FlutterEntryActivity))
        }
    }
}