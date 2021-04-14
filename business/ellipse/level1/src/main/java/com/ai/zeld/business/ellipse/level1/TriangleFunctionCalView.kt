package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.util.AttributeSet
import com.ai.zeld.business.elllipse.level1.R
import com.shawnlin.numberpicker.NumberPicker
import kotlin.math.sin

typealias TriangleFunction = (x: Float) -> Float

class TriangleFunctionCalView(context: Context, attrs: AttributeSet?) :
    BaseFunctionControlView(context, attrs) {
    private var a: NumberPicker
    private var b: NumberPicker
    private var c: NumberPicker
    private var d: NumberPicker
    private var listener: ((function: TriangleFunction) -> Unit)? = null

    init {
        inflate(context, R.layout.ellipse_level1_cal_sin, this)
        a = findViewById(R.id.a)
        b = findViewById(R.id.b)
        c = findViewById(R.id.c)
        d = findViewById(R.id.d)

        a.setOnValueChangedListener(this)
        b.setOnValueChangedListener(this)
        c.setOnValueChangedListener(this)
        d.setOnValueChangedListener(this)

        a.setOnScrollListener(this)
        b.setOnScrollListener(this)
        c.setOnScrollListener(this)
        d.setOnScrollListener(this)
    }

    fun setFunctionChangeListener(listener: (function: TriangleFunction) -> Unit) {
        this.listener = listener
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        super.onValueChange(picker, oldVal, newVal)

    }

    override fun onScrollStateChange(view: NumberPicker?, scrollState: Int) {
        super.onScrollStateChange(view, scrollState)
        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
            listener?.invoke {
                a.value * sin(b.value.toFloat() * it + c.value) + d.value
            }
        }
    }
}