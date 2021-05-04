package com.ai.zeld.util.path

import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.RectF
import android.webkit.WebStorage


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
    originPath: Path? = null
): Path {
    fun checkIsEnd(temp: Float) = (start < end && temp <= end) || (start > end && temp >= end)

    val path = originPath ?: Path()
    var temp = start
    while (checkIsEnd(temp)) {
        val x = temp + centerPointF.x
        val y = centerPointF.y - cal(temp)
        if (y <= coordinateSystemRectF.bottom
            && y >= coordinateSystemRectF.top
        ) {
            path.moveOrLineTo(x, y)
        }
        temp += step
    }

    val pathEnd = end

    val x = pathEnd + centerPointF.x
    val y = centerPointF.y - cal(pathEnd)
    if (y <= coordinateSystemRectF.bottom
        && y >= coordinateSystemRectF.top
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

