package com.jiangkang.container.fragment

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_LAYOUT_ID = "layout_id"
private const val ARG_TITLE = "title"

open class ContainerFragment : androidx.fragment.app.Fragment() {

    private var mLayoutId: Int = -1

    private var mContentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            mLayoutId = getInt(ARG_LAYOUT_ID)
            getString(ARG_TITLE)?.let {
                activity?.title = it
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mLayoutId != -1) {
            mContentView = layoutInflater.inflate(mLayoutId, container, false)
        }
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinder?.bindView(view)
    }

    companion object {

        @JvmStatic
        fun newInstance(@LayoutRes layoutId: Int, title: String? = null) =
                ContainerFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_LAYOUT_ID, layoutId)
                        putString(ARG_TITLE, title)
                    }
                }
    }

    var viewDataBinder: ViewDataBinder? = null

}