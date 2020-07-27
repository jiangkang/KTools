package com.jiangkang.tools.utils

/**
 *
 * @author jiangkang
 * @date 2017/10/20
 */
object Utils {
    fun <T> checkNotNull(`object`: T?, message: String?): T {
        if (`object` == null) {
            throw NullPointerException(message)
        }
        return `object`
    }
}