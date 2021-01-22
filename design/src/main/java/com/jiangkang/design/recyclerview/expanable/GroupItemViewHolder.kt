package com.jiangkang.design.recyclerview.expanable

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open abstract class GroupItemViewHolder : RecyclerView.ViewHolder, View.OnClickListener {

    var listener: GroupItemViewHolder? = null

    constructor(itemView: View) : super(itemView) {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        listener?.onClick(view)
    }

    abstract fun expand()
    abstract fun collapse()

}