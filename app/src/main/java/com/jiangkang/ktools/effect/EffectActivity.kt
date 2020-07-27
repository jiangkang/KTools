package com.jiangkang.ktools.effect

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.effect.fragment.EffectFragment

class EffectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, EffectFragment(), this.toString())
                .commit()
    }
}