package com.alek.simplecounterapp.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.View

object ViewUtils {

    fun <T : View> T.animateBackgroundColor(
        colorTo: Int,
        durationInMs: Int = 100
    ) {

        val colorDrawable =  background as ColorDrawable

        if (durationInMs > 0) {

            ValueAnimator.ofObject(
                ArgbEvaluator(),
                colorDrawable.color,
                colorTo
            ).apply {

                addUpdateListener { animator: ValueAnimator ->
                    colorDrawable.color = (animator.animatedValue as Int)
                    setBackgroundColor(colorDrawable.color)
                }
                duration = durationInMs.toLong()
                start()
            }
        }
    }

    fun <T: View> T.setViewVisibility(
        visibility: Int,
        offset: Float = 12f,
        duration: Long = 150
    ){
        if(this.visibility == visibility) return
        if(visibility == View.VISIBLE){
            this.translationY = offset
            this.visibility = View.VISIBLE
            this.animate()
                .translationY(0f)
                .alpha(1f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        this@setViewVisibility.translationY = 0f
                    }
                })
                .duration = duration
        }else{
            this.translationY = 0f
            this.animate()
                .translationY(-1*offset)
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        this@setViewVisibility.translationY = 0f
                        this@setViewVisibility.visibility = visibility
                        this@setViewVisibility.alpha = 1f
                    }
                })
                .duration = duration
        }
    }

}