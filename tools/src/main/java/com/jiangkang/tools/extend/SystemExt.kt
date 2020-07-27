package com.jiangkang.tools.extend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by jiangkang on 2018/1/7.
 * description：系统组件功能拓展
 */

fun ImageView.loadUrl(url: String) {
    Glide.with(this)
            .load(url)
            .into(this)
}

fun AppCompatActivity.launch(target: Class<*>, data: Bundle?) {
    var intent = Intent(this, target)
    if (data != null){
        intent.putExtras(data)
    }
    startActivity(intent)
}

fun Activity.launch(target: Class<*>, data: Bundle?) {
    var intent = Intent(this, target)
    if (data != null){
        intent.putExtras(data)
    }
    startActivity(intent)
}




