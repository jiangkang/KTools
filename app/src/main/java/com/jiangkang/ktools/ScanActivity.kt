package com.jiangkang.ktools

import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.tools.utils.QRCodeUtils
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KDialog
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {

    private val etUrl: EditText by lazy { findViewById<EditText>(R.id.et_url) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        btn_gen_qr_code.setOnClickListener {
            onBtnGenQrCodeClicked()
        }
    }

    fun onBtnGenQrCodeClicked() {
        if (!TextUtils.isEmpty(etUrl.text.toString())) {
            val bitmap = QRCodeUtils.createQRCodeBitmap(
                    etUrl!!.text.toString(),
                    640,
                    640
            )
            if (bitmap != null) {
                KDialog.showImgInDialog(this, bitmap)
            }
        } else {
            ToastUtils.showShortToast("请在输入框输入url")
        }
    }
}