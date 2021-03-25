package com.ai.zeld.util

import android.view.View
import androidx.core.os.postDelayed
import androidx.lifecycle.Lifecycle
import com.ai.zeld.util.thread.ThreadPlus

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun postInMainDelay(lifecycle: Lifecycle?, delay: Long, run: () -> Unit) {
    ThreadPlus.mainHandler.postDelayed(delay) {
        if (lifecycle != null) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                run.invoke()
            }
        } else {
            run.invoke()
        }
    }
}