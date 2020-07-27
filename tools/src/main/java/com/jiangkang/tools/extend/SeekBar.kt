package com.jiangkang.tools.extend

import android.widget.SeekBar

/**
 * Created by jiangkang on 2018/2/8.
 * description：
 */

fun SeekBar.addOnChangeListener(
        onStartTrackingTouch: ((SeekBar?) -> Unit)? = null,
        onStopTrackingTouch: ((SeekBar?) -> Unit)? = null,
        onProgressChanged: ((SeekBar?, Int, Boolean) -> Unit)? = null
): SeekBar.OnSeekBarChangeListener {

    val listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onProgressChanged?.invoke(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            onStartTrackingTouch?.invoke(seekBar)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            onStopTrackingTouch?.invoke(seekBar)
        }
    }
    setOnSeekBarChangeListener(listener)
    return listener
}

