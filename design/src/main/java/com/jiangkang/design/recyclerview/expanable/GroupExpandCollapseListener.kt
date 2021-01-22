package com.jiangkang.design.recyclerview.expanable

interface GroupExpandCollapseListener {
    fun onExpanded(positionStart: Int, childrenCount: Int)
    fun onCollapsed(positionStart: Int, childrenCount: Int)
}