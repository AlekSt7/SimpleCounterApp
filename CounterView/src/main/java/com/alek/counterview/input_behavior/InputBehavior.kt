package com.alek.counterview.input_behavior

import androidx.annotation.CallSuper
import com.alek.counterview.CounterView

/**
 * A simple class that specifies the visual behavior of a counter when its value changes
 * */
abstract class InputBehavior() {


    protected var first: Boolean = true
        private set

    /**
     * Called when clicking on any button or on the counterview
     * */
    open fun onInteraction(counter: CounterView) {

    }

    /**
     * Called when the plus button is clicked
     * */
    @CallSuper
    open fun onPlusButtonClick(counter: CounterView) {
        onInteraction(counter)
    }

    /**
     * Called when the minus button is clicked
     * */
    @CallSuper
    open fun onMinusButtonClick(counter: CounterView) {
        onInteraction(counter)
    }

    /**
     * Called once when the value is reached or exceeded the maximum
     * */
    @CallSuper
    open fun onMaxValueReached(maxValue: Int, counter: CounterView) {
        if(first) first = false
    }

    /**
     * It is always called when the value is equal to or greater than the maximum value
     * */
    open fun onMaxValue(maxValue: Int, counter: CounterView) {

    }

    /**
     * Called once when the value is reached or below the minimum
     * */
    @CallSuper
    open fun onMinValueReached(minValue: Int, counter: CounterView) {
        if(first) first = false
    }


    /**
     * It is always called when the value is equal to or less than the minimum value
     * */
    open fun onMinValue(minValue: Int, counter: CounterView) {

    }

    /**
     * It is called once when the value has become less than the maximum
     * */
    open fun onMaxValueDroppedBelow(maxValue: Int, counter: CounterView) {

    }

    /**
     * It is called once when the value has become higher than the minimum
     * */
    open fun onMinValueExceeded(minValue: Int, counter: CounterView) {

    }

    /**
     * It is always called when the value changes
     * */
    open fun onValueChanged(value: Int, increment: Boolean, counter: CounterView) {

    }

}