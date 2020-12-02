package com.jiangkang.ktools

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Debug
import android.os.StrictMode
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.multidex.MultiDex
import com.facebook.drawee.backends.pipeline.Fresco
import com.jiangkang.hack.HookUtils
import com.jiangkang.hack.hook.ActivityStartingCallback
import com.jiangkang.ktools.receiver.KToolsAppWidgetProvider
import com.jiangkang.ktools.works.AppOptWorker
import com.jiangkang.ndk.NdkMainActivity
import com.jiangkang.tools.King
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KNotification
import com.jiangkang.tools.widget.KShortcut
import okhttp3.*
import okio.ByteString

/**
 * @author jiangkang
 * @date 2017/9/6
 */
open class KApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        KNotification.createNotificationChannel(base)
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            val stackTrace = e.stackTrace
            val reason = StringBuilder()
            reason.appendln(e.message)
            stackTrace.forEach {
                reason.appendln(it.toString())
            }
            val intent = Intent(applicationContext, CrashInfoActivity::class.java)
                    .apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra("crash_info", reason.toString())
                    }
            startActivity(intent)
        }
        HookUtils.hookInstrumentation(object : ActivityStartingCallback {
            override fun activityStarting(source: Context, target: Activity, intent: Intent) {
                ToastUtils.showShortToast("启动了${intent.component?.shortClassName}")
            }
        })
    }

    override fun onCreate() {
        Debug.startMethodTracing()
        super.onCreate()

        enableStrictMode()

        King.init(this)

        Debug.stopMethodTracing()

        Fresco.initialize(this)

        AppOptWorker.launch(this)

        initShortcuts()

        initWidgets()


    }

    /**
     * 创建固定微件
     */
    private fun initWidgets() {
        val appWidgetManager: AppWidgetManager = applicationContext.getSystemService(AppWidgetManager::class.java)
        val myProvider = ComponentName(applicationContext, KToolsAppWidgetProvider::class.java)
        val successCallback: PendingIntent? = if (appWidgetManager.isRequestPinAppWidgetSupported) {
            Intent(applicationContext,MainActivity::class.java).let { intent ->
                PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        } else {
            null
        }
        successCallback?.also { pendingIntent ->
            appWidgetManager.requestPinAppWidget(myProvider, null, pendingIntent)
        }
    }

    private fun initShortcuts() {
        val shortcutSystem = KShortcut.createShortcutInfo(this,"system","System","System Demos",R.drawable.ic_system,Intent(this,SystemActivity::class.java))
        val shortcutUI = KShortcut.createShortcutInfo(this,"ui","UI","UI Demos",R.drawable.ic_widget,Intent(this,WidgetActivity::class.java))
        val shortcutNdk = KShortcut.createShortcutInfo(this,"ndk","NDK","NDK Demos",R.drawable.ic_cpp,Intent(this,NdkMainActivity::class.java))

        ShortcutManagerCompat.addDynamicShortcuts(this,listOf(shortcutSystem,shortcutUI,shortcutNdk))

        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)){
            val pinIntent = ShortcutManagerCompat.createShortcutResultIntent(this,shortcutNdk)
            val successCallback = PendingIntent.getBroadcast(this,0,pinIntent,0)
            ShortcutManagerCompat.requestPinShortcut(this,shortcutNdk,successCallback.intentSender)
        }
    }

    private fun enableStrictMode() {
        if (BuildConfig.DEBUG) {
            enableThreadPolicy()
            enableVmPolicy()
        }
    }

    private fun enableVmPolicy() {
        StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build()
        )
    }

    private fun enableThreadPolicy() {
        StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build()
        )
    }

}
