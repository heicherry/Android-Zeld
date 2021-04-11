package com.ai.zeld.business.world

import android.graphics.PointF
import android.graphics.RectF
import android.view.View
import com.ai.zeld.business.world.views.CoordinateSystemView
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.util.gone
import com.ai.zeld.util.visible

class Stage : IStage {
    private lateinit var stageView: View
    private lateinit var coordinateSystemView: CoordinateSystemView

    private var coordinateSystemRectF = RectF(0F, 0F, 500F, 500F)
    private var localCenterPointF =
        PointF(coordinateSystemRectF.centerX(), coordinateSystemRectF.centerY())

    internal fun init(stageView: View) {
        this.stageView = stageView
        coordinateSystemView = stageView.findViewById(R.id.coordinate)
    }

    override fun getCoordinateRect() = coordinateSystemRectF

    override fun getCenterPointF() = localCenterPointF

    override fun updateCoordinate(coordinate: RectF, centerPointF: PointF?) {
        coordinateSystemRectF.set(coordinate)
        if (centerPointF != null) {
            this.localCenterPointF.set(centerPointF)
        } else {
            this.localCenterPointF.set(
                coordinateSystemRectF.centerX(),
                coordinateSystemRectF.centerY()
            )
        }
        coordinateSystemView.updateCoordinate(coordinate, this.localCenterPointF)
    }

    override fun enableCoordinate(enable: Boolean) {
        if (enable) {
            coordinateSystemView.visible()
        } else {
            coordinateSystemView.gone()
        }
    }
}