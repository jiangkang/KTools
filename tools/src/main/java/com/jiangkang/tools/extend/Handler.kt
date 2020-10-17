package com.jiangkang.tools.extend

import android.os.Handler
import android.os.Looper

/**
 * Created by jiangkang on 2018/2/9.
 * description：
 */

fun mainHandler(){
    Handler(Looper.getMainLooper()).post {  }
     
}
