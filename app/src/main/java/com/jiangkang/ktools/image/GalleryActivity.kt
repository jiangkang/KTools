package com.jiangkang.ktools.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiangkang.ktools.R
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        initViews()

    }

    private fun initViews() {
        rcGallery.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rcGallery.adapter = GalleryAdapter(this)
    }

    companion object {

        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, GalleryActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}
