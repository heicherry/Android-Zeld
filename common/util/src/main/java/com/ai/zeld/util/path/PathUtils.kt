package com.ai.zeld.util.path

import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.RectF


fun path2Array(path: Path, precision: Float): FloatArray {
    val pathMeasure = PathMeasure(path, false)
    val pathLength = pathMeasure.length
    val numPoints = (pathLength / precision).toInt() + 1
    val data = FloatArray(numPoints * 2)
    val position = FloatArray(2)
    var index = 0
    for (i in 0 until numPoints) {
        val distance = i * pathLength / (numPoints - 1)
        pathMeasure.getPosTan(distance, position, null)
        data[index] = position[0]
        data[index + 1] = position[1]
        index += 2
    }
    return data
}

fun createPath(
    centerPointF: PointF,
    coordinateSystemRectF: RectF,
    start: Float,
    end: Float,
    step: Float,
    cal: (Float) -> Float,
    penaltyZone: List<RectF>? = null
): Path {
    fun isInPenaltyZone(x: Float, y: Float): Boolean {
        val zones = penaltyZone ?: return false
        for (rectF in zones) {
            if (x >= rectF.left && x < rectF.right && y >= rectF.top && y < rectF.bottom) {
                return true
            }
        }
        return false
    }

    val path = Path()
    var temp = start
    while (temp <= end) {
        val x = temp + centerPointF.x
        val y = cal(temp) + centerPointF.y
        if (y <= coordinateSystemRectF.bottom
            && y >= coordinateSystemRectF.top
            && !isInPenaltyZone(x, y)
        ) {
            path.moveOrLineTo(x, y)
        }
        temp += step
    }
    val x = end + centerPointF.x
    val y = cal(end) + centerPointF.y
    if (y <= coordinateSystemRectF.bottom
        && y >= coordinateSystemRectF.top
        && !isInPenaltyZone(x, y)
    ) {
        path.moveOrLineTo(x, y)
    }
    return path
}


fun Path.moveOrLineTo(x: Float, y: Float) {
    if (isEmpty) {
        moveTo(x, y)
    } else {
        lineTo(x, y)
    }
}

