package com.jiangkang.ktools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_http_request.*
import okhttp3.*
import okio.ByteString

class HttpRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http_request)

        btn_simple_websocket.setOnClickListener {
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