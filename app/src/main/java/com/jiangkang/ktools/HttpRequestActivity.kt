package com.jiangkang.ktools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivityHttpRequestBinding
import com.jiangkang.tools.utils.ToastUtils
import okhttp3.*
import okio.ByteString

class HttpRequestActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHttpRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHttpRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimpleWebsocket.setOnClickListener {
              initWebSocket()
        }
    }

    private fun initWebSocket() {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
//                .url("ws://echo.websocket.org")
                .url("ws://192.168.1.6:80")
                .build()
        okHttpClient.newWebSocket(request,object : WebSocketListener(){

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                ToastUtils.showShortToast("OnOpen")
                webSocket.send("Hello...");
                webSocket.send("...World!");
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                ToastUtils.showShortToast(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                ToastUtils.showShortToast(bytes.toString())
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                ToastUtils.showShortToast("OnClosed")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                ToastUtils.showShortToast("OnFailure: ${response?.message},${t.message}")
            }

        })
    }
}