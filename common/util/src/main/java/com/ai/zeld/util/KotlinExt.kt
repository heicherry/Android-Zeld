package com.ai.zeld.util

import android.animation.Animator
import android.graphics.*
import android.util.Log
import android.view.View
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.graphics.toRectF
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import com.ai.zeld.util.app.App
import com.ai.zeld.util.thread.ThreadPlus
import com.badlogic.gdx.math.Vector2
import kotlin.math.max
import kotlin.math.min

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

fun Vector2.toRealWorldPointF() = PointF(x.toPixels(), y.toPixels())

fun RectF.scale(value: Float): RectF {
    val dest = RectF()
    dest.left = centerX() - (width() / 2) * value
    dest.right = centerX() + (width() / 2) * value
    dest.top = centerY() - (height() / 2) * value
    dest.bottom = centerY() + (height() / 2) * value
    return dest
}

inline fun Animator.doOnEndExt(crossinline action: (animator: Animator) -> Unit) {
    var isCancel = false
    doOnCancel {
        isCancel = true
    }
    doOnEnd {
        if (!isCancel) {
            action(it)
        }
    }
}

private val commonPaint = Paint()

fun RectF.draw(canvas: Canvas, color: Int, des: String? = null) {
    commonPaint.color = color
    commonPaint.isAntiAlias = true
    commonPaint.style = Paint.Style.FILL_AND_STROKE
    canvas.drawRect(this, commonPaint)
    if (des != null) {
        commonPaint.color = Color.RED
        commonPaint.textSize = 32F
        val bound = Rect()
        commonPaint.getTextBounds(des, 0, des.length - 1, bound)
        canvas.drawText(
            des,
            centerX() - bound.width() / 2,
            centerY() - bound.height() / 2,
            commonPaint
        )
    }
}

fun Float.square(): Float {
    return this * this
}

fun Float.sqrt(): Float {
    return kotlin.math.sqrt(this.toDouble()).toFloat()
}

private val tempPointF = PointF()
fun FloatArray.eachPoint(action: (index: Int, point: PointF) -> Unit) {
    var index = 0
    while (index < size) {
        tempPointF.x = get(index)
        tempPointF.y = get(index + 1)
        action(index / 2, tempPointF)
        index += 2
    }
}

fun FloatArray.firstPointF() = PointF(get(0), get(1))

fun FloatArray.point(index: Int): PointF? {
    return if (index * 2 + 1 < size)
        PointF(get(index * 2), get(index * 2 + 1))
    else null
}

fun FloatArray.lastPointF() = PointF(get(lastIndex - 1), get(lastIndex))

fun FloatArray.containRectF(): RectF {
    val rectF = RectF(Float.MAX_VALUE, Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)
    eachPoint { _, point ->
        rectF.apply {
            left = min(left, point.x)
            right = max(right, point.x)
            top = min(top, point.y)
            bottom = max(bottom, point.y)
        }
    }
    return rectF
}

fun RectF.center() = PointF(centerX(), centerY())

fun Int.px2sp(): Int {
    val fontScale: Float = App.application.resources.displayMetrics.scaledDensity
    return (this / fontScale + 0.5f).toInt()
}

fun Int.px2dp(): Float {
    val scale: Float = App.application.resources.displayMetrics.density
    return this / scale + 0.5f
}

fun RectF.distance(target: RectF): Float {
    return ((left - target.left).square()
            + (right - target.right).square()
            + (top - target.top).square()
            + (bottom - target.bottom).square()).sqrt()
}

fun View.showRectF(): RectF {
    val rect = Rect()
    getLocalVisibleRect(rect)
    rect.offset(left, top)
    return rect.toRectF()
}

fun View.layoutRectF(): RectF {
    return Rect(left, top, right, bottom).toRectF()
}

fun View.moveCenterTo(center: PointF) {
    val originCenter = layoutRectF().center()
    translationX = center.x - originCenter.x
    translationY = center.y - originCenter.y
}

fun View.resetPos() {
    translationX = 0F
    translationY = 0F
    rotation = 0F
}

typealias Block = () -> Unit