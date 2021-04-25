package com.ai.zeld.business.ellipse.level1

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.ai.zeld.business.ellipse.level1.bodys.BodyManager
import com.ai.zeld.business.ellipse.level1.bodys.FlyBody
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.idToBitmap
import com.badlogic.gdx.physics.box2d.Box2D


@Section(SectionConfig.HERO_CAN_NOT_FLY)
class EllipseLevel1Section : BaseSection(), IGameResult {
    private lateinit var world: IWorld
    private lateinit var stage: IStage
    private lateinit var box2DView: Box2DView
    private lateinit var functionControlView: TriangleFunctionCalView
    private lateinit var bodyManager: BodyManager
    private lateinit var flyBody: FlyBody

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
        initPlayGround()
        initFlyBody()
        initMonsters()
        initFunctionControlPanel()
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
        bodyManager = box2DView.getBodyManager()
        box2DView.showBoundary(true)
    }

    private fun initBall() {
        val ballBitmap = R.drawable.ellipse_level1_diamond.idToBitmap()
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

    private fun initFlyBody() {
        flyBody = bodyManager.createBody(
            BodyManager.BodyType.FLY,
            RectF(),
            R.drawable.ellipse_level1_bean_eater.idToBitmap()
        ) as FlyBody
        rootViewTree!!.findViewById<ImageView>(R.id.go).setOnClickListener {
            flyBody.startFly()
        }
        flyBody.setGameListener(this)
    }

    private fun initFunctionControlPanel() {
        functionControlView = rootViewTree!!.findViewById(R.id.function_control)
        functionControlView.setFunctionChangeListener {
            flyBody.setFunctionCal(it)
        }
    }

    private fun initMonsters() {
        createBarrier(240F, 900F, R.drawable.ellipse_level1_mine)
        createBarrier(440F, 790F, R.drawable.ellipse_level1_mine)
        createBarrier(600F, 1000F, R.drawable.ellipse_level1_mine)

        bodyManager.createBody(
            BodyManager.BodyType.VIRUS_BARRIER,
            PointF(700F, 200F), R.drawable.ellipse_level1_virus.idToBitmap()
        )

        bodyManager.createBody(
            BodyManager.BodyType.DIAMOND,
            PointF(900F, 900F), R.drawable.ellipse_level1_diamond.idToBitmap()
        )
    }

    private fun createBarrier(x: Float, y: Float, bitmapId: Int) {
        bodyManager.createBody(
            BodyManager.BodyType.BARRIER,
            PointF(x, y), bitmapId.idToBitmap()
        )
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

    override fun onSucceed(diamondCount: Int) {
        Log.i("ayy", "成功了")
    }

    override fun onFailed() {
        Log.i("ayy", "失败了！！")
    }
}