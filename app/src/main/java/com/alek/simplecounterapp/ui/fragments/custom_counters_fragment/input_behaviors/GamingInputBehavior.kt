package com.alek.simplecounterapp.ui.fragments.custom_counters_fragment.input_behaviors

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.alek.counterview.CounterView
import com.alek.counterview.input_behavior.DefaultInputBehavior
import com.alek.simplecounterapp.R
import com.alek.simplecounterapp.utils.ViewUtils.animateBackgroundColor
import kotlin.random.Random


class GamingInputBehavior(private val comboValueCount: Byte) : DefaultInputBehavior() {

    private var rotateDegrees = 0f
    private var scaleSize = 0f
    private var comboMode = false
    private var comboIterator: Byte = 0
    private var latestCombo = 0
    private var comboRecord = 0

    private var comboListener: ((record: Int, current: Int) -> Unit)? = null

    override fun onPlusButtonClick(counter: CounterView) {
        super.onPlusButtonClick(counter)
        counter.apply {

            if(counterValue + 1 < maxValue){

                comboMode = true
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    counter.animateBackgroundColor(ContextCompat.getColor(
                        context,
                        com.alek.counterview.R.color.background
                    ), 2000)
                    resetCombo()
                    setValueText(null)
                    inputFormat = CounterView.FULL
                }, 500)

                if(comboMode) comboIterator++
                if(comboMode && comboIterator > comboValueCount){
                    if(inputFormat != CounterView.INCREMENT) inputFormat = CounterView.INCREMENT
                    comboClicks(this)
                }
            }
        }
    }

    private fun resetCombo(){
        comboMode = false
        comboIterator = 0
        if(latestCombo > comboRecord) comboRecord = latestCombo
        comboListener?.invoke(comboRecord, latestCombo)
        latestCombo = 0
    }

    private fun comboClicks(counter: CounterView) {
        counter.apply {
            latestCombo++
            if(latestCombo + 10 == comboRecord) {
                this.animateBackgroundColor(ContextCompat.getColor(context, R.color.orange))
            } else if(latestCombo + 5 == comboRecord) {
                this.animateBackgroundColor(ContextCompat.getColor(context, R.color.red))
            }
            val spannableText = SpannableString("Combo +${latestCombo}!")
            spannableText.setSpan(
                RelativeSizeSpan(1.3f),
                6,
                spannableText.length,
                0
            )
            setValueText(spannableText)
            animateClick(this)
        }
    }

    private fun animateClick(counter: CounterView) {
        counter.apply {

            val random = Random(System.currentTimeMillis())
            rotateDegrees = (random.nextFloat() * 30f)
            scaleSize = (random.nextFloat() * 1f)
            if(random.nextBoolean()) rotateDegrees *= -1

            val rotationRunnable = Runnable {
                this@apply.animate()
                    .rotation(0f)
                    .setDuration(200)
                    .setInterpolator(LinearInterpolator())
                    .start()
            }


            val scaleRunnable = Runnable {
                this@apply.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .setInterpolator(LinearInterpolator())
                    .start()
            }

            this.animate()
                .rotationBy(rotateDegrees)
                .setDuration(50)
                .setInterpolator(LinearInterpolator())
                .withEndAction(rotationRunnable)
                .start()

            this.animate()
                .scaleXBy(scaleSize)
                .scaleYBy(scaleSize)
                .setDuration(50)
                .setInterpolator(LinearInterpolator())
                .withEndAction(scaleRunnable)
                .start()

        }
    }

    fun setComboListener(listener: (record: Int, current: Int) -> Unit) {
        this.comboListener = listener
    }

}