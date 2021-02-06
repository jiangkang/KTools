package com.jiangkang.ndk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ndk.databinding.ActivityNdkMainBinding
import com.jiangkang.ndk.skia.SkiaActivity
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.tools.utils.ToastUtils

class NdkMainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityNdkMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        System.loadLibrary("ktools")
        handleLogic()
    }

    private fun handleLogic() {
        binding.btnHelloWorld.setOnClickListener {
            ToastUtils.showShortToast(sayHello())
        }

        binding.btnSkia.setOnClickListener {
            startActivity<SkiaActivity>()
        }
    }

    private external fun sayHello():String
}