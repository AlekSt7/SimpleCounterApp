package com.alek.counterview

import android.os.CountDownTimer

/**
 * A simple class for incrementing a variable by time period
 * */
class ValueUpdater(private val maxValue: Int, private val minValue: Int, period: Long = 50) :
    CountDownTimer(Long.MAX_VALUE, period){

    private var onValueUpdatedCallback: ((value: Int) -> Unit)? = null

    private var value: Int = 0
    private var increment = true
    private var running = false

    fun update(value: Int, increment: Boolean) {
        this.value = value
        this.increment = increment
        start()
        running = true
    }

    fun stop() {
        if(running){
            running = false
            cancel()
        }
    }

    fun setOnValueUpdatedCallback(callback: ((value: Int) -> Unit)){
        this.onValueUpdatedCallback = callback
    }

    override fun onTick(p0: Long) {
        if(increment) {
            onValueUpdatedCallback?.invoke(++value)
        } else {
            onValueUpdatedCallback?.invoke(--value)
        }
        if(increment && value == maxValue) cancel()
        if(!increment && value == minValue) cancel()
    }

    override fun onFinish() { }

}