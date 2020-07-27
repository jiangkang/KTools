package com.jiangkang.tools.struct

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by jiangkang on 2017/9/13.
 */
class JsonGenerator {
    private var json = JSONObject()
    private val array = JSONArray()
    fun put(name: String?, value: Any?): JsonGenerator {
        val tmpObj = json
        try {
            json.put(name, value)
        } catch (e: JSONException) {
            json = tmpObj
        }
        return this
    }

    fun gen(): JSONObject {
        return json
    }
}