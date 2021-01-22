package com.jiangkang.design.recyclerview.expanable

interface ExpandableItemClickListener {
    fun onGroupClick(position: Int)
    fun onChildClick(position: Int)
}