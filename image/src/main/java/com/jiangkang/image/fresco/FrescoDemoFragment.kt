package com.jiangkang.image.fresco

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jiangkang.image.R

class FrescoDemoFragment : Fragment() {

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
        viewModel = ViewModelProvider(this).get(FrescoDemoViewModel::class.java)
    }





}
