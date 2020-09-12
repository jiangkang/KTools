package com.jiangkang.tools.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.*
import okhttp3.internal.closeQuietly
import java.io.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by jiangkang on 2017/10/16.
 */
object DownloadUtils {

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private val downloadService: ExecutorService = Executors.newCachedThreadPool()

    @JvmStatic
    fun downloadBitmap(url: String, callback: DownloadBitmapCallback?) {
        val request = Request.Builder().url(url).build()
        downloadService.submit {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback?.onFailed(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
                    callback?.onSuccess(bitmap)
                }
            })
        }
    }

    @JvmStatic
    fun downloadFile(url: String,outputFile:File,callback: DownloadCallback?){
        val request = Request.Builder().url(url).build()
        downloadService.submit {
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback?.onFailed(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!outputFile.exists()){
                        outputFile.createNewFile()
                    }
                    FileOutputStream(outputFile).apply {
                        write(response.body?.bytes())
                        flush()
                        closeQuietly()
                    }
                    callback?.onSuccess(outputFile)
                }
            })
        }
    }

    interface DownloadCallback {
        fun onSuccess(downloadedFile: File)
        fun onFailed(msg: String?)
    }

    abstract class DownloadBitmapCallback: DownloadCallback{
        abstract fun onSuccess(bitmap:Bitmap?)
        override fun onSuccess(downloadedFile: File) {
            // do nothing
        }
    }

}
