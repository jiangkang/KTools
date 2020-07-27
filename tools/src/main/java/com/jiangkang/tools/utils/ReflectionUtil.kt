package com.jiangkang.tools.utils

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * Created by jiangkang on 2018/1/29.
 * description：反射工具类
 */
object ReflectionUtil {
    /**
     * @param clazz 类，如Object.class
     * @return 包名
     */
    fun getPackageName(clazz: Class<*>): String {
        return getPackageName(clazz.name)
    }

    /**
     * @param classFullName 类名
     * @return 包名
     */
    @JvmStatic
    fun getPackageName(classFullName: String): String {
        val lastDot = classFullName.lastIndexOf('.')
        return if (lastDot < 0) "" else classFullName.substring(0, lastDot)
    }

    /**
     * 确保传入的类被初始化，适用于有静态初始化的类
     *
     * @param classes 要初始化的类
     */
    @JvmStatic
    fun initialize(vararg classes: Class<*>) {
        for (clazz in classes) {
            try {
                Class.forName(clazz.name, true, clazz.classLoader)
            } catch (e: ClassNotFoundException) {
                throw AssertionError(e)
            }
        }
    }

    @JvmStatic
    fun <T> newProxy(interfaceType: Class<T>, handler: InvocationHandler?): T? {
        val `object` = Proxy.newProxyInstance(
                interfaceType.classLoader, arrayOf<Class<*>>(interfaceType), handler
        )
        //转换对象
        return interfaceType.cast(`object`)
    }
}