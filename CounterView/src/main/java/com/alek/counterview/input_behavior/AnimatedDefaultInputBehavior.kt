package com.alek.counterview.input_behavior

import android.view.animation.AnimationUtils
import androidx.annotation.CallSuper
import com.alek.counterview.CounterView
import com.alek.counterview.R

open class AnimatedDefaultInputBehavior : DefaultInputBehavior() {

    override fun onMaxValue(maxValue: Int, counter: CounterView) {
        if(!first) {
            counter.startAnimation(AnimationUtils.loadAnimation(counter.context, R.anim.shake))
        }
    }

    override fun onInteraction(counter: CounterView) {
        if(!counter.active) {
            counter.startAnimation(AnimationUtils.loadAnimation(counter.context, R.anim.shake))
        }
    }

}