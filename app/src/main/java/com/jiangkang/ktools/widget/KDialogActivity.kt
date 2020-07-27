package com.jiangkang.ktools.widget

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jiangkang.ktools.R
import com.jiangkang.tools.utils.FileUtils
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KDialog
import com.jiangkang.tools.widget.KDialog.MultiSelectedCallback
import kotlinx.android.synthetic.main.activity_kdialog.*
import java.text.DateFormat
import java.util.*

class KDialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //转场动画
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            window.enterTransition = Fade()
            window.exitTransition = Fade()
        }
        setContentView(R.layout.activity_kdialog)

        btn_create_simple_text_dialog.setOnClickListener {
            onBtnCreateSimpleTextDialogClicked()
        }

        btn_single_choice_dialog.setOnClickListener {
            onBtnSingleChoiceDialogClicked()
        }

        btn_multi_choices_dialog.setOnClickListener {
            onBtnMultiChoicesDialogClicked()
        }

        btn_progress_dialog_simple.setOnClickListener {
            onBtnProgressDialogSimpleClicked()
        }

        btn_progress_dialog_indeterminate.setOnClickListener {
            onBtnProgressDialogIndeterminateClicked()
        }

        btn_input_dialog.setOnClickListener {
            onBtnInputDialogClicked()
        }

        btn_choose_date_dialog.setOnClickListener {
            onBtnChooseDateDialogClicked()
        }

        btn_choose_time_dialog.setOnClickListener {
            onBtnChooseTimeDialogClicked()
        }

        btn_web_view_dialog.setOnClickListener {
            onBtnWebViewDialogClicked()
        }

        btn_bottom_dialog.setOnClickListener {
            onBtnBottomDialogClicked()
        }
    }

    private fun onBtnCreateSimpleTextDialogClicked() {
        AlertDialog.Builder(this)
                .setTitle("AlertDialog")
                .setMessage("AlertDialog.Builder(context)\n     .setMessage('....')\n     .show()")
                .setNegativeButton("关闭") { dialog, which -> dialog.dismiss() }
                .setCancelable(false)
                .show()
    }

    private fun onBtnSingleChoiceDialogClicked() {
        val singleChoiceItems = arrayOf(
                "篮球",
                "足球",
                "乒乓球",
                "羽毛球"
        )
        KDialog.showSingleChoiceDialog(this,
                "你最喜欢哪种运动？",
                singleChoiceItems,object: KDialog.SingleSelectedCallback{
            override fun singleSelected(index: Int) {
                ToastUtils.showShortToast(singleChoiceItems[index])
            }
        }
        )
    }

    private fun onBtnMultiChoicesDialogClicked() {
        val multiChoicesItems = arrayOf(
                "篮球",
                "足球",
                "乒乓球",
                "羽毛球"
        )
    }

    private fun onBtnProgressDialogSimpleClicked() {
        val progress = intArrayOf(0)
        val dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                progress[0] += 10
                dialog.progress = progress[0]
                if (progress[0] >= 100) {
                    ToastUtils.showShortToast("加载完成")
                    timer.cancel()
                    dialog.dismiss()
                }
            }
        }, 0, 500)
    }

    private fun onBtnProgressDialogIndeterminateClicked() {
        val dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setMessage("正在加载中，请稍等....")
        dialog.show()
    }

    private fun onBtnInputDialogClicked() {
        /*
        * 封装之后，会变得非常有趣
        * */
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        val inputView1 = LayoutInflater.from(this).inflate(R.layout.layout_input_dialog, linearLayout, false)
        val inputView2 = LayoutInflater.from(this).inflate(R.layout.layout_input_dialog, linearLayout, false)
        (inputView1.findViewById<View>(R.id.tv_input) as TextView).text = "用户名："
        (inputView1.findViewById<View>(R.id.et_input) as EditText).hint = "请输入用户名"
        (inputView2.findViewById<View>(R.id.tv_input) as TextView).text = "密    码："
        (inputView2.findViewById<View>(R.id.et_input) as EditText).hint = "请输入密码"
        linearLayout.addView(inputView1, 0)
        linearLayout.addView(inputView2, 1)
        AlertDialog.Builder(this)
                .setView(linearLayout)
                .setTitle("登录框")
                .setCancelable(false)
                .setPositiveButton("登录") { dialog, _ ->
                    ToastUtils.showShortToast("这只是一个假登录")
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                .show()
    }

    private fun onBtnChooseDateDialogClicked() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(
                this,
                OnDateSetListener { _, year, month, dayOfMonth ->
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = month
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
                    ToastUtils.showShortToast(date)
                }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        dialog.show()
    }

    private fun onBtnChooseTimeDialogClicked() {
        val calendar = Calendar.getInstance()
        val dialog = TimePickerDialog(
                this,
                OnTimeSetListener { _, hourOfDay, minute ->
                    calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                    calendar[Calendar.MINUTE] = minute
                    val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
                    ToastUtils.showShortToast(time)
                },
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                false
        )
        dialog.show()
    }

    private fun onBtnWebViewDialogClicked() {
        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(FileUtils.getAssetsPath("web/gravity-points/index.html"))
        AlertDialog.Builder(this)
                .setView(webView)
                .setNegativeButton("关闭") { dialog, _ -> dialog.dismiss() }
                .setCancelable(false)
                .show()
    }

    private fun onBtnBottomDialogClicked() {
        val webView = WebView(this)
        webView.loadUrl("https://github.com/jiangkang/KTools")
        webView.settings.javaScriptEnabled = true
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(webView)
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        }
    }

    companion object {
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, KDialogActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}