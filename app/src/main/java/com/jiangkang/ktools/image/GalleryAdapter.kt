package com.jiangkang.ktools.image

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jiangkang.ktools.R
import com.jiangkang.tools.bean.ImagePool
import java.util.*

/**
 * Created by jiangkang on 2017/10/16.
 */

class GalleryAdapter(private val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var urlList: ArrayList<String>? = null

    init {
        loadData()
    }

    private fun loadData() {
        urlList = ArrayList()
        ImagePool.urls.map {
            urlList?.add(it)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_image, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
                .load(urlList!![position])
                .into(holder.ivContent)
    }

    override fun getItemCount(): Int {
        return urlList?.size ?: 0
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var ivContent: ImageView = itemView.findViewById<View>(R.id.iv_content) as ImageView

    }


}
