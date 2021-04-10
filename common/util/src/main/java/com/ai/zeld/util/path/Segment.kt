package com.ai.zeld.util.path

import android.graphics.PointF
import com.ai.zeld.util.objectpool.PointFPool

class Segment {
    constructor(start: PointF, end: PointF)
    constructor(x1: Float, y1: Float, x2: Float, y2: Float) : this(
        PointFPool.borrow(x1, y1),
        PointFPool.borrow(x2, y2)
    )
}