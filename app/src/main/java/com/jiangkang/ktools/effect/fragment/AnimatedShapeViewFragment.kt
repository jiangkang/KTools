package com.jiangkang.ktools.effect.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jiangkang.ktools.databinding.FragmentAnimatedShapeViewBinding


/**
 * View + Animation
 */
class AnimatedShapeViewFragment : Fragment() {

    private var _binding: FragmentAnimatedShapeViewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentAnimatedShapeViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.animatedShapeView.post { binding.animatedShapeView.startRotate() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
