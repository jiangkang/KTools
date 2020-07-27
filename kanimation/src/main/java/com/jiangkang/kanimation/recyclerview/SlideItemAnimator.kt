package com.jiangkang.kanimation.recyclerview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import android.util.Log

/**
 * 自右向左滑动动效
 * {@link android.support.v7.widget.RecyclerView#notifyItemChanged(int position)}
 *
 * */
class SlideItemAnimator : androidx.recyclerview.widget.SimpleItemAnimator() {

    private val DEBUG = true

    private var mDefaultInterpolator: TimeInterpolator? = null
    private var mPendingChanges = ArrayList<ChangeInfo>()
    private var mChangesList = ArrayList<ArrayList<ChangeInfo>>()
    private var mChangeAnimations = ArrayList<androidx.recyclerview.widget.RecyclerView.ViewHolder>()

    class ChangeInfo(var oldHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?, var newHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?) {
        var fromX: Int = 0
        var fromY: Int = 0
        var toX: Int = 0
        var toY: Int = 0

        internal constructor(
                oldHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, newHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                fromX: Int, fromY: Int, toX: Int, toY: Int
        ) : this(oldHolder, newHolder) {
            this.fromX = fromX
            this.fromY = fromY
            this.toX = toX
            this.toY = toY
        }

        override fun toString(): String {
            return ("ChangeInfo{"
                    + "oldHolder=" + oldHolder
                    + ", newHolder=" + newHolder
                    + ", fromX=" + fromX
                    + ", fromY=" + fromY
                    + ", toX=" + toX
                    + ", toY=" + toY
                    + '}'.toString())
        }
    }

    private val TAG = "KItemAnimator"

    override fun animateAdd(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun runPendingAnimations() {
        val changePending = !mPendingChanges.isEmpty()
        if (changePending) {
            val changes = ArrayList<ChangeInfo>()
            changes.addAll(mPendingChanges)

            mChangesList.add(changes)
            mPendingChanges.clear()

            val changer = Runnable {
                changes.forEach {
                    animateChangeImpl(it)
                }
                changes.clear()
                mChangesList.remove(changes)
            }.run()
        }
    }

    private fun animateChangeImpl(changeInfo: ChangeInfo) {
        val oldHolder = changeInfo.oldHolder
        val oldView = oldHolder?.itemView
        val newHolder = changeInfo.newHolder
        val newView = newHolder?.itemView

        oldView?.run {
            val oldViewAnim = animate().apply { duration = changeDuration }
            mChangeAnimations.add(oldHolder)
            oldViewAnim.apply {
                //旧View 向左移动
                translationX(-oldView.width.toFloat())

                //旧View 不动
//                translationX(0.0f)
                alpha(0.0f)
                setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animator: Animator) {
                        dispatchChangeStarting(oldHolder, true)
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        oldViewAnim.setListener(null)
                        this@run.alpha = 1f
                        this@run.translationX = 0f
                        this@run.translationY = 0f
                        dispatchChangeFinished(oldHolder, true)
                        mChangeAnimations.remove(oldHolder)
                        dispatchFinishedWhenDone()
                    }
                })
            }.start()
        }

        newView?.run {
            val newViewAnim = animate()
            mChangeAnimations.add(newHolder)
            newViewAnim.alpha(1.0f)
                    .translationX(0.0f)
                    .setDuration(changeDuration)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animator: Animator) {
                            dispatchChangeStarting(changeInfo.newHolder, false)
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            newViewAnim.setListener(null)
                            newView.alpha = 1f
                            newView.translationX = 0f
                            newView.translationY = 0f
                            dispatchChangeFinished(newHolder, false)
                            mChangeAnimations.remove(newHolder)
                            dispatchFinishedWhenDone()
                        }
                    }).start()

        }


    }

    override fun animateMove(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        return true
    }

    /**
     *
     * @param oldHolder The original item that changed.
     * @param newHolder The new item that was created with the changed content. Might be null
     * @param fromLeft  Left of the old view holder
     * @param fromTop   Top of the old view holder
     * @param toLeft    Left of the new view holder
     * @param toTop     Top of the new view holder
     * @return true if a later call to {@link #runPendingAnimations()} is requested,
     * false otherwise.
     * */
    override fun animateChange(
            oldHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
            newHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
            fromLeft: Int,
            fromTop: Int,
            toLeft: Int,
            toTop: Int
    ): Boolean {
        if (oldHolder == newHolder) {
            Log.d(TAG, "oldHolder == newHolder")
        }
        resetAnimation(oldHolder)

        oldHolder.run {
            itemView.translationX = 0.0f
            itemView.alpha = 1.0f
        }

        newHolder.run {
            resetAnimation(this)
            itemView.translationX = itemView.width.toFloat()
            itemView.alpha = 0.0f
        }

        mPendingChanges.add(ChangeInfo(oldHolder, newHolder, fromLeft, fromTop, toLeft, toTop))
        //如果要求后面调用 @see #runPedingAnimations(),则返回true，否则返回false
        return true
    }

    override fun isRunning(): Boolean {
        return (!mPendingChanges.isEmpty()
                || !mChangeAnimations.isEmpty()
                || !mChangesList.isEmpty())
    }

    override fun endAnimation(item: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        val view = item.itemView
        //取消属性动画
        view.animate().cancel()
        endChangeAnimation(mPendingChanges, item)

        for (i in mChangesList.indices.reversed()) {
            val changes = mChangesList[i]
            endChangeAnimation(changes, item)
            if (changes.isEmpty()) {
                mChangesList.removeAt(i)
            }
        }

        if (mChangeAnimations.remove(item) && DEBUG) {
            throw IllegalStateException("after animation is cancelled, item should not be in " + "mChangeAnimations list")
        }
        dispatchFinishedWhenDone()
    }

    private fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    override fun animateRemove(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun endAnimations() {
        var count = mPendingChanges.size
        for (i in count - 1 downTo 0) {
            endChangeAnimationIfNecessary(mPendingChanges[i])
        }
        mPendingChanges.clear()
        if (!isRunning) {
            return
        }
    }

    private fun resetAnimation(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        if (mDefaultInterpolator == null) {
            mDefaultInterpolator = ValueAnimator().interpolator
        }
        holder.itemView.animate().interpolator = mDefaultInterpolator
        endAnimation(holder)
    }

    private fun endChangeAnimation(infoList: MutableList<ChangeInfo>, item: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        for (i in infoList.indices.reversed()) {
            val changeInfo = infoList[i]
            if (endChangeAnimationIfNecessary(changeInfo, item)) {
                if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    infoList.remove(changeInfo)
                }
            }
        }
    }

    private fun endChangeAnimationIfNecessary(
            changeInfo: ChangeInfo,
            item: androidx.recyclerview.widget.RecyclerView.ViewHolder?
    ): Boolean {
        var oldItem = false
        if (changeInfo.newHolder === item) {
            changeInfo.newHolder = null
        } else if (changeInfo.oldHolder === item) {
            changeInfo.oldHolder = null
            oldItem = true
        } else {
            return false
        }
        item!!.itemView.alpha = 1f
        item.itemView.translationX = 0f
        item.itemView.translationY = 0f
        dispatchChangeFinished(item, oldItem)
        return true
    }

    private fun endChangeAnimationIfNecessary(changeInfo: ChangeInfo) {
        if (changeInfo.oldHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder)
        }
        if (changeInfo.newHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder)
        }
    }

}