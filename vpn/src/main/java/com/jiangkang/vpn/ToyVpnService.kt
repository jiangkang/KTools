package com.jiangkang.vpn

import android.app.*
import android.content.Intent
import android.net.VpnService
import android.os.Handler
import android.os.Message
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.util.Pair
import com.jiangkang.tools.extend.notificationManager
import com.jiangkang.tools.utils.ToastUtils
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import kotlin.concurrent.thread

class ToyVpnService : VpnService(), Handler.Callback {

    private val mHandler = Handler(this)

    /**
     * 用于点击通知栏的时候启动VPN Activity
     */
    private var mConfigIntent: PendingIntent? = null

    override fun onCreate() {
        super.onCreate()
        mConfigIntent = PendingIntent.getActivity(
                this,
                0,
                Intent(this, ToyVpnClientActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return when (intent.action) {
            ACTION_DISCONNECT -> {
                disconnect()
                Service.START_NOT_STICKY
            }
            else -> {
                connect()
                Service.START_STICKY
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnect()
    }

    private var server = "127.0.0.1"
    private val port = 8888
    private val proxyHost = ""
    private val proxyPort = 8888

    private fun connect() {
        // 更新通知栏信息
        updateForegroundNotification(R.string.connecting)
        mHandler.sendEmptyMessage(R.string.connecting)

        thread {
            val serverAddress = InetSocketAddress(server,port)
            val tunnel = DatagramChannel.open()
            if (!protect(tunnel.socket())){
                ToastUtils.showShortToast("Cannot protect the tunnel")
            }
            tunnel.connect(serverAddress)
            tunnel.configureBlocking(false)

            val pfd = Builder()
                    .apply {
                        addAddress(server,port)
                        addRoute("0:0:0:0",0)
                        setSession(server)
                        setConfigureIntent(mConfigIntent)
                    }.establish()
            val fis = FileInputStream(pfd.fileDescriptor)
            val fos = FileOutputStream(pfd.fileDescriptor)

            val packet =  ByteBuffer.allocate(Short.MAX_VALUE.toInt())
            while (true){
                val len = fis.read(packet.array())
                if (len > 0){
                    packet.limit(len)
                    tunnel.write(packet)
                    packet.clear()
                    Log.d("Toy",packet.toString())
                }
            }
        }


        
    }

    private fun disconnect() {
        mHandler.sendEmptyMessage(R.string.disconnected)
        stopForeground(true)
    }

    override fun handleMessage(msg: Message): Boolean {
        ToastUtils.showShortToast(msg.what.toString())
        if (msg.what != R.string.disconnected) {
            updateForegroundNotification(msg.what)
        }
        return true
    }

    private fun updateForegroundNotification(msg: Int) {
        val channelId = "ToyVpn"
        val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        val notification = Notification.Builder(this, channelId)
                .setContentText(getString(msg))
                .setContentIntent(mConfigIntent)
                .build()
        startForeground(0, notification)
    }

    companion object {
        const val ACTION_CONNECT = "com.jiangkang.toyvpn.start"
        const val ACTION_DISCONNECT = "com.jiangkang.toyvpn.stop"
    }


    class Connection(thread: Thread, parcelFileDescriptor: ParcelFileDescriptor) : Pair<Thread, ParcelFileDescriptor>(thread, parcelFileDescriptor)
}