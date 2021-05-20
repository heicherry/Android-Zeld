package com.ai.zeld.business.ellipse.level1

import android.annotation.SuppressLint
import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.media.MusicClip
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.playground.BaseBusinessSection
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.Box2DView
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.BarrierBody
import com.ai.zeld.playground.body.Coin
import com.ai.zeld.playground.body.VirusBody
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.postInMainDelay
import com.badlogic.gdx.physics.box2d.Box2D

@Section(SectionConfig.ELLIPSE_MIN)
open class EllipseLevel1Section : BaseBusinessSection() {
    private lateinit var world: IWorld
    private lateinit var stage: IStage
    private lateinit var box2DView: Box2DView
    protected lateinit var functionControlView: EllipseFunctionCalView
    protected lateinit var bodyManager: BodyManager
    protected lateinit var flyBody: BallRing
    protected lateinit var targetBody: TargetBallRing

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
        initTargetBody()
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

    protected open fun initFlyBody() {
        flyBody = bodyManager.createBody(
            BodyManager.BodyType.HERO,
            RectF(),
            R.drawable.playground_diamond.idToBitmap()
        ) as BallRing
        rootViewTree!!.findViewById<ImageView>(R.id.go).setOnClickListener {
            MusicClipsPlayerManager.play(MusicClip.GO)
            postInMainDelay(500) {
                bodyManager.startPlay()
            }
        }
        flyBody.setGameListener(this)
    }

    protected open fun initTargetBody() {
        targetBody = bodyManager.createBody(
            BodyManager.BodyType.OTHERS,
            RectF(),
            R.drawable.playground_rose.idToBitmap()
        )
        targetBody.setFunctionCal(TriangleFunction(EllipseData(1F, 100F, 100F, 100F)))
    }

    protected open fun initFunctionControlPanel() {
        functionControlView = rootViewTree!!.findViewById(R.id.function_control)
        functionControlView.setFunctionChangeListener {
            flyBody.setFunctionCal(it)
            val center = stage.getCenterPointF()
            val currentH = functionControlView.calCurrentH()
            currentH.first.apply {
                x += center.x
                y = center.y - y
            }
            currentH.second.apply {
                x += center.x
                y = center.y - y
            }
            flyBody.setNearestH(currentH.first, currentH.second)
        }
        functionControlView.setErrorCallback {
            Log.i("ayy", "错误了，调回原来的样子！")
        }
    }

    protected open fun initMonsters() {
        createBarrier(440F, 790F, R.drawable.playground_mine)
        createBarrier(600F, 1000F, R.drawable.playground_mine)
    }

    protected fun createBarrier(x: Float, y: Float, bitmapId: Int) {
        bodyManager.createBody<BarrierBody>(
            BodyManager.BodyType.BARRIER,
            PointF(x, y), bitmapId.idToBitmap()
        )
    }

    private fun initPlayGround() {
        val y = stage.getCoordinateRect()
            .height() - localContext.resources.getDimension(R.dimen.ellipse_level1_coordinate_init_bottom_offset_bottom)
        box2DView.updatePlayGround(y)
    }

    override fun onReset() {
        bodyManager.reset()
        initFlyBody()
        initMonsters()
        initTargetBody()
        functionControlView.reset()
        initFunctionControlPanel()
    }

    override fun onSucceed(diamondCount: Int) {
        super.onSucceed(diamondCount)
        showGameResultHintDialog(true)
    }

    override fun onFailed() {
        super.onFailed()
        showGameResultHintDialog(false)
    }
}