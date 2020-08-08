package com.jiangkang.vpn

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import kotlinx.android.synthetic.main.layout_toy_vpn_client_activity.*


class ToyVpnClientActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_toy_vpn_client_activity)

        btn_connect.setOnClickListener {
            val intent = VpnService.prepare(this@ToyVpnClientActivity)
            if (intent != null) {
                startActivityForResult(intent, 0)
            } else {
                onActivityResult(0, RESULT_OK, null)
            }
        }

        btn_disconnect.setOnClickListener {
            startService(getServiceIntent().setAction(ToyVpnService.ACTION_DISCONNECT))
        }
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?) {
        if (result == RESULT_OK && request == 0) {
            startService(getServiceIntent().setAction(ToyVpnService.ACTION_CONNECT))
        }
    }

    private fun getServiceIntent(): Intent {
        return Intent(this, ToyVpnService::class.java)
    }
}