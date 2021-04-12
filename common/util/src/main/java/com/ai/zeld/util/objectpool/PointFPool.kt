package com.ai.zeld.util.objectpool

import android.graphics.PointF
import android.graphics.RectF

object PointFPool : SimpleObjectPool<PointF>(100, { PointF() }) {
    fun borrow(x: Float, y: Float): PointF {
        val pointF: PointF = borrow()
        pointF.set(x, y)
        return pointF
    }

    fun borrow(src: PointF): PointF {
        val pointF: PointF = borrow()
        pointF.set(src)
        return pointF
    }
}