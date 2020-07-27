package com.jiangkang.tools.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Matrix
import android.util.Base64
import android.util.Log
import androidx.print.PrintHelper
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created by jiangkang on 2017/9/19.
 * 图片相关工具类
 */
object ImageUtils {
    private const val TAG = "ImageUtils"
    fun bitmap2Bytes(bitmap: Bitmap?, quality: Int, format: CompressFormat?): ByteArray? {
        return if (bitmap == null) {
            null
        } else {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, outputStream)
            try {
                outputStream.flush()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            outputStream.toByteArray()
        }
    }

    fun bitmap2Base64(bitmap: Bitmap?, quality: Int, format: CompressFormat?): String {
        val bytes = bitmap2Bytes(bitmap, quality, format)
        return Base64.encodeToString(bytes, Base64.DEFAULT)
                .replace("\n", "")
                .replace("\r", "")
                .replace("\t", "")
    }

    fun scaleBitmap(srcBitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = srcBitmap.width
        val height = srcBitmap.height
        Log.d(TAG, "scaleBitmap: \nwidth = $width\nheight = $height")
        val desiredWidth = Math.min(width, maxWidth)
        val desiredHeight = Math.min(height, maxHeight)
        val scaleWidth = desiredWidth.toFloat() / width
        val scaleHeight = desiredHeight.toFloat() / height
        val scaled = Math.min(scaleHeight, scaleWidth)
        val matrix = Matrix()
        matrix.postScale(scaled, scaled)
        return Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true)
    }

    fun convert2Gray(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixs = IntArray(width * height)
        bitmap.getPixels(pixs, 0, width, 0, 0, width, height)
        val alpha = 0xFF shl 24
        for (i in 0 until height) {
            for (j in 0 until width) {
                var color = pixs[width * i + j]
                val red = color and 0x00FF0000 shr 16
                val green = color and 0x0000FF00 shr 8
                val blue = color and 0x000000FF
                color = (red + green + blue) / 3
                color = alpha or (color shl 16) or (color shl 8) or color
                pixs[width * i + j] = color
            }
        }
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        result.setPixels(pixs, 0, width, 0, 0, width, height)
        return result
    }

    fun printBitmap(context: Context?, bitmap: Bitmap?) {
        val helper = PrintHelper(context!!)
        helper.scaleMode = PrintHelper.SCALE_MODE_FIT
        helper.printBitmap("print_" + System.currentTimeMillis().toString(), bitmap!!)
    }
}