package com.jiangkang.ktools.effect.fragment

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Path
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.BounceInterpolator
import android.widget.*
import androidx.fragment.app.Fragment
import com.jiangkang.container.fragment.ViewDataBinder
import com.jiangkang.container.loadFragment
import com.jiangkang.ktools.R
import com.jiangkang.ktools.animation.CardFlipActivity
import com.jiangkang.ktools.animation.SpringAnimationActivity
import com.jiangkang.tools.extend.findViewById
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.utils.screenHeight
import com.jiangkang.tools.utils.screenSize
import com.jiangkang.tools.utils.screenWidth
import com.jiangkang.tools.widget.KDialog
import com.jiangkang.widget.view.TaiChiView


val colors = arrayOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.CYAN,
        Color.DKGRAY,
        Color.GRAY,
        Color.LTGRAY,
        Color.MAGENTA,
        Color.YELLOW
)

/**
 * 动效相关Demo
 */
class EffectFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_effect, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewClicked()
        handleCrossFadeView()
        handleFlipCard()
        handleSpringAnimation()
        handleCircleRevealAnimation()
        handleCurvedMotion()
    }

    /**
     * 通过PathInterpolator实现曲线运动
     */
    private fun handleCurvedMotion() {
        activity?.findViewById<Button>(R.id.btnCurvedMotionAnim)?.setOnClickListener {
            activity?.loadFragment(
                    R.layout.fragment_value_animator,
                    "Curved Motion Animation",
                    object : ViewDataBinder {
                        override fun bindView(view: View) {
                            val ivDog = view.findViewById<ImageView>(R.id.iv_dog)

                            val path = Path().apply {
                                arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
                            }
                            val animator = ObjectAnimator.ofFloat(ivDog, View.X, View.Y, path).apply {
                                duration = 5000
                                repeatMode = ObjectAnimator.REVERSE
                                start()
                            }

                        }

                    }
            )
        }
    }


    /**
     * 圆形显示动画
     */
    private fun handleCircleRevealAnimation() {
        activity?.findViewById<Button>(R.id.btnCircleRevealAnim)?.setOnClickListener {
            activity?.loadFragment(
                    R.layout.fragment_value_animator,
                    "Circle Reveal Animation",
                    object : ViewDataBinder {
                        override fun bindView(view: View) {
                            val ivDog = view.findViewById<ImageView>(R.id.iv_dog)

                            val cx = ivDog.width / 2
                            val cy = ivDog.height / 2
                            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
                            val anim = ViewAnimationUtils.createCircularReveal(ivDog, cx, cy, 0f, finalRadius)
                            ivDog.visibility = View.VISIBLE
                            anim.duration = 5000
                            ivDog.postDelayed({ anim.start() }, 3000)

                            ivDog.setOnClickListener {
                                val cx = ivDog.width / 2
                                val cy = ivDog.height / 2
                                val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
                                val anim = ViewAnimationUtils.createCircularReveal(ivDog, cx, cy, finalRadius, 0f)
                                anim.duration = 5000
                                anim.addListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator?) {
                                        ivDog.visibility = View.INVISIBLE
                                        ToastUtils.showShortToast("小狗不见了！")
                                    }
                                })
                                anim.start()
                            }
                        }

                    }
            )
        }
    }

    /**
     * Spring Animation
     */
    private fun handleSpringAnimation() {
        findViewById<Button>(R.id.btnSpringAnim)?.setOnClickListener {
            activity?.startActivity(Intent(activity, SpringAnimationActivity::class.java))
        }
    }

    private fun handleFlipCard() {
        findViewById<Button>(R.id.btnCardFlip)?.setOnClickListener {
            activity?.startActivity(Intent(activity, CardFlipActivity::class.java))
        }
    }

    //实际上就是淡入淡出效果，调整透明度动画
    private fun handleCrossFadeView() {
        findViewById<Button>(R.id.btnCrossFadeAnimator)?.setOnClickListener {
            activity?.loadFragment(
                    R.layout.layout_crossfade,
                    "CrossFade Animator",
                    object : ViewDataBinder {
                        override fun bindView(view: View) {
                            val contentView = view.findViewById<ScrollView>(R.id.content)
                            val loadingView = view.findViewById<ProgressBar>(R.id.loading_spinner)
                            var animDuration = 2000
                            contentView.visibility = View.GONE

                            contentView.apply {
                                alpha = 0f
                                visibility = View.VISIBLE
                                animate()
                                        .alpha(1.0f)
                                        .setDuration(animDuration.toLong())
                                        .setListener(null)
                            }

                            loadingView.animate()
                                    .alpha(0f)
                                    .setDuration(animDuration.toLong())
                                    .setListener(object : AnimatorListenerAdapter() {
                                        override fun onAnimationEnd(animation: Animator) {
                                            loadingView.visibility = View.GONE
                                        }
                                    })
                        }
                    }
            )
        }

    }

    private fun handleViewClicked() {

        findViewById<Button>(R.id.effect_shape)?.setOnClickListener {
            handleClick(ShapeViewFragment())
        }

        findViewById<Button>(R.id.tai_chi)?.setOnClickListener {
            val taiChiView = TaiChiView(this@EffectFragment.activity)
            KDialog.showCustomViewDialog(this@EffectFragment.activity, "太极图", taiChiView
                    , DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() },DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() })
            taiChiView.startRotate()
        }

        findViewById<Button>(R.id.animated_shape_view)?.setOnClickListener {
            handleClick(AnimatedShapeViewFragment())
        }

        findViewById<Button>(R.id.shape_path_view)?.setOnClickListener {
            handleClick(ShapePathViewFragment())
            val (x, y) = mContext.screenSize
        }

        //自动布局动画
        findViewById<Button>(R.id.btnAutoLayoutAnimation)?.setOnClickListener {
            activity?.loadFragment(
                    R.layout.fragment_auto_animation_layout,
                    "Auto Animation Layout Updates",
                    object : ViewDataBinder {
                        @SuppressLint("ObjectAnimatorBinding")
                        override fun bindView(view: View) {
                            if (view is ViewGroup) {
                                view.layoutTransition = LayoutTransition().apply {
                                    val screenWidth = mContext.screenWidth.toFloat()
                                    setAnimator(
                                            LayoutTransition.APPEARING,
                                            ObjectAnimator.ofFloat(null, "translationX", screenWidth, 0f))
                                    setDuration(LayoutTransition.APPEARING, 1000)

                                    setAnimator(
                                            LayoutTransition.DISAPPEARING,
                                            ObjectAnimator.ofFloat(null, "translationX", 0f, -screenWidth))
                                    setDuration(LayoutTransition.DISAPPEARING, 1000)

                                }
                                val btnAdd = view.findViewById<Button>(R.id.btn_add_view)
                                val btnRemove = view.findViewById<Button>(R.id.btn_remove_view)
                                val layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    topMargin = 6
                                }
                                btnAdd.setOnClickListener {
                                    view.addView(Button(view.context).apply {
                                        text = view.childCount.toString()
                                        gravity = Gravity.CENTER
                                        setBackgroundColor(colors[IntRange(0, colors.size - 1).random()])
                                        setOnClickListener { ToastUtils.showShortToast(this@apply.text.toString()) }
                                    }, layoutParams)
                                }
                                btnRemove.setOnClickListener {
                                    if (view.childCount > 1) {
                                        view.removeViewAt(view.childCount - 1)
                                    } else {
                                        ToastUtils.showShortToast("没有View可以移除咯！")
                                    }
                                }
                            }
                        }

                    }
            )
        }

        //ValueAnimator的动效
        findViewById<Button>(R.id.btnValueAnimator)?.setOnClickListener {
            activity?.loadFragment(
                    R.layout.fragment_value_animator,
                    "Value Animator",
                    object : ViewDataBinder {
                        override fun bindView(view: View) {
                            val ivDog = view.findViewById<ImageView>(R.id.iv_dog)
                            val translationXAnimator = ValueAnimator.ofFloat(0f, mContext.screenWidth.toFloat())
                            val translationYAnimator = ValueAnimator.ofFloat(0f, mContext.screenHeight.toFloat())
                            translationXAnimator.addUpdateListener { value ->
                                ivDog.translationX = value.animatedValue as Float
                            }
                            translationYAnimator.addUpdateListener { value ->
                                ivDog.translationY = value.animatedValue as Float
                            }
                            val set = AnimatorSet()
                            set.duration = 10000
                            set.play(translationXAnimator).with(translationYAnimator)
                            set.interpolator = BounceInterpolator()
                            set.start()
                        }
                    }
            )
        }

        findViewById<Button>(R.id.btnVectorDrawable)?.setOnClickListener {
            activity?.loadFragment(
                    R.layout.fragment_vector_drawable,
                    "Vector Drawable",
                    object : ViewDataBinder {
                        override fun bindView(view: View) {
                            val drawable = view.findViewById<ImageView>(R.id.iv_animated_vector_drawable).drawable as AnimatedVectorDrawable
                            drawable.start()
                        }
                    })
        }

    }

    private fun handleClick(fragment: Fragment) {
        val tag = fragment.javaClass.toString()
        this.activity?.apply {
            supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(tag)
                    .replace(android.R.id.content, fragment, tag)
                    .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
