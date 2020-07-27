package com.jiangkang.ktools.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.jiangkang.ktools.R
import com.jiangkang.tools.utils.ToastUtils

class SearchViewActivity : AppCompatActivity() {

    private val searchViewDemo: SearchView by lazy {findViewById<SearchView>(R.id.search_view_demo)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_view)
        initSearchView()
    }

    private fun initSearchView() {
        searchViewDemo.isSubmitButtonEnabled = true
        searchViewDemo.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                ToastUtils.showLongToast(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                ToastUtils.showShortToast(newText)
                return false
            }
        })
    }

    companion object {
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, SearchViewActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}