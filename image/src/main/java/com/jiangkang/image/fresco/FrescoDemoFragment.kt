package com.jiangkang.image.fresco

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jiangkang.image.R

class FrescoDemoFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = FrescoDemoFragment()
    }

    private lateinit var viewModel: FrescoDemoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fresco_demo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FrescoDemoViewModel::class.java)
    }





}
