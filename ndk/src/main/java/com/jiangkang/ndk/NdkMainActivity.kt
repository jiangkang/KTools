package com.jiangkang.ndk

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_ndk_main.*

class NdkMainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ndk_main)
        System.loadLibrary("ktools")

        handleLogic()
    }

    private fun handleLogic() {
        btn_hello_world.setOnClickListener {
            ToastUtils.showShortToast(sayHello())
        }

        
        btn_draw_shape.setOnClickListener {
            val file = createTempFile("line_",".png")
            drawShapeTest(file.absolutePath)
        }
    }

    private external fun drawShapeTest(filename:String)

    private external fun sayHello():String
}