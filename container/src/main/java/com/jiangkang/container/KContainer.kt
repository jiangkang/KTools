package com.jiangkang.container

import android.R
import androidx.fragment.app.FragmentActivity
import com.jiangkang.container.fragment.ContainerFragment
import com.jiangkang.container.fragment.ViewDataBinder

fun androidx.fragment.app.FragmentActivity.loadFragment(layoutId: Int, title: String? = null, viewBinder: ViewDataBinder? = null) {
    val fragment = ContainerFragment.newInstance(layoutId, title).apply {
        viewDataBinder = viewBinder
    }
    supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, fragment)
            .commitAllowingStateLoss()
}
