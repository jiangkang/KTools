package com.jiangkang.hybrid.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class JsRequest(
        @SerializedName("id")
        val id: String,

        @SerializedName("class")
        val className: String,

        @SerializedName("method")
        val methodName: String,

        @SerializedName("data")
        val data: String
)