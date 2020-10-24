package com.jiangkang.ktools

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.transition.Fade
import android.view.Window
import android.widget.Button
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.widget.*
import com.jiangkang.tools.extend.launch
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.FloatingWindow
import com.jiangkang.tools.widget.KNotification
import com.jiangkang.widget.ui.TouchLogicActivity
import kotlinx.android.synthetic.main.activity_widget.*

class WidgetActivity : AppCompatActivity() {

    private val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //转场动画
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Fade()
        window.exitTransition = Fade()
        setContentView(R.layout.activity_widget)
        handleOnClick()
    }

    private fun handleOnClick() {
        btn_coordinator_layout.setOnClickListener {
            CoordinatorActivity.launch(mContext, null)
        }
        
        btn_show_floating_window.setOnClickListener {
            if (!Settings.canDrawOverlays(this@WidgetActivity)) {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            } else {
                FloatingWindow.show(mContext, "来来来，看这里\n这是一个悬浮框")
            }
        }

        btn_dismiss_floating_window.setOnClickListener {
            FloatingWindow.dismiss()
        }

        btn_set_toast_show_time.setOnClickListener {
            ToastUtils.showToast("测试自定义显示时间Toast:20s", 1000 * 20)
        }

        btn_create_simple_notification.setOnClickListener {
            KNotification.createNotification(mContext, R.mipmap.ic_launcher, "测试标题", "测试内容", Intent(mContext, MainActivity::class.java))
        }


        btn_show_custom_notification.setOnClickListener {
            KNotification.createNotification(mContext, R.mipmap.ic_launcher, BitmapFactory.decodeResource(resources,R.drawable.demo),
                    Intent(mContext, MainActivity::class.java))
        }

        btn_show_bubble.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                KNotification.createBubble<BubbleActivity>(this@WidgetActivity)
            } else {
                ToastUtils.showShortToast("Android 11及以上系统才可以")
            }
        }

        btn_open_notification_settings.setOnClickListener {
            KNotification.openSettingsPage(this@WidgetActivity)
        }


        btn_widget_dialog.setOnClickListener {
            launch(KDialogActivity::class.java, null)
        }


        btn_floating_action_button.setOnClickListener {
            FabActivity.launch(mContext, null)
        }

        btn_scroll.setOnClickListener {
            ScrollingActivity.launch(mContext, null)
        }

        btn_bottom_nav.setOnClickListener {
            BottomNavigationActivity.launch(mContext, null)
        }

        btn_constraint_layout.setOnClickListener {
            ConstraintLayoutActivity.launch(mContext, null)
        }

        btn_touch_logic.setOnClickListener {
            startActivity<TouchLogicActivity>()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
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
