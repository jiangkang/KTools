package com.jiangkang.tools.utils

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * @author jiangkang
 * @date 2017/9/24
 *
 * SharedPreference 工具类
 */
class SpUtils private constructor(context: Context, name: String) {
    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    fun <T> put(key: String?, value: T): SpUtils {
        if (value is String) {
            editor.putString(key, value as String)
        } else if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean))
        } else if (value is Int) {
            editor.putInt(key, (value as Int))
        } else if (value is Long) {
            editor.putLong(key, (value as Long))
        } else if (value is Float) {
            editor.putFloat(key, (value as Float))
        } else {
            throw IllegalArgumentException("value can not support")
        }
        editor.apply()
        return this
    }

    fun putString(key: String?, value: String?): SpUtils {
        editor.putString(key, value).commit()
        return this
    }

    fun putBoolean(key: String?, value: Boolean): SpUtils {
        editor.putBoolean(key, value).commit()
        return this
    }

    fun putInt(key: String?, value: Int): SpUtils {
        editor.putInt(key, value).commit()
        return this
    }

    fun putFloat(key: String?, value: Float): SpUtils {
        editor.putFloat(key, value).commit()
        return this
    }

    fun putLong(key: String?, value: Long): SpUtils {
        editor.putLong(key, value).commit()
        return this
    }

    fun putJsonObject(key: String?, `object`: JSONObject): SpUtils {
        editor.putString(key, `object`.toString())
        return this
    }

    fun getJsonObject(key: String?): JSONObject? {
        val jsonString = preferences.getString(key, "{}")
        return try {
            JSONObject(jsonString)
        } catch (e: JSONException) {
            null
        }
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }

    val all: Map<String, *>
        get() = preferences.all

    companion object {
        private const val DEFAULT_PREF_NAME = "ktools_pref"
        @JvmStatic
        fun getInstance(context: Context, name: String): SpUtils {
            return SpUtils(context, name)
        }
    }

    init {
        preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }
}