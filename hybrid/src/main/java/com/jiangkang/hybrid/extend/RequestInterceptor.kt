package com.jiangkang.hybrid.extend

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView

/**
 * Created by jiangkang on 2018/2/5.
 * description：
 */

interface RequestInterceptor {

    fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse?

}