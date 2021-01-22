package com.jiangkang.design.recyclerview

import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.jiangkang.design.recyclerview.expanable.ChildItemViewHolder
import com.jiangkang.design.recyclerview.expanable.GroupItemViewHolder

class ExpandableItemAdapter<GVH : GroupItemViewHolder, CVH : ChildItemViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>(),ExpandableListView.OnGroupClickListener{

    override fun onGroupClick(parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}