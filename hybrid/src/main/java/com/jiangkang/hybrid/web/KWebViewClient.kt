package com.jiangkang.hybrid.web

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import com.jiangkang.tools.utils.FileUtils


/**
 * Created by jiangkang on 2017/9/20.
 */

class KWebViewClient : WebViewClient {

    private val TAG = "KWebViewClient"

    private val mContext: WebContract.IView

    private val mWebArgs: WebArgs

    constructor(context: WebContract.IView, webArgs: WebArgs) : super() {
        mContext = context
        mWebArgs = webArgs
    }

    //TODO: Kotlin 传入的参数类型要注意为null的情况，如果不修改重载函数参数类型，会出现运行时错误
    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (mWebArgs.isLoadImgLazy) {
            view.settings.blockNetworkImage = true
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        if (mWebArgs.isLoadImgLazy) {
            view.settings.blockNetworkImage = false
        }

        if (!TextUtils.isEmpty(mWebArgs.jsInjected)){
            injectJsFile(view,mWebArgs.jsInjected)
        }

    }

    private fun queryHistoryList(view: WebView) {
        val historyList = view.copyBackForwardList()
        if (historyList != null) {
            val size = historyList.size
            val currentIndex = historyList.currentIndex
            (0..(size - 1))
                    .map { "$it-${historyList.getItemAtIndex(it)?.title}-${historyList.getItemAtIndex(it)?.url}" }
                    .forEach {
                        Log.d(TAG, it)
                    }
        }
    }

    private fun injectJsFile(view: WebView, jsString: String) {
        view.loadUrl("javascript:$jsString")
    }

    override fun onScaleChanged(view: WebView, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        //此处对文件资源，js，css等请求资源进行拦截，替换
        Log.d(TAG, "shouldInterceptRequest: request = \n\nurl = ${request.url}\nmethod = ${request.method}\nheaders = ${request.requestHeaders}")
        if (mWebArgs.isInterceptResources) {
            val url = request.url.toString()
            if ((url.startsWith("https://") || url.startsWith("http://")) && (url.endsWith(".png") or url.endsWith(".jpg"))) {
                return WebResourceResponse(MimeTypeMap.getFileExtensionFromUrl(".jpg"), "utf-8", FileUtils.getInputStreamFromAssets("img/dog.jpg"))
            }
        }
        return super.shouldInterceptRequest(view, request)
    }


    fun changeBgColor(webView:WebView){
        val changeBodyBg = """
            (function(){
                document.body.style.backgroundColor = "rgba(0,0,0,125)"
            })();
            """
        injectJsFile(webView,changeBodyBg)
    }


}
