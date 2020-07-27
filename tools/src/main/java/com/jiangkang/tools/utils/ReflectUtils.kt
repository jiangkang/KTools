package com.jiangkang.tools.utils

object ReflectUtils {

    fun getClassByName(className: String): Class<*> {
        return Class.forName(className)

    }
    
}