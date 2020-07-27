package com.jiangkang.tools.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

/**
 * Created by jiangkang on 2017/9/17.
 * 二维码工具类
 */
object QRCodeUtils {
    /**
     * @param content 二维码内容
     * @param width 宽度px
     * @param height 高度px
     * @param characterSet 字符编码， 支持格式:[CharacterSetECI]，传null时,zxing源码默认使用 "ISO-8859-1"
     * @param errorCorrection 容错级别，(支持级别:[ErrorCorrectionLevel])。传null时,zxing源码默认使用 "L"
     * @param margin 空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"
     * @param colorBlack 黑色色块的自定义颜色值
     * @param colorWhite 白色色块的自定义颜色值
     * @return 二维码Bitmap
     */
    @JvmOverloads
    fun createQRCodeBitmap(content: String,
                           @IntRange(from = 0) width: Int,
                           @IntRange(from = 0) height: Int,
                           characterSet: String? =
                                   CharacterSetECI.UTF8.name,
                           errorCorrection: String? =
                                   ErrorCorrectionLevel.H.name,
                           margin: String? =
                                   "2",
                           @ColorInt colorBlack: Int =
                                   Color.BLACK,
                           @ColorInt colorWhite: Int =
                                   Color.WHITE
    ): Bitmap? {
        val hints = Hashtable<EncodeHintType, String?>()
        if (!TextUtils.isEmpty(characterSet)) {
            hints[EncodeHintType.CHARACTER_SET] = characterSet
        }
        if (!TextUtils.isEmpty(errorCorrection)) {
            hints[EncodeHintType.ERROR_CORRECTION] = errorCorrection
        }
        if (!TextUtils.isEmpty(margin)) {
            hints[EncodeHintType.MARGIN] = margin
        }
        try {
            val bitMatrix = QRCodeWriter()
                    .encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = colorBlack // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = colorWhite // 白色色块像素设置
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}