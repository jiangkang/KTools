package com.jiangkang.ktools.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGalleryBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()

    }

    private fun initViews() {
        binding.rcGallery.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rcGallery.adapter = GalleryAdapter(this)
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
