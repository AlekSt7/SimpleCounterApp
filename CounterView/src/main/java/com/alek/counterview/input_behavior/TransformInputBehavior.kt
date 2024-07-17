package com.alek.counterview.input_behavior

import com.alek.counterview.CounterView

open class TransformInputBehavior : InputBehavior() {

    override fun onMaxValueReached(maxValue: Int, counter: CounterView) {
        counter.inputFormat = CounterView.DECREMENT
        super.onMaxValueReached(maxValue, counter)
    }

    override fun onMinValueReached(minValue: Int, counter: CounterView) {
        counter.inputFormat = CounterView.INCREMENT
        super.onMinValueReached(minValue, counter)
    }

    override fun onMaxValueDroppedBelow(maxValue: Int, counter: CounterView) {
        counter.inputFormat = CounterView.FULL
    }

    override fun onMinValueExceeded(minValue: Int, counter: CounterView) {
        counter.inputFormat = CounterView.FULL
    }

}