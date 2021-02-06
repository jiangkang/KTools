package com.jiangkang.vpn

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import com.jiangkang.vpn.databinding.LayoutToyVpnClientActivityBinding

class ToyVpnClientActivity : Activity(){

    private val binding by lazy { LayoutToyVpnClientActivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnConnect.setOnClickListener {
            val intent = VpnService.prepare(this@ToyVpnClientActivity)
            if (intent != null) {
                startActivityForResult(intent, 0)
            } else {
                onActivityResult(0, RESULT_OK, null)
            }
        }

        binding.btnDisconnect.setOnClickListener {
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