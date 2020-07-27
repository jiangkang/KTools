package com.jiangkang.ktools.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jiangkang.ktools.R

/**
 * Created by jiangkang on 2017/10/5.
 * description：
 */
class TextAdapter(private val mContext: Context, private val mDataList: List<String>?) : RecyclerView.Adapter<TextAdapter.ViewHolder>() {
    private var mListener: onItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_text, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mDataList != null && mDataList.isNotEmpty()) {
            val content = mDataList[position]
            holder.tvItem.text = content
            holder.itemView.setOnClickListener {
                if (mListener != null) {
                    mListener!!.onClick(holder, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mDataList != null && mDataList.size > 0) {
            mDataList.size
        } else 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView = itemView.findViewById<View>(R.id.tv_item) as TextView
    }

    fun setOnItemClickListener(listener: onItemClickListener?) {
        mListener = listener
    }

    interface onItemClickListener {
        fun onClick(holder: ViewHolder?, position: Int)
    }

}