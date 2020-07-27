package com.jiangkang.tools.bean

/**
 * Created by jiangkang on 2017/10/31.
 * description：歌曲实体类
 */

data class Song(
        var name: String? = null,
        var artist: String? = null,
        var coverUri: String? = null,
        var time: Long = 0,
        var location: String? = null
)
