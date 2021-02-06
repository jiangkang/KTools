package com.jiangkang.hybrid.web

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.text.TextUtils
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil.isHttpUrl
import android.webkit.URLUtil.isHttpsUrl
import android.webkit.WebSettings
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.hybrid.R
import com.jiangkang.hybrid.databinding.ActivityWebBinding
import com.jiangkang.tools.utils.DownloadUtils
import com.jiangkang.tools.utils.LogUtils
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KDialog

class WebActivity : AppCompatActivity(), WebContract.IView {

    override val tvTitle: TextView
        get() {
            return binding.tvTitleMiddle
        }
    override val ivBack: ImageView
        get() {
            return binding.ivTitleLeft
        }

    //网址
    private var launchUrl: String = "https://www.jiangkang.tech"

    private var mContext = this

    private val binding by lazy { ActivityWebBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        registerForContextMenu(binding.webContainer)
        initVar()
        handleClick()
    }


    private val CONTEXT_MENU_ID_DOWNLOAD_IMAGE = 0

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        binding.webContainer.hitTestResult?.let {
            when (it.type) {
                WebView.HitTestResult.IMAGE_TYPE,
                WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE -> {
                    menu.setHeaderTitle("菜单")
                    menu.add(0, CONTEXT_MENU_ID_DOWNLOAD_IMAGE, 0, "下载图片")
                }
                else -> {
                    LogUtils.d(TAG, "long pressed type : ${it.type}")
                }
            }
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        binding.webContainer.hitTestResult?.let {
            val url = it.extra
            if (CONTEXT_MENU_ID_DOWNLOAD_IMAGE == item.itemId) {
                if (url != null && (isHttpUrl(url) or isHttpsUrl(url))) {
                    DownloadUtils.downloadBitmap(url, object : DownloadUtils.DownloadBitmapCallback() {
                        override fun onSuccess(bitmap: Bitmap?) {
                            runOnUiThread {
                                ToastUtils.showShortToast("图片下载成功")
                                KDialog.showImgInDialog(this@WebActivity, bitmap)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            ToastUtils.showShortToast(msg)
                        }
                    })
                } else {
                    ToastUtils.showShortToast("图片链接不是http或者https，无法下载")
                }
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun handleClick() {

        binding.ivTitleLeft.setOnClickListener {
            if (binding.webContainer.canGoBack()) {
                binding.webContainer.goBack()
            } else {
                finish()
            }
        }

        binding.ivTitleRight.setOnClickListener {
            printPage()
        }

    }

    private fun printPage() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.webContainer.createPrintDocumentAdapter("KTools_Doc_" + System.currentTimeMillis().toString())
        } else {
            binding.webContainer.createPrintDocumentAdapter()
        }

        val printJob = printManager.print("Print_" + System.currentTimeMillis().toString(),
                printAdapter,
                PrintAttributes.Builder().build())
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initVar() {
        if (TextUtils.isEmpty(intent.getStringExtra("launchUrl"))){
            return
        }
        launchUrl = intent.getStringExtra("launchUrl")!!

        var webArgs = initWebArgs()

        Log.d(TAG, "initVar: launchUrl = " + launchUrl!!)

        binding.webContainer.apply {
            webChromeClient = KWebChromeClient(mContext)
            webViewClient = KWebViewClient(mContext, webArgs)
            addJavascriptInterface(KJavaInterface(mContext), "jk")
        }

        binding.webContainer.settings.apply {
            mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
            javaScriptEnabled = false
            allowFileAccessFromFileURLs = true
            setGeolocationEnabled(true)
            allowFileAccess = true
            allowFileAccessFromFileURLs = true
        }


        WebView.setWebContentsDebuggingEnabled(true)
        binding.webContainer.loadUrl(launchUrl, mapOf("x-requested-with" to "good luck to you"))
    }

    private fun initWebArgs(): WebArgs {
        var webArgs = WebArgs()
        webArgs.isLoadImgLazy = intent.getBooleanExtra(WebArgs.IS_LOAD_IMG_LAZY, false)
        webArgs.isInterceptResources = intent.getBooleanExtra(WebArgs.IS_INTERCEPT_RESOURCES, false)
        webArgs.jsInjected = intent.getStringExtra(WebArgs.STR_INJECTED_JS)!!
        return webArgs
    }


    override fun onBackPressed() {
        if (binding.webContainer.canGoBack()) {
            binding.webContainer.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            binding.webContainer.goBack()
        } else {
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {

        private val TAG = "WebActivity"

        /**
         *
         * @param bundle
         * launchUrl : 网址
         * imgClickable : 点击图片是否显示大图
         *
         * @return
         */
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, WebActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}
