package com.jiangkang.ndk.skia

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ndk.R
import com.jiangkang.ndk.databinding.ActivitySkiaBinding
import com.jiangkang.tools.widget.KDialog

class SkiaActivity : AppCompatActivity() {

    private val binding:ActivitySkiaBinding by lazy { ActivitySkiaBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skia)
        handleLogic()
    }

    private fun handleLogic() {
        binding.btnDrawShape.setOnClickListener {
            val file = createTempFile("shape_", ".png")
            SkiaJni.drawShape(file.absolutePath)
            KDialog.showImgInDialog(this@SkiaActivity, BitmapFactory.decodeFile(file.absolutePath))
        }

        binding.btnDrawText.setOnClickListener {
            val file = createTempFile("text_", ".png")
            SkiaJni.drawText(file.absolutePath)
            KDialog.showImgInDialog(this@SkiaActivity, BitmapFactory.decodeFile(file.absolutePath))
        }
    }
}