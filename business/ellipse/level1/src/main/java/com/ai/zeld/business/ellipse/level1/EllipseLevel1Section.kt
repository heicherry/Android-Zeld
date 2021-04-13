package com.ai.zeld.business.ellipse.level1

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.claymore.load
import com.badlogic.gdx.physics.box2d.Box2D


@Section(SectionConfig.HERO_CAN_NOT_FLY)
class EllipseLevel1Section : BaseSection() {
    private lateinit var world: IWorld
    private lateinit var stage: IStage
    private lateinit var box2DView: Box2DView

    override fun onPreload() {
        super.onPreload()
        Box2D.init()
        world = IWorld::class.java.load()
        stage = IStage::class.java.load()
        initCoordinate()
        initViews()
        initBall()
    }

    private fun initCoordinate() {
        val resource = localContext.resources
        val newCenterY = stage.getCoordinateRect()
            .height() - resource.getDimension(R.dimen.ellipse_level1_coordinate_center_offset_bottom)
        val newCoordinateCenter = PointF(stage.getCenterPointF().x, newCenterY)
        stage.updateCoordinate(stage.getCoordinateRect(), newCoordinateCenter)
    }

    private fun initViews() {
        box2DView = rootViewTree!!.findViewById(R.id.box2d)
        box2DView.showBoundary(true)
    }

    private fun initBall() {
        val ballBitmap =
            BitmapFactory.decodeResource(localContext.resources, R.drawable.ellipse_level1_diamond)
        val centerPointX =
            localContext.resources.getDimension(R.dimen.ellipse_level1_wave_margin_left) + 100F
        val centerPointY = stage.getCenterPointF().y - 300F
        val ballRectF =
            RectF(
                centerPointX - ballBitmap.width / 2,
                centerPointY - ballBitmap.height / 2,
                centerPointX + ballBitmap.width / 2,
                centerPointY + ballBitmap.height / 2
            )
        box2DView.updateBall(ballRectF, ballBitmap)
    }

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.ellipse_level1_main, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSectionEnter() {
        super.onSectionEnter()

    }
}