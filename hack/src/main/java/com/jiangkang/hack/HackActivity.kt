package com.jiangkang.hack

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.tools.utils.FileUtils
import com.jiangkang.tools.utils.ReflectUtils
import com.jiangkang.tools.utils.ReflectionUtil
import com.jiangkang.tools.utils.ToastUtils
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_hack.*
import java.io.File
import java.nio.file.Files

class HackActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hack)
        findViewById<Button>(R.id.btn_hook_OnClick).setOnClickListener {
            ToastUtils.showShortToast("Hook 点击事件")
        }
        hookOnClickListener(findViewById<Button>(R.id.btn_hook_OnClick))
        handleClick()
    }

    private fun handleClick() {
        btn_dex_path_list.setOnClickListener {
            val field = ReflectionUtil.findField(classLoader,"pathList")
            val dexPathList = field?.get(classLoader)
        }

        btn_load_dex.setOnClickListener {
            val jarFile = File(
                    filesDir,
                    "hello_world_dex.jar"
            )
            if (!jarFile.exists()) {
                ToastUtils.showShortToast("文件不存在，复制内容到文件中")
                Files.copy(FileUtils.getInputStreamFromAssets("code/hello_world_dex.jar"),jarFile.toPath())
            }
            val loader = DexClassLoader(
                    jarFile.absolutePath,
                    codeCacheDir.absolutePath, null,
                    classLoader
            )
            val clazz = loader.loadClass("com.jiangkang.ktools.HelloWorld")
            val sayHelloMethod = clazz.getDeclaredMethod("sayHello")
            if (!sayHelloMethod.isAccessible){
                sayHelloMethod.isAccessible = true
            }
            val msg = sayHelloMethod.invoke(clazz.newInstance()) as String?
            ToastUtils.showLongToast("执行dex中方法：$msg")
        }

        btn_hook_instrumentation.setOnClickListener {
            HookUtils.hookInstrumentation(this@HackActivity)
            startActivity<HackActivity>()
        }
    }

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    private fun hookOnClickListener(view:View) {
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