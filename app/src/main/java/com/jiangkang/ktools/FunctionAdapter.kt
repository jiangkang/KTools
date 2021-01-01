package com.jiangkang.ktools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.jiangkang.hack.HackActivity
import com.jiangkang.ktools.BaseActivity.Companion.VIEW_NAME_HEADER_TITLE
import com.jiangkang.ktools.effect.EffectActivity
import com.jiangkang.ktools.web.HybridActivity
import com.jiangkang.ndk.NdkMainActivity
import com.jiangkang.video.KVideoActivity
import com.jiangkang.vpn.ToyVpnClientActivity
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * Created by jiangkang on 2017/9/5.
 * description：Function Module Adapter
 */
class FunctionAdapter(private val mContext: Context) : RecyclerView.Adapter<FunctionAdapter.ViewHolder>() {
    private val functionList = ArrayList<FunctionEntity>()
    private fun loadData() {
        functionList.add(FunctionEntity("System", SystemActivity::class.java, R.drawable.ic_system))
        functionList.add(FunctionEntity("UI", WidgetActivity::class.java, R.drawable.ic_widget))
        functionList.add(FunctionEntity("Storage", StorageActivity::class.java, R.drawable.ic_storage))
        functionList.add(FunctionEntity("HTTP", HttpRequestActivity::class.java, R.drawable.ic_requests))
        functionList.add(FunctionEntity("Device", DeviceActivity::class.java, R.drawable.ic_device))
        functionList.add(FunctionEntity("Image", ImageActivity::class.java, R.drawable.ic_image))
        functionList.add(FunctionEntity("Scan", ScanActivity::class.java, R.drawable.ic_scan))
        functionList.add(FunctionEntity("Audio", AudioActivity::class.java, R.drawable.ic_audio))
        functionList.add(FunctionEntity("Effect", EffectActivity::class.java, R.drawable.ic_effect))
        functionList.add(FunctionEntity("Web", HybridActivity::class.java, R.drawable.ic_web))
        functionList.add(FunctionEntity("Hack", HackActivity::class.java, R.drawable.ic_widget))
        functionList.add(FunctionEntity("VPN", ToyVpnClientActivity::class.java, R.drawable.ic_web))
        functionList.add(FunctionEntity("NDK", NdkMainActivity::class.java,R.drawable.ic_cpp))
        functionList.add(FunctionEntity("Flutter", FlutterEntryActivity::class.java,R.drawable.ic_flutter))
        functionList.add(FunctionEntity("Video", KVideoActivity::class.java,R.drawable.ic_music_paly))
        functionList.add(FunctionEntity("Tools", ToolsActivity::class.java,R.mipmap.ic_launcher))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.layout_item_function, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = functionList[position]
        holder.itemView.setOnClickListener {
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((mContext as Activity),
                    holder.mTvFunctionName!!, VIEW_NAME_HEADER_TITLE)
            ActivityCompat.startActivity(mContext, Intent(mContext, entity.activity), optionsCompat.toBundle())
        }
        holder.mTvFunctionName!!.text = entity.name
        if (entity.resId != -1) {
            holder.mIvFunctionIcon!!.setImageResource(entity.resId)
        }
    }

    override fun getItemCount(): Int {
        return functionList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvFunctionIcon: ImageView by lazy {view.findViewById<ImageView>(R.id.iv_function_icon)}
        val mTvFunctionName: TextView by lazy {view.findViewById<TextView>(R.id.tv_function_name)}
    }

    init {
        loadData()
    }
}