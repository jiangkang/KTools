package com.jiangkang.ktools.widget

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jiangkang.ktools.R
import kotlinx.android.synthetic.main.activity_coordinator.*

class CoordinatorActivity : AppCompatActivity() {

    private val mToolbar: Toolbar by lazy {findViewById<Toolbar>(R.id.toolbar)}
    private val mWebView: WebView by lazy { findViewById<WebView>(R.id.web_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)
        initViews()
    }

    private fun initViews() {
        mToolbar.title = "Demo"
        mToolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(mToolbar)
        mWebView.settings.javaScriptEnabled = true
        mWebView.loadUrl("http://www.jianshu.com/u/2c22c64b9aff")
        fab.setOnClickListener {
            mWebView.reload()
        }
    }

    companion object {
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, CoordinatorActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}