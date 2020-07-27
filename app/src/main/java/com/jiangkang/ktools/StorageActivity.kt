package com.jiangkang.ktools

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.jiangkang.storage.sqlite.LoginDbActivity
import com.jiangkang.tools.utils.SpUtils
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KDialog
import kotlinx.android.synthetic.main.activity_storage.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class StorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)
        title = "Storage"
        handleClick()
    }

    private fun handleClick() {
        btn_set_pref.onClick {
            onBtnSetPrefClicked()
        }

        btn_get_pref.onClick {
            onBtnGetPrefClicked()
        }


        btn_sqlite.onClick {
            startActivity<LoginDbActivity>()
        }

    }

    fun onBtnSetPrefClicked() {

        val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.layout_dialog_key_value, null)

        KDialog.showCustomViewDialog(this, "将值存入SharedPreference中", dialogView, DialogInterface.OnClickListener { dialog, _ ->
            val etKey = dialogView.findViewById<View>(R.id.et_key) as EditText
            val etValue = dialogView.findViewById<View>(R.id.et_value) as EditText

            val key = etKey.text.toString()
            val value = etValue.text.toString()

            if (TextUtils.isEmpty(key)) {
                ToastUtils.showShortToast("key值不能为空")
                return@OnClickListener
            }

            SpUtils.getInstance(this@StorageActivity, "storage")
                    .putString(key, value)

            ToastUtils.showShortToast("存储成功")

            dialog.dismiss()
        }, DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })

        SpUtils.getInstance(this, "storage")
                .putString("author", "姜康")
    }

    fun onBtnGetPrefClicked() {

        val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.layout_dialog_key_value, null)
        val etKey = dialogView.findViewById<View>(R.id.et_key) as EditText
        val etValue = dialogView.findViewById<View>(R.id.et_value) as EditText
        val tvValue = dialogView.findViewById<View>(R.id.tv_value) as TextView
        tvValue.visibility = View.GONE
        etValue.visibility = View.GONE

        KDialog.showCustomViewDialog(this, "从SharedPreference中取值", dialogView, DialogInterface.OnClickListener { dialog, _ ->
            val key = etKey.text.toString()

            if (TextUtils.isEmpty(key)) {
                ToastUtils.showShortToast("key值不符合规范")
                return@OnClickListener
            }

            val value = SpUtils.getInstance(this@StorageActivity, "storage")
                    .getString(key, "null")

            ToastUtils.showToast(value, 9000)
            dialog.dismiss()
        }, DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })

    }

}
