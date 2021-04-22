package com.jiangkang.ktools

import android.bluetooth.BluetoothHeadset
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.hardware.usb.UsbManager
import android.media.AudioManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import com.jiangkang.hybrid.Khybrid
import com.jiangkang.ktools.databinding.ActivityMainBinding
import com.jiangkang.ktools.lifecycle.LogLifecycleObserver
import com.jiangkang.ktools.receiver.HeadSetBroadcastReceiver
import com.jiangkang.ktools.receiver.USBReceiver
import com.jiangkang.tools.utils.ToastUtils

/**
 * Adapter : [FunctionAdapter]
 */
open class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding
    private val tag = "${javaClass.simpleName}_Lifecycle"
    private val mHeadsetReceiver = HeadSetBroadcastReceiver()
    private val mUsbReceiver = USBReceiver()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        lifecycle.addObserver(LogLifecycleObserver())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filter = IntentFilter().apply {
            // 有线耳机
            addAction(Intent.ACTION_HEADSET_PLUG)
            addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

            // 蓝牙耳机
            addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
        }

        val usbFilter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        
        registerReceiver(mHeadsetReceiver,filter)
        registerReceiver(mUsbReceiver,usbFilter)

        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(tag,"onCreate(${savedInstanceState.toString()})")
        setContentView(binding.root)
        initViews()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(tag,"onNewIntent()")
        if (Intent.FLAG_ACTIVITY_CLEAR_TOP and intent.flags != 0) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag,"onResume()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(tag,"onConfigurationChanged($newConfig)")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 0, 0, "关于")
        menu.add(0, 1, 1, "源代码")
        menu.add(0, 2, 2, "文章解析")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> startActivity(Intent(this@MainActivity, AboutActivity::class.java))
            1 -> openBrowser("https://github.com/jiangkang/KTools")
            2 -> openBrowser("http://www.jianshu.com/u/2c22c64b9aff")
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openBrowser(url: String) {
        Khybrid().loadUrl(this, url)
    }

    private fun initViews() {
        binding.rcFunctionList.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 4)
            adapter = FunctionAdapter(this@MainActivity)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag,"onStart()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag,"onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag,"onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag,"onDestroy()")
        unregisterReceiver(mHeadsetReceiver)
        unregisterReceiver(mUsbReceiver)
    }

    override fun finish() {
        super.finish()
        Log.d(tag,"finish()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag,"onSaveInstanceState($outState)")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.d(tag,"onSaveInstanceState($outState,$outPersistentState)")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(tag,"onRestoreInstanceState($savedInstanceState)")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Log.d(tag,"onRestoreInstanceState($savedInstanceState,$persistentState)")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag,"onRestart()")
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}