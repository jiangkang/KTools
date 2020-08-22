package com.jiangkang.tools.utils

import android.content.Context
import android.view.LayoutInflater

object LayoutInflaterUtils {
    fun compileLayout(context:Context,layoutId:Int){
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}