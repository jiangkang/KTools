package com.jiangkang.tools.widget

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.JsResult
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import java.util.*

/**
 * Created by jiangkang on 2017/9/13.
 */
object KDialog {
    @JvmStatic
    fun showImgInDialog(context: Context?, bitmap: Bitmap?) {
        val imageView = ImageView(context)
        imageView.setImageBitmap(bitmap)
        AlertDialog.Builder(context!!)
                .setView(imageView)
                .setNegativeButton("关闭") { dialog, which -> dialog.dismiss() }
                .show()
    }
    @JvmStatic
    fun showMsgDialog(context: Context?, content: String?) {
        Handler(Looper.getMainLooper())
                .post {
                    AlertDialog.Builder(context!!)
                            .setMessage(content)
                            .setNegativeButton("关闭") { dialog, which -> dialog.dismiss() }
                            .show()
                }
    }

    @JvmStatic
    fun showJsAlertDialog(context: Context?, content: String?, result: JsResult) {
        Handler(Looper.getMainLooper())
                .post {
                    AlertDialog.Builder(context!!)
                            .setMessage(content)
                            .setNegativeButton("关闭") { dialog, which ->
                                result.confirm()
                                dialog.dismiss()
                            }
                            .show()
                }
    }

    @JvmStatic
    fun showCustomViewDialog(context: Context?, title: String?, view: View?,
                             positiveListener: DialogInterface.OnClickListener?, negativeListener: DialogInterface.OnClickListener?) {
        Handler(Looper.getMainLooper())
                .post {
                    AlertDialog.Builder(context!!)
                            .setTitle(title)
                            .setView(view)
                            .setPositiveButton("确认", positiveListener)
                            .setNegativeButton("取消", negativeListener)
                            .show()
                }
    }

    @JvmStatic
    fun showSingleChoiceDialog(context: Context?, title: String?, items: Array<String>, callback: SingleSelectedCallback?) {
        val selectedIndex = IntArray(1)
        AlertDialog.Builder(context!!)
                .setTitle(title)
                .setSingleChoiceItems(items, 0) { dialog, which -> selectedIndex[0] = which }
                .setPositiveButton("确定") { dialog, which ->
                    callback?.singleSelected(selectedIndex[0])
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                .setCancelable(false)
                .show()
    }

    @JvmStatic
    fun showMultiChoicesDialog(context: Context?, title: String?, items: Array<CharSequence?>, callback: MultiSelectedCallback?) {
        val selectedItems: IntArray
        val selected = BooleanArray(items.size)
        AlertDialog.Builder(context!!)
                .setTitle(title)
                .setMultiChoiceItems(items, BooleanArray(items.size)) { dialog, which, isChecked -> selected[which] = isChecked }
                .setPositiveButton("确定") { dialog, which ->
                    val size = selected.size
                    val selectedList: MutableList<Int> = ArrayList()
                    if (callback != null) {
                        for (i in 0 until size) {
                            if (selected[i]) {
                                selectedList.add(i)
                            }
                        }
                        if (selectedList != null && selectedList.size > 0) {
                            callback.multiSelected(selectedList)
                        } else {
                            callback.selectedNothing()
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                .setCancelable(false)
                .show()
    }

    private var progressDialog: ProgressDialog? = null

    @JvmStatic
    fun showProgressDialog(context: Context?, progress: Int) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
        }
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.max = 100
        progressDialog!!.progress = progress
        progressDialog!!.show()
        if (progress >= 100) {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }
    }

    interface SingleSelectedCallback {
        fun singleSelected(index: Int)
    }

    interface MultiSelectedCallback {
        fun multiSelected(list: List<Int>?)
        fun selectedNothing()
    }
}