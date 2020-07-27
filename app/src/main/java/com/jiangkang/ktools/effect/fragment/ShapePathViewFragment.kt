package com.jiangkang.ktools.effect.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jiangkang.ktools.R
import com.jiangkang.tools.extend.addOnChangeListener
import kotlinx.android.synthetic.main.fragment_shape_path_view.*


/**
 * 利用Path 绘图
 */
class ShapePathViewFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shape_path_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekBarSides.addOnChangeListener(
                onProgressChanged = { _, progress, _ ->
                    shapePathView.post {
                        shapePathView.updateSides(sides = progress)
                    }
                }
        )

        seekBarProgress.addOnChangeListener(
                onProgressChanged = { _, progress, _ ->
                    shapePathView.post {
                        shapePathView.updateProgress(progress = progress.toFloat())
                    }
                }
        )

    }


}
