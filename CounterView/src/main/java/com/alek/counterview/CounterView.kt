package com.alek.counterview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Outline
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.alek.counterview.databinding.LayoutCounterViewBinding
import com.alek.counterview.input_behavior.AnimatedDefaultInputBehavior
import com.alek.counterview.input_behavior.DefaultInputBehavior
import com.alek.counterview.input_behavior.InputBehavior
import com.alek.counterview.input_behavior.TransformInputBehavior


open class CounterView : LinearLayout {

    private var inputBehavior: InputBehavior? = null

    private lateinit var array: TypedArray

    private val mViewBinding: LayoutCounterViewBinding by lazy {
        LayoutCounterViewBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }

    private var textValue: Boolean = false

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {

        array = context.obtainStyledAttributes(attrs, R.styleable.CounterView)

        array.getInteger(
            R.styleable.CounterView_inputBehavior,
            -1
        ).apply {
            when(this) {
                0 -> setInputBehavior(DefaultInputBehavior())
                1 -> setInputBehavior(AnimatedDefaultInputBehavior())
                2 -> setInputBehavior(TransformInputBehavior())
            }
        }

        /**
         * Because Input Behavior can be set via a custom method some time after initialization
         * */
        Handler(Looper.getMainLooper()).postDelayed({
            if(inputBehavior == null) {
                Log.d(TAG, "Skipping initialization, InputBehavior is null")
                array.recycle()
            }
        }, 500)


    }

    constructor(
        context: Context
    ) : super(context)

    private fun initFromStyleableArray(array: TypedArray) {

        if(background == null) setBackgroundColorFromResource(R.color.background)

        setCorners(
            array.getDimension(
                R.styleable.CounterView_cornersRad,
                DEFAULT_CORNERS_RADIUS * resources.displayMetrics.density
            )
        )

        mViewBinding.minus.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                array.getResourceId(
                    R.styleable.CounterView_decrementImage,
                    R.drawable.baseline_remove_24
                )
            )
        )

        mViewBinding.plus.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                array.getResourceId(
                    R.styleable.CounterView_incrementImage,
                    R.drawable.baseline_add_24
                )
            )
        )

        mViewBinding.minusContainer.setBackgroundResource(
            array.getResourceId(
                R.styleable.CounterView_decrementBackground,
                R.color.transparent
            )
        )

        mViewBinding.plusContainer.setBackgroundResource(
            array.getResourceId(
                R.styleable.CounterView_incrementBackground,
                R.color.transparent
            )
        )

        activeColorAccent = array.getResourceId(
            R.styleable.CounterView_activeColorAccent,
            R.color.active
        )

        inactiveColorAccent = array.getResourceId(
            R.styleable.CounterView_inactiveColorAccent,
            R.color.inactive
        )

        array.getDimension(
            R.styleable.CounterView_size,
            DEFAULT_ELEMENTS_SIZE * resources.displayMetrics.density
        ).apply {

            setMinusSize(
                array.getDimension(
                    R.styleable.CounterView_minusSize,
                    this
                ).toInt()
            )

            setPlusSize(
                array.getDimension(
                    R.styleable.CounterView_plusSize,
                    this
                ).toInt()
            )

            setValueSize(
                array.getDimension(
                    R.styleable.CounterView_valueSize,
                    this
                )
            )

        }

        array.getDimension(
            R.styleable.CounterView_elementsPadding,
            DEFAULT_PADDING * resources.displayMetrics.density
        ).toInt().apply {
            if(this != 0) setElementsPadding(this)
        }

        inputFormat = array.getInteger(
            R.styleable.CounterView_inputFormat,
            FULL
        )

        active = array.getBoolean(
            R.styleable.CounterView_active,
            true
        )

        maxValue = array.getInteger(
            R.styleable.CounterView_maxValue,
            DEFAULT_MAX_VALUE
        )

        minValue = array.getInteger(
            R.styleable.CounterView_minValue,
            DEFAULT_MIN_VALUE
        )

        setValue(
            array.getInteger(
                R.styleable.CounterView_value,
                minValue
            ), false
        )

        setValueText(
            array.getString(
                R.styleable.CounterView_valueText
            )
        )

    }

    private var onValueChangeListener: OnValueChangeListener? = null
    private var onValueButtonClickListener: OnValueButtonClickListener? = null

    // variables --------------------------

    var counterValue: Int = DEFAULT_VALUE
        private set

    var maxValue: Int = DEFAULT_MAX_VALUE
        set(value){
            field = value
            if(counterValue >= maxValue) onMaxValueReached(maxValue)
        }

    var minValue: Int = DEFAULT_MIN_VALUE
        set(value){
            field = value
            if(counterValue <= minValue) onMinValueReached(maxValue)
        }

    var inputFormat: Int = FULL
        set(countVisibility){
            field = countVisibility
            when(countVisibility){
                FULL -> {
                    mViewBinding.minusContainer.visibility = VISIBLE
                    mViewBinding.plusContainer.visibility = VISIBLE
                }
                INCREMENT -> {
                    mViewBinding.minusContainer.visibility = GONE
                    mViewBinding.plusContainer.visibility = VISIBLE
                }
                DECREMENT -> {
                    mViewBinding.plusContainer.visibility = GONE
                    mViewBinding.minusContainer.visibility = VISIBLE
                }
                VALUE_ONLY -> {
                    mViewBinding.plusContainer.visibility = GONE
                    mViewBinding.minusContainer.visibility = GONE
                }
            }
        }

    var active = true
        set(isActive){
            field = isActive
            if (isActive) {
                plusActive = plusActiveOld
                minusActive = minusActiveOld
                mViewBinding.value.setTextColorFromResource(activeColorAccent)
            } else {
                plusActiveOld = plusActive
                minusActiveOld = minusActive
                plusActive = false
                minusActive = false
                mViewBinding.value.setTextColorFromResource(inactiveColorAccent)
            }
        }

    private var minusActiveOld = true

    var minusActive = true
        set(isActive){
            field = isActive
            val color = if(isActive) activeColorAccent else inactiveColorAccent
            mViewBinding.minus.setImageTintColor(color)
        }

    private var plusActiveOld = true

    var plusActive = true
        set(isActive){
            field = isActive
            val color = if (isActive) activeColorAccent else inactiveColorAccent
            mViewBinding.plus.setImageTintColor(color)
        }

    var inactiveColorAccent = R.color.inactive
        set(color){
            field = color
            if (!minusActive && !plusActive) {
                mViewBinding.minus.setImageTintColor(color)
                mViewBinding.plus.setImageTintColor(color)
            } else if (!plusActive) {
                mViewBinding.plus.setImageTintColor(color)
            } else if (!minusActive) {
                mViewBinding.minus.setImageTintColor(color)
            }
            if(!active) mViewBinding.value.setTextColorFromResource(color)
        }
    var activeColorAccent = R.color.active
        set(color){
            field = color
            if (minusActive && plusActive) {
                mViewBinding.minus.setImageTintColor(color)
                mViewBinding.plus.setImageTintColor(color)
            } else if (plusActive) {
                mViewBinding.plus.setImageTintColor(color)
            } else if (minusActive) {
                mViewBinding.minus.setImageTintColor(color)
            }
            if(active) mViewBinding.value.setTextColorFromResource(color)
        }

    // end variables ----------------------

    abstract class OnValueChangeListener {

        /**
         * It is always called when the value changes
         * */
        open fun onValueChanged(value: Int, isIncrement: Boolean, fromUser: Boolean) {}

        /**
         * Called once when the value is reached or exceeded the maximum
         * */
        open fun onMaxValueReached(maxValue: Int) {}

        /**
         * Called once when the value is reached or below the minimum
         * */
        open fun onMinValueReached(minValue: Int) {}
    }

    abstract class OnValueButtonClickListener {

        /**
         * return true if you want the value not to change after pressing the button
         * */
        open fun onPlusButtonClick(): Boolean {return false}

        /**
         * return true if you want the value not to change after pressing the button
         * */
        open fun onMinusButtonClick(): Boolean {return false}
    }

    companion object {
        const val INCREMENT = 0
        const val DECREMENT = 1
        const val FULL = 2
        const val VALUE_ONLY = 3

        private const val DEFAULT_ELEMENTS_SIZE = 12
        private const val DEFAULT_CORNERS_RADIUS = 4f
        private const val DEFAULT_MAX_VALUE = 1000
        private const val DEFAULT_MIN_VALUE = 0
        private const val DEFAULT_VALUE = DEFAULT_MIN_VALUE
        private const val DEFAULT_PADDING = 12f
        private const val TAG = "CounterView"
    }

    /**
     * @param withFastValueUpdate allows to change the value by long tap on + or -
     *
     * */
    @SuppressLint("ClickableViewAccessibility")
    private fun init(withFastValueUpdate: Boolean = false) {
        gravity = Gravity.CENTER
        orientation = HORIZONTAL


        if(withFastValueUpdate) {

            val updater = ValueUpdater(maxValue, minValue)

            updater.setOnValueUpdatedCallback {
                setValue(it, false)
            }

            mViewBinding.plusContainer.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) updater.stop()
                false
            }

            mViewBinding.plusContainer.setOnLongClickListener {
                updater.update(counterValue, true)
                true
            }

            mViewBinding.minusContainer.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) updater.stop()
                false
            }

            mViewBinding.minusContainer.setOnLongClickListener {
                updater.update(counterValue, false)
                true
            }

        }

        mViewBinding.root.setOnClickListener {
            inputBehavior?.onInteraction(this)
        }

        mViewBinding.minusContainer.setOnClickListener {
            var isValueBlocked = false
            inputBehavior?.onMinusButtonClick(this)
            if (onValueButtonClickListener != null) {
                isValueBlocked = onValueButtonClickListener!!.onMinusButtonClick()
            }
            if (!isValueBlocked) {
                setValue(counterValue-1, false)
            }
        }

        mViewBinding.plusContainer.setOnClickListener {
            var isValueBlocked = false
            inputBehavior?.onPlusButtonClick(this)
            if (onValueButtonClickListener != null) {
                isValueBlocked = onValueButtonClickListener!!.onPlusButtonClick()
            }
            if (!isValueBlocked) {
                setValue(counterValue+1, false)
            }
        }
    }

    private fun <T : ImageView> T.setImageTintColor(color: Int) {
        ImageViewCompat.setImageTintList(
            this,
            ColorStateList.valueOf(
                ContextCompat.getColor(context, color)
            )
        )
    }

    private fun <T : TextView> T.setTextColorFromResource(color: Int) {
        this.setTextColor(ContextCompat.getColor(context, color))
    }

    /**
     * The set text value will always be displayed instead of a numeric value until it becomes null
     * or empty
     *
     * If text value is not null or empty, the CounterVIew value still changes,
     * but the text value is displayed
     * */
    fun setValueText(string: CharSequence?) {
        if(!string.isNullOrEmpty()) {
            textValue = true
            mViewBinding.value.text = string
        } else {
            textValue = false
            mViewBinding.value.text = counterValue.toString()
        }
    }

    /**
     * @param withListener the parameter determines
     * whether the listener will be called when the value changes
     * */
    @JvmOverloads
    fun setValue(value: Int, withListener: Boolean = true) {
        var v = value
        val increment = this.counterValue < v

        if(v >= maxValue) onMaxValue(maxValue)
        if(v <= minValue) onMinValue(minValue)

        if(!active) return

        if(increment && !plusActive) return
        if(!increment && !minusActive) return

        if(v >= maxValue && counterValue < maxValue) onMaxValueReached(maxValue)
        if(v > maxValue && v != counterValue) {
            v = maxValue
        } else if(v < maxValue && counterValue >= maxValue) {
            onMaxValueDroppedBelow(maxValue)
        }

        if(v <= minValue && counterValue > minValue) onMinValueReached(minValue)
        if(v < minValue && v != counterValue){
            v = minValue
        } else if(v > minValue && counterValue <= minValue) {
            onMinValueExceeded(minValue)
        }

        if(v != counterValue) onValueChanged(value, increment, withListener)
    }

    fun getValue(): Int {
        return counterValue
    }

    /**
     * @param sizePX defines the size value in pixels
     * */
    fun setPlusSize(sizePX: Int){
        mViewBinding.plus.layoutParams.height = sizePX
    }

    /**
     * @param sizePX defines the size value in pixels
     * */
    fun setMinusSize(sizePX: Int){
        mViewBinding.minus.layoutParams.height = sizePX
    }

    /**
     * @param sizePX defines the size value in pixels
     * */
    fun setValueSize(sizePX: Float){
        mViewBinding.value.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePX)
    }

    /**
     * @param sizePX defines the size value in pixels
     * */
    fun setSize(sizePX: Float) {
        setPlusSize(sizePX.toInt())
        setValueSize(sizePX)
        setMinusSize(sizePX.toInt())
    }

    fun setInputBehavior(inputBehavior: InputBehavior) {
        Log.d(TAG, "Setting up new InputBehavior...")
        if(this.inputBehavior == null) {
            initFromStyleableArray(array)
            init(array.getBoolean(R.styleable.CounterView_fastInput, false))
            array.recycle()
            this.inputBehavior = inputBehavior
        } else {
            inputFormat = FULL
            active = true
            minusActive = true
            plusActive = true
            this.inputBehavior = inputBehavior
            mViewBinding.value.text = counterValue.toString()
        }
        if(counterValue <= minValue) onMinValueReached(minValue)
        if(counterValue >= maxValue) onMaxValueReached(minValue)

    }

    fun setOnValueChangeListener(listener: OnValueChangeListener?) {
        onValueChangeListener = listener
    }

    fun setOnValueButtonClickListener(listener: OnValueButtonClickListener?) {
        onValueButtonClickListener = listener
    }

    fun setValueLayoutWeight(weight: Float) {
        val param = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
            weight
        )
        mViewBinding.value.layoutParams = param
    }

    /**
     * @param cornerRadius defines the padding value in pixels
     * */
    fun setCorners(cornerRadiusPX: Float) {
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {

                val left = 0
                val top = 0;
                val right = view.width
                val bottom = view.height
                val cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX,
                    cornerRadiusPX,
                    resources.displayMetrics
                )

                outline.setRoundRect(left, top, right, bottom, cornerRadius)

            }
        }
    }

    /**
     * @param paddingPX defines the padding value in pixels
     * */
    fun setElementsPadding(paddingPX: Int){
        setPadding(paddingPX / 2, 0, paddingPX / 2, 0)
        mViewBinding.plus.setPadding(paddingPX / 2, 0, paddingPX / 2, 0)
        mViewBinding.value.setPadding(paddingPX / 2, 0, paddingPX / 2, 0)
        mViewBinding.minus.setPadding(paddingPX / 2, 0, paddingPX / 2, 0)
    }

    private fun onValueChanged(value: Int, increment: Boolean, withListener: Boolean) {

        counterValue = value
        if(!textValue) mViewBinding.value.text = value.toString()

        inputBehavior?.onValueChanged(value, increment, this)

        if (withListener) onValueChangeListener?.onValueChanged(value, increment, false)
    }

    private fun onMaxValueReached(maxValue: Int) {
        inputBehavior?.onMaxValueReached(maxValue, this)
        onValueChangeListener?.onMaxValueReached(maxValue)
    }

    private fun onMaxValue(maxValue: Int) {
        inputBehavior?.onMaxValue(maxValue, this)
    }

    private fun onMinValueReached(minValue: Int) {
        inputBehavior?.onMinValueReached(minValue, this)
        onValueChangeListener?.onMinValueReached(minValue)
    }

    private fun onMinValue(minValue: Int) {
        inputBehavior?.onMinValue(minValue, this)
    }

    private fun onMaxValueDroppedBelow(maxValue: Int) {
        inputBehavior?.onMaxValueDroppedBelow(maxValue, this)
    }

    private fun onMinValueExceeded(minValue: Int) {
        inputBehavior?.onMinValueExceeded(minValue, this)
    }

    private fun setBackgroundColorFromResource(colorId: Int) {
        val color: Int = if (Build.VERSION.SDK_INT >= 23) {
            ContextCompat.getColor(context!!, colorId)
        } else {
            context!!.resources.getColor(colorId)
        }
        setBackgroundColor(color)
    }

}