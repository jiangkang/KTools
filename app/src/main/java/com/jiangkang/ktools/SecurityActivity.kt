package com.jiangkang.ktools

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivitySecurityBinding

class SecurityActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySecurityBinding
    private val etOriginal: EditText by lazy { findViewById<EditText>(R.id.et_original) }
    private val tvResult: EditText by lazy { findViewById<EditText>(R.id.et_result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Security"

        binding.btnBase64Encode.setOnClickListener {
            onBtnBase64EncodeClicked()
        }

        binding.btnBase64Decode.setOnClickListener {
            onBtnBase64DecodeClicked()
        }
    }

    private fun onBtnBase64EncodeClicked() {
        val inputString = etOriginal.text.toString()
        val result = Base64.encodeToString(inputString.toByteArray(), Base64.DEFAULT)
        tvResult.setText(result)
    }

    private fun onBtnBase64DecodeClicked() {
        val inputString = etOriginal.text.toString()
        val resultByte = Base64.decode(inputString, Base64.DEFAULT)
        val result = String(resultByte)
        tvResult!!.setText(result)
    }

    companion object {
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, SecurityActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}