package com.jiangkang.ndk

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ndk.skia.SkiaActivity
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_ndk_main.*

class NdkMainActivity : AppCompatActivity() {
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

        btn_skia.setOnClickListener {
            startActivity<SkiaActivity>()
        }
    }

    private external fun sayHello():String
}