package com.ai.zeld.common.uikit.panel

import android.content.Context
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.ai.zeld.util.px2sp

@RequiresApi(Build.VERSION_CODES.M)
class NormalView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        android.R.attr.textViewStyle
    )

    init {
        setTextColor(context!!.getColor(android.R.color.holo_blue_bright))
        textSize = 50.px2sp().toFloat()
    }

}