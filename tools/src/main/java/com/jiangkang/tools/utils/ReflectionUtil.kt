package com.jiangkang.tools.utils

import java.lang.reflect.Field

/**
 * Created by jiangkang on 2018/1/29.
 * description：反射工具类
 */
object ReflectionUtil {

    fun <T : Any> findField(obj: T, fieldName: String): Field? {
       var clz:Class<*>? = obj.javaClass
        while (clz !== null){
            try {
                val field = clz.getDeclaredField(fieldName)
                if (!field.isAccessible){
                    field.isAccessible = true
                }
                return field
            }catch (e:NoSuchFieldException){
                clz = clz.superclass
            }
        }
        return null
    }
}