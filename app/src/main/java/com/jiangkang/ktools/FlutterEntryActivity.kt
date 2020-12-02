package com.jiangkang.ktools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import kotlinx.android.synthetic.main.activity_flutter_entry.*

class FlutterEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutter_entry)
        btn_flutter.setOnClickListener {
            startActivity(FlutterActivity.createDefaultIntent(this@FlutterEntryActivity))
        }
    }
}