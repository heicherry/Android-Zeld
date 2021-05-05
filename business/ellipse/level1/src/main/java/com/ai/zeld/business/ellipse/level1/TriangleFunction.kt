package com.ai.zeld.business.ellipse.level1

import com.ai.zeld.util.sqrt
import com.ai.zeld.util.square

class TriangleFunction(val data: EllipseData) {
    fun cal(x: Float, positive: Boolean): Float {
        val pos1 = (x - data.xOffset).square() / data.a.square()
        val yAndOffset = ((1 - pos1) * (1.0F * data.b).square()).sqrt()
        return if (positive) yAndOffset + data.yOffset
        else data.yOffset - yAndOffset
    }
}

data class EllipseData(val xOffset: Float, val a: Float, val yOffset: Float, val b: Float)