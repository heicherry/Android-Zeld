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
    private lateinit var functionControlView: TriangleFunctionCalView

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.ellipse_level1_main, null)
    }

    override fun onPreload() {
        super.onPreload()
        Box2D.init()
        world = IWorld::class.java.load()
        stage = IStage::class.java.load()
        initCoordinate()
        initViews()
        initBall()
        initFunctionControlPanel()
        initPlayGround()
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
            BitmapFactory.decodeResource(
                localContext.resources,
                R.drawable.ellipse_level1_diamond
            )
        val centerPointX =
            localContext.resources.getDimension(R.dimen.ellipse_level1_wave_margin_left) + 100F
        val centerPointY = stage.getCenterPointF().y
        val ballRectF =
            RectF(
                centerPointX - ballBitmap.width / 2,
                centerPointY - ballBitmap.height / 2,
                centerPointX + ballBitmap.width / 2,
                centerPointY + ballBitmap.height / 2
            )
        box2DView.updateBall(ballRectF, ballBitmap)
    }

    private fun initFunctionControlPanel() {
        functionControlView = rootViewTree!!.findViewById(R.id.function_control)
        functionControlView.setFunctionChangeListener {
            box2DView.updateWaveFun(it)
            box2DView.updateFly(
                it,
                BitmapFactory.decodeResource(
                    localContext.resources,
                    R.drawable.ellipse_level1_bean_eater
                )
            )
            initMonsters()
        }
    }

    private fun initMonsters() {
        val ballBitmap =
            BitmapFactory.decodeResource(
                localContext.resources,
                R.drawable.ellipse_level1_diamond
            )
        box2DView.addMonster(100F, 100F, ballBitmap)
    }

    private fun initPlayGround() {
        val y = stage.getCoordinateRect()
            .height() - localContext.resources.getDimension(R.dimen.ellipse_level1_coordinate_init_bottom_offset_bottom)
        box2DView.updatePlayGround(y)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSectionEnter() {
        super.onSectionEnter()

    }
}