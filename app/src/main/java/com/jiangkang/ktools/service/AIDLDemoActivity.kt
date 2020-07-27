package com.jiangkang.ktools.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.IComputation
import com.jiangkang.ktools.R
import com.jiangkang.tools.utils.ToastUtils
import java.util.*
import java.util.concurrent.Executors

class AIDLDemoActivity : AppCompatActivity() {

    val mTvAdd: TextView by lazy {findViewById<TextView>(R.id.tv_add)}
    val mTvSub: TextView by lazy {findViewById<TextView>(R.id.tv_sub)}
    val mTvMul: TextView by lazy {findViewById<TextView>(R.id.tv_mul)}
    val mTvDel: TextView by lazy {findViewById<TextView>(R.id.tv_del)}

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val computation = IComputation.Stub.asInterface(service)
            val mAddTask = Runnable {
                for (i in 0..119) {
                    val a = Random().nextFloat() * 10
                    val b = Random().nextFloat() * 10
                    try {
                        val addResult = computation.add(a, b)
                        val subResult = computation.sub(a, b)
                        val mulResult = computation.mul(a, b)
                        val delResult = computation.del(a, b)
                        Thread.sleep(500)
                        runOnUiThread {
                            mTvAdd!!.text = addResult.toString()
                            mTvSub!!.text = subResult.toString()
                            mTvMul!!.text = mulResult.toString()
                            mTvDel!!.text = delResult.toString()
                            ToastUtils.showShortToast("执行了" + (i + 1) + "次")
                        }
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            Executors.newCachedThreadPool().submit(mAddTask)
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidldemo)
        val intent = Intent(this, ComputationService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(mConnection)
        super.onDestroy()
    }

    companion object {
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, AIDLDemoActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}