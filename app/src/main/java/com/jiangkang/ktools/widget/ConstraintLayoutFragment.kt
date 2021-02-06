package com.jiangkang.ktools.widget


import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jiangkang.ktools.R
import com.jiangkang.ktools.databinding.FragmentConstraintLayoutBinding


/**
 * 约束布局相关的Demo
 */
class ConstraintLayoutFragment : Fragment() {

    private var _binding:FragmentConstraintLayoutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentConstraintLayoutBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAnimationOperations()
    }

    private fun addAnimationOperations() {
        var set = false
        val constraint1 = ConstraintSet()
        constraint1.clone(context,R.layout.fragment_constraint_layout)
        val constraint2 = ConstraintSet()
        constraint2.clone(context, R.layout.activity_constraint_layout_img_alt)

        binding.ivConstraint.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.constraintRoot)
            val constraint = if (set) constraint1 else constraint2
            constraint.applyTo(binding.constraintRoot)
            set = !set
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
