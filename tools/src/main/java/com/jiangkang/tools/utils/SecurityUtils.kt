package com.jiangkang.tools.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException

/**
 * Created by jiangkang on 2017/9/11.
 */
object SecurityUtils {
    fun encodeByBase64(string: String): String {
        var result = ""
        try {
            result = Base64.encodeToString(string.toByteArray(charset("utf-8")), Base64.DEFAULT)
        } catch (e: UnsupportedEncodingException) {
        }
        return result
    }

    fun bmp2base64(bitmap: Bitmap?, compressFormat: CompressFormat?, quality: Int): String {
        return if (bitmap == null) {
            ""
        } else {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(compressFormat, quality, outputStream)
            try {
                outputStream.flush()
                outputStream.close()
                val bytes = outputStream.toByteArray()
                Base64.encodeToString(bytes, Base64.DEFAULT)
            } catch (e: IOException) {
                ""
            }
        }
    }
}