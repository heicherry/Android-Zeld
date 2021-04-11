package com.ai.zeld.common.service.stage

import android.graphics.PointF
import android.graphics.RectF

interface IStage {
    fun getCoordinateRect(): RectF
    fun getCenterPointF(): PointF
    fun updateCoordinate(coordinate: RectF, centerPointF: PointF? = null)
    fun enableCoordinate(enable: Boolean)
}