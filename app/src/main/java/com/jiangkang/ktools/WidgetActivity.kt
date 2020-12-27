package com.jiangkang.ktools

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.transition.Fade
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivityWidgetBinding
import com.jiangkang.ktools.widget.*
import com.jiangkang.tools.extend.launch
import com.jiangkang.tools.extend.startActivity
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.FloatingWindow
import com.jiangkang.tools.widget.KNotification
import com.jiangkang.widget.ui.TouchLogicActivity

class WidgetActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWidgetBinding
    private val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWidgetBinding.inflate(layoutInflater)
        //转场动画
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Fade()
        window.exitTransition = Fade()
        setContentView(binding.root)
        handleOnClick()
    }

    private fun handleOnClick() {
        binding.btnCoordinatorLayout.setOnClickListener {
            CoordinatorActivity.launch(mContext, null)
        }

        binding.btnShowFloatingWindow.setOnClickListener {
            if (!Settings.canDrawOverlays(this@WidgetActivity)) {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            } else {
                FloatingWindow.show(mContext, "来来来，看这里\n这是一个悬浮框")
            }
        }

        binding.btnDismissFloatingWindow.setOnClickListener {
            FloatingWindow.dismiss()
        }

        binding.btnSetToastShowTime.setOnClickListener {
            ToastUtils.showToast("测试自定义显示时间Toast:20s", 1000 * 20)
        }

        binding.btnCreateSimpleNotification.setOnClickListener {
            KNotification.createNotification(mContext, R.mipmap.ic_launcher, "测试标题", "测试内容", Intent(mContext, MainActivity::class.java))
        }

        binding.btnShowCustomNotification.setOnClickListener {
            KNotification.createNotification(mContext, R.mipmap.ic_launcher, BitmapFactory.decodeResource(resources,R.drawable.demo),
                    Intent(mContext, MainActivity::class.java))
        }

        binding.btnShowBubble.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                KNotification.createBubble<BubbleActivity>(this@WidgetActivity)
            } else {
                ToastUtils.showShortToast("Android 11及以上系统才可以")
            }
        }

        binding.btnOpenNotificationSettings.setOnClickListener {
            KNotification.openSettingsPage(this@WidgetActivity)
        }

        binding.btnWidgetDialog.setOnClickListener {
            launch(KDialogActivity::class.java, null)
        }

        binding.btnFloatingActionButton.setOnClickListener {
            FabActivity.launch(mContext, null)
        }

        binding.btnScroll.setOnClickListener {
            ScrollingActivity.launch(mContext, null)
        }

        binding.btnBottomNav.setOnClickListener {
            BottomNavigationActivity.launch(mContext, null)
        }

        binding.btnConstraintLayout.setOnClickListener {
            ConstraintLayoutActivity.launch(mContext, null)
        }

        binding.btnTouchLogic.setOnClickListener {
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
