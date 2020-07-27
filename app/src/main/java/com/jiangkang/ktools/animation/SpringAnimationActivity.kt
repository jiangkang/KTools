package com.jiangkang.ktools.animation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.annotation.IdRes
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.jiangkang.ktools.R
import kotlin.LazyThreadSafetyMode.NONE

private const val MAX_STIFFNESS = SpringForce.STIFFNESS_HIGH
private const val MIN_STIFFNESS = 1
private const val MAX_DAMPING_RATIO = 1
private const val MIN_DAMPING_RATIO = 0


class SpringAnimationActivity : AppCompatActivity() {

    private val dragView by bind<View>(R.id.drag)
    private val firstView by bind<View>(R.id.first)
    private val secondView by bind<View>(R.id.second)
    private val stiffnessBar by bind<SeekBar>(R.id.stiffness_bar)
    private val stiffnessValue by bind<TextView>(R.id.stiffness_value)
    private val dampingRatioBar by bind<SeekBar>(R.id.damping_bar)
    private val dampingRatioValue by bind<TextView>(R.id.damping_value)

    private val firstXAnim by lazy(NONE) { createSpringAnimation(firstView, DynamicAnimation.X) }
    private val firstYAnim by lazy(NONE) { createSpringAnimation(firstView, DynamicAnimation.Y) }
    private val secondXAnim by lazy(NONE) { createSpringAnimation(secondView, DynamicAnimation.X) }
    private val secondYAnim by lazy(NONE) { createSpringAnimation(secondView, DynamicAnimation.Y) }

    private var dX = 0f
    private var dY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_spring_animation)

        setupAnimation()

        stiffnessBar.max = (MAX_STIFFNESS - MIN_STIFFNESS).toInt()
        stiffnessBar.onProgressChange { stiffness ->
            val actualStiffness = stiffness + MIN_STIFFNESS
            stiffnessValue.text = actualStiffness.toString()
            setStiffness(actualStiffness.toFloat())
        }

        // SeekBar only allows integer progress values.
        // So since the damping ratio values are between 0 and 1, we have to set the SeekBar value range
        // to 0-100 instead to allow 2 decimal point values
        dampingRatioBar.max = (MAX_DAMPING_RATIO - MIN_DAMPING_RATIO) * 100
        dampingRatioBar.onProgressChange { dampingRatio ->
            val actualDampingRatio = dampingRatio / 100f
            dampingRatioValue.text = actualDampingRatio.toString()
            setDampingRatio(actualDampingRatio)
        }

        // Set initial stiffness and damping ratio values
        stiffnessBar.progress = SpringForce.STIFFNESS_LOW.toInt() - MIN_STIFFNESS
        dampingRatioBar.progress = (SpringForce.DAMPING_RATIO_LOW_BOUNCY * 100).toInt()
    }

    private fun setupAnimation() {
        val firstLayoutParams = firstView.layoutParams as ViewGroup.MarginLayoutParams
        val secondLayoutParams = secondView.layoutParams as ViewGroup.MarginLayoutParams

        firstXAnim.onUpdate { value ->
            secondXAnim.animateToFinalPosition(value + ((firstView.width -
                    secondView.width) / 2))
        }
        firstYAnim.onUpdate { value ->
            secondYAnim.animateToFinalPosition(value + firstView.height +
                    secondLayoutParams.topMargin)
        }

        dragView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY

                    view.animate().x(newX).y(newY).setDuration(0).start()
                    firstXAnim.animateToFinalPosition(newX + ((dragView.width -
                            firstView.width) / 2))
                    firstYAnim.animateToFinalPosition(newY + dragView.height +
                            firstLayoutParams.topMargin)
                }
            }

            return@setOnTouchListener true
        }
    }

    private fun <K> createSpringAnimation(obj: K, property: FloatPropertyCompat<K>): SpringAnimation {
        return SpringAnimation(obj, property).setSpring(SpringForce())
    }

    private inline fun SpringAnimation.onUpdate(crossinline onUpdate: (value: Float) -> Unit): SpringAnimation {
        return this.addUpdateListener { _, value, _ ->
            onUpdate(value)
        }
    }

    private fun setStiffness(stiffness: Float) {
        firstXAnim.spring.stiffness = stiffness
        firstYAnim.spring.stiffness = stiffness
        secondXAnim.spring.stiffness = stiffness
        secondYAnim.spring.stiffness = stiffness
    }

    private fun setDampingRatio(dampingRatio: Float) {
        firstXAnim.spring.dampingRatio = dampingRatio
        firstYAnim.spring.dampingRatio = dampingRatio
        secondXAnim.spring.dampingRatio = dampingRatio
        secondYAnim.spring.dampingRatio = dampingRatio
    }

    private fun SeekBar.onProgressChange(onProgressChange: (progress: Int) -> Unit) {
        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                onProgressChange(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun <T : View> Activity.bind(@IdRes idRes: Int) = lazy(NONE) { findViewById<T>(idRes) }

}
