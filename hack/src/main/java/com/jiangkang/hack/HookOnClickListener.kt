package com.jiangkang.hack

import android.content.Context
import android.util.Log
import android.view.View
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KDialog

/**
 * Created by jiangkang on 2017/11/28.
 * description：
 */
class HookOnClickListener(private val originListener: View.OnClickListener?, private val context: Context) : View.OnClickListener {
    override fun onClick(v: View) {
        //点击之前
        Log.d(TAG, "onClick: before")

        ToastUtils.showShortToast("点击之前")
        // 执行原始的点击逻辑
        originListener?.onClick(v)
        
        //点击之后
        Log.d(TAG, "onClick: after")

        ToastUtils.showShortToast("点击之后")
    }

    companion object {
        private const val TAG = "hook"
    }

}