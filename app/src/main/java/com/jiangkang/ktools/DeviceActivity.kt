package com.jiangkang.ktools

import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivityDeviceBinding
import com.jiangkang.tools.extend.activityManager
import com.jiangkang.tools.service.WatchingTopActivityService
import com.jiangkang.tools.utils.*
import com.jiangkang.tools.widget.KDialog
import kotlin.concurrent.thread

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Device"
        handleClick()
    }


    private fun handleClick() {


        binding.btnCheckNetworkInfo.setOnClickListener {
            val builder = StringBuilder()
            builder.appendln("网络类型: ${NetworkUtils.netWorkType}")
                    .appendln("Mac地址:${NetworkUtils.macAddress}")
            KDialog.showMsgDialog(this@DeviceActivity, builder.toString())
        }


        binding.btnGetDeviceInfo.setOnClickListener {
            val builder = StringBuilder().apply {
                appendln("java.vm.version:${DeviceUtils.vmVersion}")
            }
            KDialog.showMsgDialog(this@DeviceActivity, builder.toString())
        }


        binding.btnScreenBrightness.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this@DeviceActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:$packageName")))
                    ToastUtils.showShortToast("授权用来修改系统屏幕亮度")
                } else {
                    val currentScreenBrightness = ScreenUtils.getScreenBrightness(this@DeviceActivity)
                    ScreenUtils.setScreenBrightness(this@DeviceActivity, 255)
                    KDialog.showMsgDialog(this@DeviceActivity, "原来的屏幕亮度为：$currentScreenBrightness \n现在屏幕亮度已经设置到最大")
                }
            }

        }

        binding.btnCurrentWindowBrightness.setOnClickListener {
            ScreenUtils.setCurrentWindowScreenBrightness(this@DeviceActivity, 125)
            ToastUtils.showShortToast("将亮度修改到了125,只对当前页面有效")
        }


        binding.btnCheckCurrentActivity.setOnClickListener {
            startService(Intent(this@DeviceActivity, WatchingTopActivityService::class.java))
        }

        binding.btnAdbWireless.setOnClickListener {
            thread {
                val command = "setprop service.adb.tcp.port 5555 && stop adbd && start adbd"
                ShellUtils.execCmd(command, true)
                runOnUiThread {
                    val ip = DeviceUtils.getIPAddress(this@DeviceActivity)
                    KDialog.showMsgDialog(this@DeviceActivity, "adb connect " + ip!!)
                }
            }
        }

        binding.btnGetMaxMemory.setOnClickListener {
            val maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024)
            val memoryInfo = getAvailableMemory()

            val lowMemory = memoryInfo.lowMemory
            val availableMemory = memoryInfo.availMem / (1024 * 1024)
            val totalMem = memoryInfo.totalMem / (1024 * 1024)
            val threshold = memoryInfo.threshold / (1024 * 1024)

            val msg = "分配给App的最大堆内存为：$maxMemory M \n" +
                    "是否处于低内存状态：$lowMemory \n" +
                    "系统可用内存：$availableMemory M\n" +
                    "系统总运行内存： $totalMem M\n" +
                    "低内存阈值：$threshold M"
            KDialog.showMsgDialog(this@DeviceActivity, msg)
        }

    }

    private fun getAvailableMemory(): ActivityManager.MemoryInfo {
        var memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        private val TAG = DeviceActivity::class.java.simpleName
    }


}
