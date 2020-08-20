package com.jiangkang.tools.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_INSTALL_PACKAGE
import android.content.pm.PackageInstaller
import android.net.Uri
import androidx.core.content.FileProvider
import com.jiangkang.tools.extend.runOnUiThread
import com.jiangkang.tools.widget.KDialog
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
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

    fun parseApkDemo(context: Context){
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url("https://github.com/jiangkang/flutter-system/releases/download/v0.1.0/app-debug.apk")
                .build()
        val apkFile = File(context.filesDir, "flutter-system.apk")
        if (apkFile.exists()) {
            parseApk(context,apkFile)
        } else {
            ToastUtils.showShortToast("文件不存在，下载文件")
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
                        parseApk(context,file)
                    }
                })
            }
        }
    }

    /**
     * 解析APK基本结构
     */
    fun parseApk(context: Context,apk:File){
        try {
            val apkZip = ZipFile(apk)
            if (apkZip.entries() == null || !apkZip.entries().hasMoreElements()) {
                ToastUtils.showShortToast("解析失败")
                return
            }
            val entries = apkZip.entries()
            val result = StringBuilder()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                result.appendln("${entry.name}:${entry.size}")
            }
            KDialog.showMsgDialog(context,result.toString())
        }catch (e:ZipException){
            ToastUtils.showShortToast("文件格式错误")
        } catch (e:IOException){
            ToastUtils.showShortToast("IO错误")
        }
    }
}