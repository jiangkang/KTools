package com.jiangkang.ktools.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jiangkang.ktools.R
import kotlinx.android.synthetic.main.activity_fab.*

class FabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fab)
        fab.setOnClickListener{
            Snackbar.make(it, "我一出现，FloatingActionButton也要向上移动", Snackbar.LENGTH_SHORT).show()
        }
        iv_fake_fab.setOnClickListener {
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