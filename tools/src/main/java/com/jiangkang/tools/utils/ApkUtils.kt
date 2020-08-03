package com.jiangkang.tools.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import okhttp3.*
import org.jetbrains.anko.runOnUiThread
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread

object ApkUtils {

    fun installApk(context: Context) {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url("https://github.com/jiangkang/flutter-system/releases/download/v0.1.0/app-debug.apk")
                .build()
        val apkFile = File(context.filesDir, "flutter-system.apk")
        if (apkFile.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.apply {
                flags += Intent.FLAG_ACTIVITY_NEW_TASK
                flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
                type = "application/vnd.android.package-archive"
                data = FileProvider.getUriForFile(context, "com.jiangkang.ktools", apkFile)
            }
            context.startActivity(intent)
        } else {
            thread {
                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        ToastUtils.showShortToast(e.message)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        ToastUtils.showShortToast("请求成功")
                        val file = File(context.filesDir, "flutter-system.apk")
                        val os = FileOutputStream(file)
                        os.write(response.body?.bytes())
                        os.flush()
                        context.runOnUiThread {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.apply {
                                flags += Intent.FLAG_ACTIVITY_NEW_TASK
                                flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
                                type = "application/vnd.android.package-archive"
                                data = FileProvider.getUriForFile(context, "com.jiangkang.ktools", file)
                            }
                            context.startActivity(intent)
                        }

                    }
                })
            }
        }
    }

}