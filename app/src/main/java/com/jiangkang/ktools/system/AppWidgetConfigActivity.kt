package com.jiangkang.ktools.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.ktools.R

/**
 * App Widget 配置界面，用于更新Widget
 * 可以不必添加
 */
class AppWidgetConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_widget_config)
    }
}