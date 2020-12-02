package com.jiangkang.ktools.requests

import okhttp3.*
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Socket
import java.security.cert.X509Certificate
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object RequestHelper {

    val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(KSSLSocketFactory(), KX509TrustManager())
            .eventListenerFactory(KEventListenerFactory())
            .dns(KDns())
            .authenticator(KAuthenticator())
            .build()
}

internal class KAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        TODO("Not yet implemented")
    }

}

internal class KDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        TODO("Not yet implemented")
    }
}

internal class KX509TrustManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        TODO("Not yet implemented")
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        TODO("Not yet implemented")
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        TODO("Not yet implemented")
    }
}

internal class KSSLSocketFactory : SSLSocketFactory() {
    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        TODO("Not yet implemented")
    }

    override fun createSocket(host: String?, port: Int): Socket {
        TODO("Not yet implemented")
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        TODO("Not yet implemented")
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        TODO("Not yet implemented")
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        TODO("Not yet implemented")
    }

    override fun getDefaultCipherSuites(): Array<String> {
        TODO("Not yet implemented")
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return arrayOf<String>(
                CipherSuite.TLS_AES_128_CCM_8_SHA256.javaName
        )
    }

}

internal class KEventListenerFactory : EventListener.Factory {
    override fun create(call: Call): EventListener {
        return object : EventListener() {
            override fun callStart(call: Call) {
                super.callStart(call)
            }

            override fun callEnd(call: Call) {
                super.callEnd(call)
            }

            override fun callFailed(call: Call, ioe: IOException) {
                super.callFailed(call, ioe)
            }

            override fun proxySelectStart(call: Call, url: HttpUrl) {
                super.proxySelectStart(call, url)
            }

            override fun proxySelectEnd(call: Call, url: HttpUrl, proxies: List<Proxy>) {
                super.proxySelectEnd(call, url, proxies)
            }

            override fun dnsStart(call: Call, domainName: String) {
                super.dnsStart(call, domainName)
            }

            override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
                super.dnsEnd(call, domainName, inetAddressList)
            }

            override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
                super.connectStart(call, inetSocketAddress, proxy)
            }

            override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
                super.connectEnd(call, inetSocketAddress, proxy, protocol)
            }

            override fun connectFailed(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?, ioe: IOException) {
                super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
            }

            override fun canceled(call: Call) {
                super.canceled(call)
            }

        }
    }
}