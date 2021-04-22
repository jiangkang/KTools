package com.jiangkang.ktools.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.jiangkang.tools.utils.ToastUtils

class USBReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
          when(intent.action){
              UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                  ToastUtils.showShortToast("检测到USB插入")
                  val device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as UsbDevice?
                  device?.let {
                       ToastUtils.showShortToast("${it.deviceName}/${it.deviceProtocol}/${it.deviceId}/${it.productId}/${it.productName}")
                  }
              }
              UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                  ToastUtils.showShortToast("检测到USB拔出")
              }
          }
    }
}