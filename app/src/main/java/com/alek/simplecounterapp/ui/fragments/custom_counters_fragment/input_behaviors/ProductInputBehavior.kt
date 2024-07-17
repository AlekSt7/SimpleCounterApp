package com.alek.simplecounterapp.ui.fragments.custom_counters_fragment.input_behaviors

import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.alek.counterview.CounterView
import com.alek.counterview.input_behavior.AnimatedDefaultInputBehavior
import com.alek.simplecounterapp.R
import com.alek.simplecounterapp.utils.ViewUtils.animateBackgroundColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Currency
import java.util.Locale

class ProductInputBehavior(
    private val price: Double,
    private val currency: Currency,
    private val locale: Locale,
    private val withErrorBg: Boolean = false,
    private val withTransparentBg: Boolean = false,
    private val withClicked: Boolean = false
    )
    : AnimatedDefaultInputBehavior() {

    private var plusButtonClicked = false

    private val decimalFormat: DecimalFormat
    private val dfs: DecimalFormatSymbols

    init {
        decimalFormat = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        dfs = DecimalFormatSymbols.getInstance(locale)
        dfs.currencySymbol = currency.symbol
        decimalFormat.decimalFormatSymbols = dfs
    }

    override fun onPlusButtonClick(counter: CounterView) {
        if(withClicked && counter.counterValue == counter.minValue) plusButtonClicked = true
        super.onPlusButtonClick(counter)
    }

    override fun onInteraction(counter: CounterView) {
        super.onInteraction(counter)
        if(withClicked && counter.counterValue == counter.minValue && !plusButtonClicked) {
            counter.setValue(1)
        }
        plusButtonClicked = false
    }

    override fun onMinValueReached(minValue: Int, counter: CounterView) {
        super.onMinValueReached(minValue, counter)
        counter.apply {
            if(!withTransparentBg) {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        com.alek.counterview.R.color.background
                    )
                )
                activeColorAccent = com.alek.counterview.R.color.active
            }
            inputFormat = CounterView.INCREMENT
            setValueText(decimalFormat.format(price))
        }
    }

    override fun onMinValueExceeded(minValue: Int, counter: CounterView) {
        super.onMinValueExceeded(minValue, counter)
        counter.inputFormat = CounterView.FULL
        counter.apply {
            if(!withTransparentBg) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                activeColorAccent = R.color.white
            }
            counter.setValueText(null)
        }
    }

    private val colorHandler = Handler(Looper.getMainLooper())

    override fun onMaxValue(maxValue: Int, counter: CounterView) {
        super.onMaxValue(maxValue, counter)
        counter.apply {

            if(withErrorBg){
                animateBackgroundColor(
                    ContextCompat.getColor(context, R.color.warning)
                )
                colorHandler.removeCallbacksAndMessages(null)
                colorHandler.postDelayed({
                    animateBackgroundColor(
                        ContextCompat.getColor(context, R.color.black)
                    )
                }, 350)
            }
        }
    }

}