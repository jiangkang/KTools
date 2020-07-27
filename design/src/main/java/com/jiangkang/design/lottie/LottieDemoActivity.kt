package com.jiangkang.design.lottie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.design.R
import kotlinx.android.synthetic.main.activity_lottie_demo.*

class LottieDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie_demo)

        handleClick()
    }

    private fun handleClick() {

        btn_load_from_xml.setOnClickListener {
            lottie_view.setAnimation(R.raw.bullseye)
        }

        btn_load_from_assets.setOnClickListener {

        }

    }
}
