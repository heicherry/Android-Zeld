package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.os.Vibrator
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.ai.zeld.business.elllipse.level1.R
import com.shawnlin.numberpicker.NumberPicker


class CalView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener {
    private var x_2_pre: NumberPicker
    private var x_1_pre: NumberPicker
    private var b: NumberPicker
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    init {
        inflate(context, R.layout.ellipse_level1_x_square, this)
        val x_2 = findViewById<TextView>(R.id.x_2)
        val m2 = SpannableString("x2")
        m2.setSpan(RelativeSizeSpan(0.5F), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        m2.setSpan(SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val spannableStringBuilder = SpannableStringBuilder("")
        spannableStringBuilder.append(m2)
        x_2.text = spannableStringBuilder

        x_2_pre = findViewById(R.id.x_2_pre)
        x_1_pre = findViewById(R.id.x_pre)
        b = findViewById(R.id.b)

        x_2_pre.setOnValueChangedListener(this)
        x_1_pre.setOnValueChangedListener(this)
        b.setOnValueChangedListener(this)

        x_2_pre.setOnScrollListener(this)
        x_1_pre.setOnScrollListener(this)
        b.setOnScrollListener(this)
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        vibrator.vibrate(10)
    }

    override fun onScrollStateChange(view: NumberPicker?, scrollState: Int) {
    }
}