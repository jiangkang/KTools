package com.jiangkang.tools.utils

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Created by jiangkang on 2018/2/5.
 * description：
 */
@RunWith(RobolectricTestRunner::class)
class AppUtilsTest {

    @Test
    fun isDebug() {
        assertEquals(true,AppUtils.isDebug)
    }

}