package com.ai.zeld.business.world

import android.content.Context
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

    private var coordinateSystemRectF = RectF()
    private var localCenterPointF =
        PointF(coordinateSystemRectF.centerX(), coordinateSystemRectF.centerY())
    private lateinit var context: Context

    internal fun init(stageView: View) {
        this.stageView = stageView
        coordinateSystemView = stageView.findViewById(R.id.coordinate)
        context = coordinateSystemView.context
        val resource = context.resources
        coordinateSystemRectF.apply {
            val screenWidth = resource.displayMetrics.widthPixels
            val screenHeight =
                resource.displayMetrics.heightPixels - resource.getDimension(R.dimen.world_speaker_stage_height)
            left = 0F
            top = 0F
            right = (screenWidth - 1).toFloat()
            bottom = (screenHeight - 1).toFloat()
        }
        updateCoordinate(coordinateSystemRectF)
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