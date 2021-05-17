package com.ai.zeld.business.parabola.level1

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.ai.zeld.playground.BaseFunctionControlView
import com.ai.zeld.util.square
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.parabola_level1_cal.view.*


typealias ParabolaFunction = (x: Float) -> Float

class ParabolaFunctionCalView(context: Context, attrs: AttributeSet?) :
    BaseFunctionControlView(context, attrs) {

    private var listener: ((function: ParabolaFunction) -> Unit)? = null

    init {
        inflate(context, R.layout.parabola_level1_cal, this)

        a.numberPicker().apply {
            maxValue = 300
            minValue = -300
            value = -96
            setOnValueChangedListener(this@ParabolaFunctionCalView)
            setOnScrollListener(this@ParabolaFunctionCalView)
        }
        b.numberPicker().apply {
            maxValue = 1000
            minValue = -300
            value = 15
            setOnValueChangedListener(this@ParabolaFunctionCalView)
            setOnScrollListener(this@ParabolaFunctionCalView)
        }

        c.numberPicker().apply {
            maxValue = 1000
            minValue = -1000
            value = 95
            setOnValueChangedListener(this@ParabolaFunctionCalView)
            setOnScrollListener(this@ParabolaFunctionCalView)
        }
    }

    fun setFunctionChangeListener(listener: (function: ParabolaFunction) -> Unit) {
        this.listener = listener
        onScrollStateChange(a.numberPicker(), NumberPicker.OnScrollListener.SCROLL_STATE_IDLE)
    }

    override fun onScrollStateChange(view: NumberPicker?, scrollState: Int) {
        super.onScrollStateChange(view, scrollState)
        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
            listener?.invoke {
                a.numberPicker().value.toFloat() / 5 * (it / 80).square() + b.numberPicker().value * it / 80 + c.numberPicker().value * 4
            }
        }
    }

    private fun View.numberPicker(): NumberPicker = this as NumberPicker
}