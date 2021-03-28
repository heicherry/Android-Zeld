package com.ai.zeld.business.world.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.postDelayed
import com.ai.zeld.util.thread.ThreadPlus

class HorseTextView : AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var content: String = ""
    private var index = 0
    private val localHandler = ThreadPlus.mainHandler


    @SuppressLint("SetTextI18n")
    fun update(
        content: String,
        prefix: String,
        onEnd: (() -> Unit)? = null,
        waitingForClick: (() -> Unit)? = null,
        delay: Long = 1000,
        allElapseTime: Long = -1
    ): Long {
        text = ""
        localHandler.removeCallbacksAndMessages(null)
        this.content = prefix + content
        index = prefix.length
        localHandler.postDelayed(delay) {
            if (allElapseTime != -1L) {
                runUpdate(onEnd, allElapseTime / content.length)
            } else {
                runUpdate(onEnd)
            }
        }
        setOnClickListener {
            setOnClickListener(null)
            text = this.content
            localHandler.removeCallbacksAndMessages(null)
            onEnd?.invoke()
            waitingForClick?.invoke()
        }
        val time = if (allElapseTime == -1L) content.length * 300L else allElapseTime
        return time + 1000
    }

    private fun runUpdate(onEnd: (() -> Unit)?, wordElapse: Long = 300L) {
        post {
            if (content.length >= index) {
                text = content.substring(0, index)
            }
            index++
            if (index <= content.length) {
                localHandler.postDelayed(wordElapse) {
                    runUpdate(onEnd, wordElapse)
                }
            } else {
                onEnd?.invoke()
            }
        }
    }
}