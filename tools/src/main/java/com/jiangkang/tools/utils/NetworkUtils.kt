package com.jiangkang.tools.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.jiangkang.tools.King
import java.net.NetworkInterface
import java.util.*

/**
 * Created by jiangkang on 2017/9/22.
 */
object NetworkUtils {
    private val TAG = DeviceUtils::class.java.simpleName
    private const val NETWORK_TYPE_WIFI = "wifi"
    private const val NETWORK_TYPE_MOBILE = "mobile"
    private const val NETWORK_TYPE_OTHER = "other"
    private const val MAC_ADDRESS_DEFAULT = "02:00:00:00:00:00"
    private var sWifiManager: WifiManager? = null
    private var sConnectivityManager: ConnectivityManager? = null
    const val NETWORK_TYPE_OFFLINE = "offline"
    val netWorkType: String
        get() {
            val networkInfo = sConnectivityManager!!.activeNetworkInfo
            val mobileInfo = sConnectivityManager!!.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifiInfo = sConnectivityManager!!.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return if (networkInfo == null || !networkInfo.isAvailable) {
                NETWORK_TYPE_OFFLINE
            } else if (wifiInfo != null && wifiInfo.isAvailable) {
                NETWORK_TYPE_WIFI
            } else if (mobileInfo != null && mobileInfo.isAvailable) {
                NETWORK_TYPE_MOBILE
            } else {
                NETWORK_TYPE_OTHER
            }
        }

    /*
    * 获取Wifi信号等级，分为0-4五个等级
    * */
    val signalLevel: Int
        get() {
            val wifiInfo = sWifiManager!!.connectionInfo
            return WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
        }

    val macAddress: String
        get() {
            val wifiInfo = sWifiManager!!.connectionInfo
            var macAddress = wifiInfo.macAddress
            if (MAC_ADDRESS_DEFAULT != macAddress) {
                return macAddress
            }
            macAddress = macAddressByNetworkInterface
            return if (MAC_ADDRESS_DEFAULT != macAddress) {
                macAddress
            } else MAC_ADDRESS_DEFAULT
        }

    /**
     * 获取设备MAC地址
     *
     * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
     *
     * @return MAC地址
     */
    private val macAddressByNetworkInterface: String
        private get() {
            try {
                val nis: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (ni in nis) {
                    if (!ni.name.equals("wlan0", ignoreCase = true)) continue
                    val macBytes = ni.hardwareAddress
                    if (macBytes != null && macBytes.size > 0) {
                        val res1 = StringBuilder()
                        for (b in macBytes) {
                            res1.append(String.format("%02x:", b))
                        }
                        return res1.deleteCharAt(res1.length - 1).toString()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "02:00:00:00:00:00"
        }

    /**
     * 获取设备MAC地址
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
     *
     * @return MAC地址
     */
    @get:SuppressLint("HardwareIds")
    private val macAddressByWifiInfo: String
        private get() {
            try {
                @SuppressLint("WifiManagerLeak") val wifi = King.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                if (wifi != null) {
                    val info = wifi.connectionInfo
                    if (info != null) return info.macAddress
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "02:00:00:00:00:00"
        }

    val isNetAvailable: Boolean
        get() = sConnectivityManager!!.activeNetworkInfo != null

    init {
        sWifiManager = King.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        sConnectivityManager = King.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}