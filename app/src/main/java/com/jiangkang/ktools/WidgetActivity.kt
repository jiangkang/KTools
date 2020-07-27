package com.jiangkang.ktools

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.transition.Fade
import android.view.Window
import android.widget.RemoteViews
import com.jiangkang.ktools.widget.*
import com.jiangkang.tools.extend.launch
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.FloatingWindow
import com.jiangkang.tools.widget.KNotification
import kotlinx.android.synthetic.main.activity_widget.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class WidgetActivity : AppCompatActivity() {

    private val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //转场动画
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            window.enterTransition = Fade()
            window.exitTransition = Fade()
        }
        setContentView(R.layout.activity_widget)
        handleOnClick()
    }

    private fun handleOnClick() {

        btn_coordinator_layout.onClick {
            CoordinatorActivity.launch(mContext, null)
        }

        btn_show_floating_window.onClick {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this@WidgetActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
                } else {
                    FloatingWindow.show(mContext, "来来来，看这里\n这是一个悬浮框")
                }
            }
        }


        btn_dismiss_floating_window.onClick {
            FloatingWindow.dismiss()
        }


        btn_set_toast_show_time.onClick {
            ToastUtils.showToast("测试自定义显示时间Toast:20s", 1000 * 20)
        }


        btn_create_simple_notification.onClick {
            KNotification.createNotification(mContext, R.mipmap.ic_launcher, "测试标题", "测试内容", Intent(mContext, MainActivity::class.java))
        }


        btn_show_custom_notification.onClick {
            val views = RemoteViews(mContext.packageName, R.layout.layout_big_notification)
            views.setImageViewResource(R.id.iv_notification_img, R.drawable.demo)
            KNotification.createNotification(mContext, R.mipmap.ic_launcher, views,
                    Intent(mContext, MainActivity::class.java))
        }


        btn_widget_dialog.onClick {
            launch(KDialogActivity::class.java, null)
        }


        btn_floating_action_button.onClick {
            FabActivity.launch(mContext, null)
        }


        btn_scroll.onClick {
            ScrollingActivity.launch(mContext, null)
        }

        btn_bottom_nav.onClick {
            BottomNavigationActivity.launch(mContext, null)
        }

        btn_constraint_layout.onClick {
            ConstraintLayoutActivity.launch(mContext, null)
        }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        }
    }


    companion object {

        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, WidgetActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }

}
