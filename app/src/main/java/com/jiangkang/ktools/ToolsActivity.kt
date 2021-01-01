package com.jiangkang.ktools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiangkang.ktools.databinding.ActivityToolsBinding
import com.jiangkang.tools.utils.FileUtils
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.utils.XmlUtils

class ToolsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityToolsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToolsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleLogic()
    }

    private fun handleLogic() {
        binding.btnParseXmlFile.setOnClickListener {
            val assetInputStream = FileUtils.getInputStreamFromAssets("xml/demo.xml")
            ToastUtils.showShortToast(XmlUtils.parseXmlStream(assetInputStream))
        }
    }
}