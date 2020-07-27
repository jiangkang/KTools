package com.jiangkang.ktools.audio

import android.text.TextUtils
import java.util.*

/**
 *
 * @author jiangkang
 * @date 2017/10/17
 */
class VoiceTemplate {
    private var numString: String? = null

    private var prefix: String? = null

    private var suffix: String? = null


    fun prefix(prefix: String?): VoiceTemplate {
        this.prefix = prefix
        return this
    }

    fun suffix(suffix: String?): VoiceTemplate {
        this.suffix = suffix
        return this
    }

    fun numString(numString: String?): VoiceTemplate {
        this.numString = numString
        return this
    }

    fun gen(): List<String?> {
        return genVoiceList()
    }

    private fun createReadableNumList(numString: String): List<String> {
        val result: MutableList<String> = ArrayList()
        if (!TextUtils.isEmpty(numString)) {
            val len = numString.length
            for (i in 0 until len) {
                if ('.' == numString[i]) {
                    result.add("dot")
                } else {
                    result.add(numString[i].toString())
                }
            }
        }
        return result
    }

    private fun genVoiceList(): List<String?> {
        val result: MutableList<String?> = ArrayList()
        if (!TextUtils.isEmpty(prefix)) {
            result.add(prefix)
        }
        if (!TextUtils.isEmpty(numString)) {
            result.addAll(genReadableMoney(numString))
        }
        if (!TextUtils.isEmpty(suffix)) {
            result.add(suffix)
        }
        return result
    }

    private fun genReadableMoney(numString: String?): List<String> {
        val result: MutableList<String> = ArrayList()
        if (!TextUtils.isEmpty(numString)) {
            if (numString!!.contains(DOT)) {
                val integerPart = numString.split("\\.".toRegex()).toTypedArray()[0]
                val decimalPart = numString.split("\\.".toRegex()).toTypedArray()[1]
                val intList = readIntPart(integerPart)
                val decimalList = readDecimalPart(decimalPart)
                result.addAll(intList)
                if (!decimalList.isEmpty()) {
                    result.add("dot")
                    result.addAll(decimalList)
                }
            } else {
                //int
                result.addAll(readIntPart(numString))
            }
        }
        return result
    }

    private fun readDecimalPart(decimalPart: String): List<String> {
        val result: MutableList<String> = ArrayList()
        if ("00" != decimalPart) {
            val chars = decimalPart.toCharArray()
            for (ch in chars) {
                result.add(ch.toString())
            }
        }
        return result
    }

    private fun readIntPart(integerPart: String?): List<String> {
        val result: MutableList<String> = ArrayList()
        val intString = readInt(integerPart!!.toInt())
        val len = intString.length
        for (i in 0 until len) {
            val current = intString[i]
            if (current == '拾') {
                result.add("ten")
            } else if (current == '佰') {
                result.add("hundred")
            } else if (current == '仟') {
                result.add("thousand")
            } else if (current == '万') {
                result.add("ten_thousand")
            } else if (current == '亿') {
                result.add("ten_million")
            } else {
                result.add(current.toString())
            }
        }
        return result
    }

    companion object {
        private const val DOT = "."
        fun getDefaultTemplate(money: String?): List<String?> {
            return VoiceTemplate()
                    .prefix("success")
                    .numString(money)
                    .suffix("yuan")
                    .gen()
        }

        private val NUM = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        private val CHINESE_UNIT = charArrayOf('元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟')

        /**
         * 返回关于钱的中文式大写数字,支仅持到亿
         */
        fun readInt(moneyNum: Int): String {
            var moneyNum = moneyNum
            var res = ""
            var i = 0
            if (moneyNum == 0) {
                return "0"
            }
            if (moneyNum == 10) {
                return "拾"
            }
            if (moneyNum in 11..19) {
                return "拾" + moneyNum % 10
            }
            while (moneyNum > 0) {
                res = CHINESE_UNIT[i++].toString() + res
                res = NUM[moneyNum % 10].toString() + res
                moneyNum /= 10
            }
            return res.replace("0[拾佰仟]".toRegex(), "0")
                    .replace("0+亿".toRegex(), "亿").replace("0+万".toRegex(), "万")
                    .replace("0+元".toRegex(), "元").replace("0+".toRegex(), "0")
                    .replace("元", "")
        }
    }
}