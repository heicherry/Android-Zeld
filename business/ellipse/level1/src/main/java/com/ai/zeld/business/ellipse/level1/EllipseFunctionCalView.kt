package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.util.AttributeSet
import android.view.View
import com.ai.zeld.util.sqrt
import com.ai.zeld.util.square
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.ellipse_level1_cal.view.*
import kotlinx.android.synthetic.main.ellipse_level1_cal2.view.*

typealias TriangleFunction = (x: Float, positive: Boolean) -> Float

class EllipseFunctionCalView(context: Context, attrs: AttributeSet?) :
    BaseFunctionControlView(context, attrs) {

    private var listener: ((function: TriangleFunction) -> Unit)? = null

    init {
        inflate(context, R.layout.ellipse_level1_cal2, this)

        x_offset.numberPicker().apply {
            value = 1
            maxValue = 300
            minValue = -300
            setOnValueChangedListener(this@EllipseFunctionCalView)
            setOnScrollListener(this@EllipseFunctionCalView)
        }
        y_offset.numberPicker().apply {
            value = 100
            setOnValueChangedListener(this@EllipseFunctionCalView)
            setOnScrollListener(this@EllipseFunctionCalView)
        }

        a.numberPicker().apply {
            value = 100
            setOnValueChangedListener(this@EllipseFunctionCalView)
            setOnScrollListener(this@EllipseFunctionCalView)
        }

        b.numberPicker().apply {
            value = 100
            setOnValueChangedListener(this@EllipseFunctionCalView)
            setOnScrollListener(this@EllipseFunctionCalView)
        }
    }

    fun setFunctionChangeListener(listener: (function: TriangleFunction) -> Unit) {
        this.listener = listener
        onScrollStateChange(x_2_end, NumberPicker.OnScrollListener.SCROLL_STATE_IDLE)
    }

    override fun onScrollStateChange(view: NumberPicker?, scrollState: Int) {
        super.onScrollStateChange(view, scrollState)
        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
            listener?.invoke { x, positive ->
                val pos1 =
                    (x - x_offset.numberPicker().value).square() / (1.5F * a.numberPicker().value).square()
                val yAndOffset =
                    ((1 - pos1) * (1.5F * b.numberPicker().value.toFloat()).square()).sqrt()
                if (positive) yAndOffset + y_offset.numberPicker().value
                else y_offset.numberPicker().value - yAndOffset
            }
        }
    }

    private fun View.numberPicker(): NumberPicker = this as NumberPicker
}