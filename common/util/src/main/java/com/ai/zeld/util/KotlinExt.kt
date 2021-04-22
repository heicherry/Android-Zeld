package com.ai.zeld.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.RectF
import android.view.View
import androidx.core.os.postDelayed
import androidx.lifecycle.Lifecycle
import com.ai.zeld.util.app.App
import com.ai.zeld.util.thread.ThreadPlus
import com.badlogic.gdx.math.Vector2

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

fun Int.idToBitmap(): Bitmap {
    return BitmapFactory.decodeResource(
        App.application.resources,
        this
    )
}

fun Bitmap.realPos(center: PointF) = RectF(
    center.x - width / 2,
    center.y - height / 2,
    center.x + width / 2,
    center.y + height / 2
)

fun Vector2.toPointF() = PointF(x, y)
