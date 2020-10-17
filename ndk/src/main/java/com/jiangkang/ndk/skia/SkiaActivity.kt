package com.jiangkang.ndk.skia

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ndk.R
import com.jiangkang.tools.widget.KDialog
import kotlinx.android.synthetic.main.activity_skia.*

class SkiaActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skia)
        handleLogic()
    }

    private fun handleLogic() {
        btn_draw_shape.setOnClickListener {
            val file = createTempFile("shape_",".png")
            SkiaJni.drawShape(file.absolutePath)
            KDialog.showImgInDialog(this@SkiaActivity, BitmapFactory.decodeFile(file.absolutePath))
        }

        btn_draw_text.setOnClickListener {
            val file = createTempFile("text_",".png")
            SkiaJni.drawText(file.absolutePath)
            KDialog.showImgInDialog(this@SkiaActivity, BitmapFactory.decodeFile(file.absolutePath))
        }
    }
}