package com.jiangkang.ktools.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jiangkang.ktools.R
import com.jiangkang.ktools.databinding.ActivityFabBinding

class FabActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFabBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fab)
        binding.fab.setOnClickListener{
            Snackbar.make(it, "我一出现，FloatingActionButton也要向上移动", Snackbar.LENGTH_SHORT).show()
        }
        binding.ivFakeFab.setOnClickListener {
            Snackbar.make(it, "这是一个假的FloatingActionButton", Snackbar.LENGTH_SHORT).show()
        }
    }


    companion object {
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, FabActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}