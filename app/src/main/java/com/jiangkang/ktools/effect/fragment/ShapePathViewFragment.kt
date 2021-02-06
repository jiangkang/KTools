package com.jiangkang.ktools.effect.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jiangkang.ktools.databinding.FragmentShapePathViewBinding
import com.jiangkang.tools.extend.addOnChangeListener

/**
 * 利用Path 绘图
 */
class ShapePathViewFragment : Fragment() {

    private var _binding:FragmentShapePathViewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShapePathViewBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seekBarSides.addOnChangeListener(
                onProgressChanged = { _, progress, _ ->
                    binding.shapePathView.post {
                        binding.shapePathView.updateSides(sides = progress)
                    }
                }
        )

        binding.seekBarProgress.addOnChangeListener(
                onProgressChanged = { _, progress, _ ->
                    binding.shapePathView.post {
                        binding.shapePathView.updateProgress(progress = progress.toFloat())
                    }
                }
        )

    }


}
