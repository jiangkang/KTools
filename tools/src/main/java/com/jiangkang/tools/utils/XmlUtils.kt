package com.jiangkang.tools.utils

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*

object XmlUtils {

    private const val tag = "XmlUtils"

    fun parseXmlFile(file: File): String {
        return parseXmlInner(FileReader(file))
    }

    fun parseXmlStream(stream: InputStream): String {
        return parseXmlInner(InputStreamReader(stream))
    }

    private fun parseXmlInner(reader: Reader): String {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(reader)

        val result = StringBuilder()
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> {
                    result.append("Start document\n")
                    Log.d(tag, "Start document")
                }
                XmlPullParser.START_TAG -> {
                    result.append("Start tag : ${parser.name} props: ${(0 until parser.attributeCount).map { "${parser.getAttributeValue(it)} : ${parser.getAttributeValue(it)}" }}\n")
                    Log.d(tag, "Start tag : ${parser.name} props: ${(0 until parser.attributeCount).map { "${parser.getAttributeValue(it)} : ${parser.getAttributeValue(it)}" }}")
                }
                XmlPullParser.TEXT -> {
                    result.append("Text : ${parser.text}\n")
                    Log.d(tag, "Text : ${parser.text}")
                }
                XmlPullParser.END_TAG -> {
                    result.append("End tag : ${parser.name}\n")
                    Log.d(tag, "End tag : ${parser.name}")
                }
                else -> {
                }
            }
            eventType = parser.next()
        }
        result.append("End document\n")
        Log.d(tag, "End document")
        return result.toString()
    }

}