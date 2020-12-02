package com.jiangkang.tools.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CoroutineUtils {

    fun createScope(){
        CoroutineScope(Dispatchers.Main).launch {
            // doSomething()
        }
    }

}