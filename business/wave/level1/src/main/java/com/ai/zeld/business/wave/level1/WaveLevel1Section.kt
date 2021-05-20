package com.ai.zeld.business.wave.level1

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
import com.ai.zeld.playground.body.ShakeBarrierBody
import com.ai.zeld.playground.body.VirusBody
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.postInMainDelay
import com.badlogic.gdx.physics.box2d.Box2D

@Section(SectionConfig.WAVE_MIN)
open class WaveLevel1Section : BaseBusinessSection() {
    private lateinit var world: IWorld
    private lateinit var stage: IStage
    private lateinit var box2DView: Box2DView
    private lateinit var functionControlView: TriangleFunctionCalView
    protected lateinit var bodyManager: BodyManager
    private lateinit var flyBody: FlyBody

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.wave_main, null)
    }

    override fun onPreload() {
        super.onPreload()
        world = IWorld::class.java.load()
        stage = IStage::class.java.load()
        initCoordinate()
        initViews()
    }

    override fun onSectionEnter() {
        super.onSectionEnter()
        initPlayGround()
        initFlyBody()
        initMonsters()
        initFunctionControlPanel()
    }

    private fun initCoordinate() {
        val resource = localContext.resources
        val newCenterY = stage.getCoordinateRect()
            .height() - resource.getDimension(R.dimen.wave_coordinate_center_offset_bottom)
        val newCoordinateCenter = PointF(stage.getCenterPointF().x, newCenterY)
        stage.updateCoordinate(stage.getCoordinateRect(), newCoordinateCenter)
    }

    private fun initViews() {
        box2DView = rootViewTree!!.findViewById(R.id.box2d)
        bodyManager = box2DView.getBodyManager()
        box2DView.showBoundary(true)
    }

    private fun initFlyBody() {
        flyBody = bodyManager.createBody(
            BodyManager.BodyType.HERO,
            RectF(),
            R.drawable.playground_bean_eater.idToBitmap()
        ) as FlyBody
        rootViewTree!!.findViewById<ImageView>(R.id.go).setOnClickListener {
            MusicClipsPlayerManager.play(MusicClip.GO)
            postInMainDelay(500) {
                bodyManager.startPlay()
            }
        }
        flyBody.setGameListener(this)
    }

    private fun initFunctionControlPanel() {
        functionControlView = rootViewTree!!.findViewById(R.id.function_control)
        functionControlView.setFunctionChangeListener {
            flyBody.setFunctionCal(it)
        }
    }

    protected open fun initMonsters() {
        createBarrier(440F, 790F, R.drawable.playground_mine)
        createBarrier(600F, 1000F, R.drawable.playground_mine)
    }

    protected open fun createBarrier(x: Float, y: Float, bitmapId: Int) {
        bodyManager.createBody<BarrierBody>(
            BodyManager.BodyType.BARRIER,
            PointF(x, y), bitmapId.idToBitmap()
        )
    }

    private fun initPlayGround() {
        val y = stage.getCoordinateRect()
            .height() - localContext.resources.getDimension(R.dimen.wave_coordinate_init_bottom_offset_bottom)
        box2DView.updatePlayGround(y)
    }

    override fun onReset() {
        bodyManager.reset()
        initFlyBody()
        initMonsters()
        initFunctionControlPanel()
    }

    override fun onSucceed(diamondCount: Int) {
        showGameResultHintDialog(true)
    }

    override fun onFailed() {
        showGameResultHintDialog(false)
    }
}