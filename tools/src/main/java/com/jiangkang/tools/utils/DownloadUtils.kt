package com.jiangkang.tools.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by jiangkang on 2017/10/16.
 */
class DownloadUtils private constructor() {

    private val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
    private val downloadService: ExecutorService = Executors.newCachedThreadPool()

    fun downloadImage(url: String): Bitmap? {
        val result = arrayOfNulls<Bitmap>(1)
        return if (url.startsWith("https://") || url.startsWith("http://")) {
            val request = Request.Builder()
                    .url(url)
                    .build()
            val latch = CountDownLatch(1)
            downloadService.execute {
                try {
                    val response = client.newCall(request).execute()
                    result[0] = BitmapFactory.decodeStream(response.body!!.byteStream())
                    latch.countDown()
                } catch (e: IOException) {
                    latch.countDown()
                }
            }
            try {
                latch.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            result[0]
        } else {
            null
        }
    }

    companion object {
        private var sInstance: DownloadUtils? = null
        val instance: DownloadUtils?
            get() {
                if (sInstance == null) {
                    sInstance = DownloadUtils()
                }
                return sInstance
            }
    }

}