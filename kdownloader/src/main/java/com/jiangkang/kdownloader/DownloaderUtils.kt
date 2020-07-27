package com.jiangkang.kdownloader

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by jiangkang on 2018/1/9.
 * description：
 */
object DownloaderUtils {

    fun checkIsSupportPartialContent(url: String): Boolean {
        val response = getResponseFromUrl(url)
        if (response!!.isSuccessful
                && response.header("Accept-Ranges")!!.isNotEmpty()) {
            return true
        }
        return false
    }


    private fun getResponseFromUrl(url: String): Response? {
        var request = Request.Builder()
                .url(url)
                .build()
        return OkHttpClient().newCall(request).execute()
    }

    fun download(url: String, filePath: String): File? {
        val response = getResponseFromUrl(url)
        if (response!!.isSuccessful) {
            val file = File(filePath)
            val buffer = BufferedOutputStream(FileOutputStream(file))
            buffer.write(response.body?.bytes())
            buffer.flush()
            return file
        }
        return null
    }



}