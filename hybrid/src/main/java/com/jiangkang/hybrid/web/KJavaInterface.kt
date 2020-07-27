package com.jiangkang.hybrid.web

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.jiangkang.hybrid.R
import com.jiangkang.hybrid.entity.JsRequest
import com.jiangkang.tools.utils.ImageUtils
import com.jiangkang.tools.utils.ToastUtils

/**
 * Created by jiangkang on 2017/9/20.
 */

class KJavaInterface(private val mContext: Context) {

    /*
    {
      id : "",
      class : "",
      method : "",
      data : jsonString
    }
    */
    @JavascriptInterface
    fun callNative(data: String) {
        val jsRequest = Gson().fromJson<JsRequest>(data, JsRequest::class.java)

    }


    val base64Img: String
        @JavascriptInterface
        get() {
            val bitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.demo)
            val content = ImageUtils.bitmap2Base64(bitmap, 80, Bitmap.CompressFormat.JPEG)
            Log.d(TAG, "getBase64Img: \n$content")
            return content
        }


    @JavascriptInterface
    fun showBigImage(url: String) {
        if (!TextUtils.isEmpty(url)) {
            ToastUtils.showShortToast(url)
        }
    }

    companion object {

        private val TAG = "KJavaInterface"
    }


}
