package com.jiangkang.ktools.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ConstraintLayoutActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, ConstraintLayoutActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, ConstraintLayoutFragment(), this.toString())
                .commit()
    }
}
