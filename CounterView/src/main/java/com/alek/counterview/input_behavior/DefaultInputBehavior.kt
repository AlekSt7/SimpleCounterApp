package com.alek.counterview.input_behavior

import android.view.animation.AnimationUtils
import androidx.annotation.CallSuper
import com.alek.counterview.CounterView

open class DefaultInputBehavior : InputBehavior() {

    override fun onMaxValueReached(maxValue: Int, counter: CounterView) {
        counter.plusActive = false
        super.onMaxValueReached(maxValue, counter)
    }

    override fun onMinValueReached(minValue: Int, counter: CounterView) {
        counter.minusActive = false
        super.onMinValueReached(minValue, counter)
    }

    override fun onMaxValueDroppedBelow(maxValue: Int, counter: CounterView) {
        counter.plusActive = true
    }

    override fun onMinValueExceeded(minValue: Int, counter: CounterView) {
        counter.minusActive = true
    }

}