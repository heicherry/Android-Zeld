package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.graphics.PointF
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
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.sin

typealias TriangleFunction = (x: Float, positive: Boolean) -> Float
typealias ErrorCallback = () -> Unit

class EllipseFunctionCalView(context: Context, attrs: AttributeSet?) :
    BaseFunctionControlView(context, attrs) {

    private var listener: ((function: TriangleFunction) -> Unit)? = null
    private var initAre: Double = 0.0
    private var lastEllipseData: EllipseData? = null
    private var errorCallback: ErrorCallback? = null

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
            value = 150
            setOnValueChangedListener(this@EllipseFunctionCalView)
            setOnScrollListener(this@EllipseFunctionCalView)
        }

        initAre = a.numberPicker().value * b.numberPicker().value * Math.PI / 2
    }

    fun setFunctionChangeListener(listener: (function: TriangleFunction) -> Unit) {
        this.listener = listener
        onScrollStateChange(x_2_end, NumberPicker.OnScrollListener.SCROLL_STATE_IDLE)
    }

    fun setErrorCallback(errorCallback: ErrorCallback) {
        this.errorCallback = errorCallback
    }

    override fun onScrollStateChange(view: NumberPicker?, scrollState: Int) {
        super.onScrollStateChange(view, scrollState)
        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
            if (isDataIllegal()) {
                lastEllipseData = EllipseData(
                    x_offset.numberPicker().value,
                    a.numberPicker().value,
                    y_offset.numberPicker().value,
                    b.numberPicker().value
                )
                listener?.invoke { x, positive ->
                    val pos1 =
                        (x - x_offset.numberPicker().value).square() / (a.numberPicker().value.toFloat()).square()
                    val yAndOffset =
                        ((1 - pos1) * (1.0F * b.numberPicker().value.toFloat()).square()).sqrt()
                    if (positive) yAndOffset + y_offset.numberPicker().value
                    else y_offset.numberPicker().value - yAndOffset
                }
            } else {
                errorCallback?.invoke()
                lastEllipseData?.let {
                    x_offset.numberPicker().value = it.xOffset
                    a.numberPicker().value = it.a
                    y_offset.numberPicker().value = it.yOffset
                    b.numberPicker().value = it.b
                }
            }
        }
    }

    private fun call(a: Double, b: Double, h: Double): Double {
        val partA = Math.PI / 2
        val partB = asin(1 - h / b)
        val partC = 0.5 * sin(2 * asin(1 - h / b))
        return a * b * (partA - partB - partC)
    }

    fun calCurrentH(): Pair<PointF, PointF> {
        var currentNearestH = 0.0
        var currentNearestOffset = Double.MAX_VALUE
        var currentH = 0.0

        val a = a.numberPicker().value.toDouble()
        val b = b.numberPicker().value.toDouble()

        while (currentH <= 2 * b) {
            val offset = abs(call(a, b, currentH) - initAre)
            if (offset < currentNearestOffset) {
                currentNearestH = currentH
                currentNearestOffset = offset
            }
            currentH += 1
        }
        val x = x_offset.numberPicker().value.toFloat()
        val y_of = y_offset.numberPicker().value

        return Pair(
            PointF(x, (-b + y_of).toFloat()),
            PointF(x, (currentNearestH - b + y_of).toFloat())
        )
    }

    private fun isDataIllegal() =
        a.numberPicker().value * b.numberPicker().value * Math.PI >= initAre

    private fun View.numberPicker(): NumberPicker = this as NumberPicker
}

data class EllipseData(val xOffset: Int, val a: Int, val yOffset: Int, val b: Int)