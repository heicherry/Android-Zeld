package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.widget.FrameLayout
import com.shawnlin.numberpicker.NumberPicker

open class BaseFunctionControlView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        vibrator.vibrate(10)
    }

    override fun onScrollStateChange(view: NumberPicker?, scrollState: Int) {
    }
}