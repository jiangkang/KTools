package com.jiangkang.kanimation.generic

import android.animation.Animator
import android.transition.Transition
import android.transition.TransitionValues
import android.view.ViewGroup

/**
 * 自定义Transition
 * package_name:transition_name:property_name
 */
class CustomTransition : Transition() {

    private val PROPNAME_BACKGROUND = "com.jiangkang.customtransition:CustomTransition:background"

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(values: TransitionValues) {
        val view = values.view
        // Store its background property in the values map
        values.values[PROPNAME_BACKGROUND] = view.background
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun createAnimator(sceneRoot: ViewGroup?, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
        return super.createAnimator(sceneRoot, startValues, endValues)
    }

}