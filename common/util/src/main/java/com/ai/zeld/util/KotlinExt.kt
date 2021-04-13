package com.ai.zeld.util

import android.graphics.RectF
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

fun postInMain(run: () -> Unit) {
    ThreadPlus.mainHandler.post {
        run.invoke()
    }
}

fun postInMainDelay(delay: Long, run: () -> Unit) {
    ThreadPlus.mainHandler.postDelayed(delay) {
        run.invoke()
    }
}

fun postInPreload(run: () -> Unit) {
    ThreadPlus.preloadHandler.post {
        run.invoke()
    }
}

operator fun RectF.plus(rectF: RectF): MutableList<RectF> {
    return mutableListOf<RectF>().apply {
        add(this@plus)
        add(rectF)
    }
}

operator fun MutableList<RectF>.plus(rectF: RectF): MutableList<RectF> {
    add(rectF)
    return this
}