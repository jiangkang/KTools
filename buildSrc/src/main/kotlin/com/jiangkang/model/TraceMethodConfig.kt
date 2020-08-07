package com.jiangkang.model

open class TraceMethodConfig{
    /**
     * 是否开启
     */
    var enable:Boolean = false

    /**
     * 输出
     */
    var output:String? = null

    /**
     * 配置文件所在路径
     */
    var configFilePath:String? = null

    /**
     * 是否开启log
     */
    var enableLog:Boolean = false
}