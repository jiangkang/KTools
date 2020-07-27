package com.jiangkang.hybrid.web

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.*
import com.jiangkang.tools.widget.KDialog


/**
 * Created by jiangkang on 2017/9/20.
 */

class KWebChromeClient : WebChromeClient {

    private val mContext: WebContract.IView

    constructor(context: WebContract.IView) : super() {
        mContext = context
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {

    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
        Log.d(TAG, "onConsoleMessage:\n " + consoleMessage.message())
        return true
    }


    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        KDialog.showJsAlertDialog(view.context, message,result)
        return true
    }


    override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }


    override fun onReceivedIcon(view: WebView, icon: Bitmap) {
        super.onReceivedIcon(view, icon)
    }

    override fun onReceivedTitle(view: WebView, title: String) {
        mContext.tvTitle.text = title
    }


    override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback)
        callback.invoke(origin, true, true)
    }


    override fun getDefaultVideoPoster(): Bitmap? {
        return super.getDefaultVideoPoster()
    }


    override fun getVideoLoadingProgressView(): View? {
        return super.getVideoLoadingProgressView()
    }

    companion object {

        private val TAG = "Web"
    }


}
