package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.util.AttributeSet
import com.ai.zeld.util.sqrt
import com.ai.zeld.util.square
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.ellipse_level1_cal.view.*

typealias TriangleFunction = (x: Float) -> Float

class EllipseFunctionCalView(context: Context, attrs: AttributeSet?) :
    BaseFunctionControlView(context, attrs) {

    private var listener: ((function: TriangleFunction) -> Unit)? = null

    init {
        inflate(context, R.layout.ellipse_level1_cal, this)
        val m2 = SpannableString("x2")
        m2.setSpan(RelativeSizeSpan(0.5F), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        m2.setSpan(SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        x_2.text = m2

        val m2end2 = SpannableString("2")
        m2end2.setSpan(RelativeSizeSpan(0.5F), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        m2end2.setSpan(SuperscriptSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        x_2_end_2.text = m2end2

        val y2 = SpannableString("y2")
        y2.setSpan(RelativeSizeSpan(0.5F), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        y2.setSpan(SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        y_2.text = y2

        y_2_end_2.text = m2end2

        x_2_end.setOnValueChangedListener(this)
        y_2_end.setOnValueChangedListener(this)
        end.setOnValueChangedListener(this)

        x_2_end.setOnScrollListener(this)
        y_2_end.setOnScrollListener(this)
        end.setOnScrollListener(this)
    }

    fun setFunctionChangeListener(listener: (function: TriangleFunction) -> Unit) {
        this.listener = listener
        onScrollStateChange(x_2_end, NumberPicker.OnScrollListener.SCROLL_STATE_IDLE)
    }

    override fun onScrollStateChange(view: NumberPicker?, scrollState: Int) {
        super.onScrollStateChange(view, scrollState)
        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
            listener?.invoke {
                ((end.value.toFloat() - it.square() / (1.5F * x_2_end.value).square()) * (1.5F * y_2_end.value.toFloat()).square()).sqrt()
            }
        }
    }
}