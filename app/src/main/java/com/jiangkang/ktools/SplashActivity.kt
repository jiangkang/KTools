package com.jiangkang.ktools

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jiangkang.tools.extend.startActivity

class SplashActivity : AppCompatActivity() {

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
                this@SplashActivity.startActivity<MainActivity>()
                finish()
            }
        }
        timer.start()
    }

    private fun initViewPager() {
        val data = listOf<Int>(
                R.drawable.wallpaper1,
                R.drawable.wallpaper2,
                R.drawable.wallpaper3,
                R.drawable.wallpaper4
        )
        findViewById<ViewPager2>(R.id.viewpager_splash).adapter = object : RecyclerView.Adapter<SplashViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplashViewHolder{
                val inflater = LayoutInflater.from(this@SplashActivity)
               return SplashViewHolder(inflater.inflate(R.layout.item_splash_image, parent,false))
            }

            override fun getItemCount(): Int  = data.size

            override fun onBindViewHolder(holder: SplashViewHolder, position: Int) {
                   holder.ivSplash.setImageResource(data[position])
            }
        }
    }
}

class SplashViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
    val ivSplash: ImageView = itemView.findViewById<ImageView>(R.id.iv_splash)
}