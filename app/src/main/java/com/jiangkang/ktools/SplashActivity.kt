package com.jiangkang.ktools

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.jiangkang.ktools.MainActivity.Companion.launch
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var item0: View
    private lateinit var item1: View
    private lateinit var item2: View

    private lateinit var views: ArrayList<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initViewPager()
        setupTimer()
    }

    private fun setupTimer() {
        val timer: CountDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                launch(this@SplashActivity)
                finish()
            }
        }
        timer.start()
    }

    private fun initViewPager() {
        val inflater = LayoutInflater.from(this)
        item0 = inflater.inflate(R.layout.item_splash_image, null)
        item1 = inflater.inflate(R.layout.item_splash_image, null)
        item2 = inflater.inflate(R.layout.item_splash_image, null)
        views = ArrayList()
        views.add(item0)
        views.add(item1)
        views.add(item2)
        val adapter: PagerAdapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return views.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(views[position])
                return views[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(views[position])
            }
        }
        viewpager_splash.adapter = adapter
    }
}