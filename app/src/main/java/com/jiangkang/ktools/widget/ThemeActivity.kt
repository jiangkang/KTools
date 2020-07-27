package com.jiangkang.ktools.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jiangkang.ktools.R
import com.jiangkang.ktools.widget.TextAdapter.onItemClickListener
import com.jiangkang.tools.utils.ToastUtils

class ThemeActivity : AppCompatActivity() {

    private val mRcTheme: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rc_theme) }

    private var mAdapter: TextAdapter? = null
    private var mThemeList: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        initViews()
        loadData()
        bindDataToView()
    }

    private fun bindDataToView() {
        mAdapter = TextAdapter(this, mThemeList)
        mAdapter!!.setOnItemClickListener(object : onItemClickListener {
            override fun onClick(holder: TextAdapter.ViewHolder?, position: Int) {
                ToastUtils.showShortToast(position.toString())
            }
        })
        mRcTheme.adapter = mAdapter
    }

    private fun loadData() {

    }

    private fun initViews() {
        mRcTheme.layoutManager = LinearLayoutManager(this)
    }
}