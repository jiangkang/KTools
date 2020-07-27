package com.jiangkang.tools.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jiangkang.tools.R

/**
 * A simple [Fragment] subclass.
 */
class KDialogFragment : DialogFragment() {
    private val receivedBundle: Bundle? = null
    private val contentViewId = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return if (contentViewId != -1) {
            inflater.inflate(contentViewId, container, false)
        } else {
            inflater.inflate(R.layout.fragment_kdialog, container, false)
        }
    }
}