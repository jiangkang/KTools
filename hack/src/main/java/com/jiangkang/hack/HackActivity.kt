package com.jiangkang.hack

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.jiangkang.hack.activity.ReplacementActivity
import com.jiangkang.hack.databinding.ActivityHackBinding
import com.jiangkang.hack.hook.ActivityStartingCallback
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.tools.utils.DownloadUtils
import com.jiangkang.tools.utils.FileUtils
import com.jiangkang.tools.utils.ReflectionUtil
import com.jiangkang.tools.utils.ToastUtils
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import java.io.File
import java.nio.file.Files

class HackActivity : ComponentActivity() {

    private val binding by lazy { ActivityHackBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnHookOnClick.setOnClickListener {
            ToastUtils.showShortToast("Hook 点击事件")
        }
        hookOnClickListener(binding.btnHookOnClick)
        handleClick()
    }

    private fun handleClick() {
        binding.btnDexPathList.setOnClickListener {
            val field = ReflectionUtil.findField(classLoader, "pathList")
            val dexPathList = field?.get(classLoader)
        }

        binding.btnLoadDex.setOnClickListener {
            val jarFile = File(
                    filesDir,
                    "hello_world_dex.jar"
            )
            if (!jarFile.exists()) {
                ToastUtils.showShortToast("文件不存在，复制内容到文件中")
                Files.copy(FileUtils.getInputStreamFromAssets("code/hello_world_dex.jar"), jarFile.toPath())
            }
            val loader = DexClassLoader(
                    jarFile.absolutePath,
                    codeCacheDir.absolutePath, null,
                    classLoader
            )
            val clazz = loader.loadClass("com.jiangkang.ktools.HelloWorld")
            val sayHelloMethod = clazz.getDeclaredMethod("sayHello")
            if (!sayHelloMethod.isAccessible) {
                sayHelloMethod.isAccessible = true
            }
            val msg = sayHelloMethod.invoke(clazz.newInstance()) as String?
            ToastUtils.showLongToast("执行dex中方法：$msg")
        }

        binding.btnHookInstrumentation.setOnClickListener {
            HookUtils.hookInstrumentationWithActivity(this@HackActivity, object : ActivityStartingCallback {
                override fun activityStarting(source: Context, target: Activity, intent: Intent) {
                    intent.setClass(source, ReplacementActivity::class.java)
                }
            })
            startActivity<HackActivity>()
        }

        binding.btnLoadApk.setOnClickListener {
            val url = "https://github.com/jiangkang/flutter-system/releases/download/v0.1.0/app-debug.apk"
            val apkFile = File(filesDir, "flutter_system.apk")
            if (apkFile.exists()) {
                loadApkDynamic(apkFile)
            } else {
                DownloadUtils.downloadFile(url, File(filesDir, "flutter_system.apk"), object : DownloadUtils.DownloadCallback {
                    override fun onSuccess(downloadedFile: File) {
                        loadApkDynamic(downloadedFile)
                    }

                    override fun onFailed(msg: String?) {
                        ToastUtils.showShortToast(msg)
                    }
                })
            }
        }
    }

    // TODO: 2020/8/22 还未完成 
    private fun loadApkDynamic(apkFile: File) {
        val dexClassLoader = DexClassLoader(apkFile.absolutePath,
                codeCacheDir.absolutePath,
                null,
                classLoader)

        val apkDexPathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
                .apply {
                    isAccessible = true
                }
        val apkPathList = apkDexPathListField.get(dexClassLoader)
        val dexElementsField = apkPathList::class.java.getDeclaredField("dexElements")
                .apply {
                    isAccessible = true
                }
        val apkDexElements = dexElementsField.get(apkPathList) as Array<*>

        val hostDexPathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
                .apply {
                    isAccessible = true
                }
        val hostPathList = hostDexPathListField.get(classLoader)
        val hostDexElementsField = hostPathList::class.java.getDeclaredField("dexElements")
                .apply {
                    isAccessible = true
                }
        val hostDexElements = hostDexElementsField.get(hostPathList) as Array<*>

        // 创建一个新的数组，依次写入host,plugin 的 elements
        val combined = java.lang.reflect.Array.newInstance(
                hostDexElements.javaClass.componentType, hostDexElements.size + apkDexElements.size) as Array<*>
        System.arraycopy(hostDexElements, 0, combined, 0, hostDexElements.size)
        System.arraycopy(apkDexElements, 0, combined, hostDexElements.size, apkDexElements.size)
        
        hostDexElementsField.set(hostPathList, combined)
        ToastUtils.showShortToast("加载APK成功")
    }

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    private fun hookOnClickListener(view: View) {
        try {
            //getListenerInfo()
            val getListenerInfoMethod = View::class.java.getDeclaredMethod("getListenerInfo")
            getListenerInfoMethod.isAccessible = true

            // 调用getListenerInfo()方法，得到ListenerInfo对象
            val listenerInfo = getListenerInfoMethod.invoke(view)

            //得到View的mOnClickListener Field
            val listenerInfoClz = Class.forName("android.view.View\$ListenerInfo")
            val mOnClickListener = listenerInfoClz.getDeclaredField("mOnClickListener")
            mOnClickListener.isAccessible = true

            // 获取到原来的listener值
            val originOnClickListener = mOnClickListener[listenerInfo] as View.OnClickListener

            // 新的listener
            val hookedOnClickListener: View.OnClickListener = HookOnClickListener(originOnClickListener, this)
            // 赋值新的listener
            mOnClickListener[listenerInfo] = hookedOnClickListener
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}