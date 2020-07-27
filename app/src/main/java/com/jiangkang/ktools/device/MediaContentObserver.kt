package com.jiangkang.ktools.device

import android.content.Context
import android.database.ContentObserver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import com.jiangkang.tools.widget.KDialog

/**
 * Created by jiangkang on 2017/11/3.
 * description：图片内容变化监听
 */
class MediaContentObserver(private val mContext: Context, handler: Handler?, private val mContentUri: Uri) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        handleChange(mContentUri)
    }

    private fun handleChange(uri: Uri) {
        val resolver = mContext.contentResolver
        val cursor = resolver.query(
                uri,
                MEDIA_PROJECTIONS,
                null,
                null,
                MEDIA_SORT_ORDER)
        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "handleChange: cusor count = " + cursor.count)
            val dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val dataTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
            val data = cursor.getString(dataIndex)
            val dataTaken = cursor.getLong(dataTakenIndex)
            handleTheLatestRowData(data, dataTaken)
        }
    }

    private fun handleTheLatestRowData(data: String, dataTaken: Long) {
        Log.d(TAG, "handleTheLatestRowData: \ndata = $data\ndataToken = $dataTaken")
        if (isScreenShot(data, dataTaken)) {
            Log.d(TAG, "handleTheLatestRowData: data = $data\ntime = $dataTaken")
            val bitmap = BitmapFactory.decodeFile(data)
            KDialog.showImgInDialog(mContext, bitmap)
        } else {
            //not
        }
    }

    private fun isScreenShot(data: String, dataTaken: Long): Boolean {
        var data = data
        data = data.toLowerCase()
        for (keyword in FILENAME_FILTER) {
            if (data.contains(keyword)) {
                return true
            }
        }
        return false
    }

    companion object {
        private val FILENAME_FILTER = arrayOf(
                "screenshot", "screen_shot", "screen-shot", "screen shot",
                "screencapture", "screen_capture", "screen-capture", "screen capture",
                "screencap", "screen_cap", "screen-cap", "screen cap"
        )
        private val MEDIA_PROJECTIONS = arrayOf(
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN)
        private const val MEDIA_SORT_ORDER = MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
        private const val TAG = "MediaContentObserver"
    }

}