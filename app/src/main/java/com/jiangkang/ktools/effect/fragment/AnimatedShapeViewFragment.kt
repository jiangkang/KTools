package com.jiangkang.ktools.effect.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jiangkang.ktools.R
import kotlinx.android.synthetic.main.fragment_animated_shape_view.*


/**
 * View + Animation
 */
class AnimatedShapeViewFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animated_shape_view, container, false)
    }


    override fun onResume() {
        super.onResume()
        animatedShapeView.post { animatedShapeView.startRotate() }
    }

}
