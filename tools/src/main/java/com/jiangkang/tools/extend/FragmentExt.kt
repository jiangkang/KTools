package com.jiangkang.tools.extend

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun <T:View>Fragment.findViewById(@IdRes id:Int):View?{
    return activity?.findViewById<T>(id)
}