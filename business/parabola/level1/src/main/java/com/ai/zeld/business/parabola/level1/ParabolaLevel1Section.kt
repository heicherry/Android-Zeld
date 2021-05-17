package com.ai.zeld.business.parabola.level1

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
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.Box2DView
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.BarrierBody
import com.ai.zeld.playground.body.Coin
import com.ai.zeld.playground.body.VirusBody
import com.ai.zeld.playground.showFailedHintDialog
import com.ai.zeld.util.*
import com.ai.zeld.util.claymore.load
import com.badlogic.gdx.physics.box2d.Box2D


@Section(SectionConfig.HERO_CAN_NOT_FLY)
class ParabolaLevel1Section : BaseSection(), IGameResult {
    private lateinit var world: IWorld
    private lateinit var stage: IStage
    private lateinit var box2DView: Box2DView
    private lateinit var functionControlView: ParabolaFunctionCalView
    private lateinit var bodyManager: BodyManager
    private lateinit var flyBody: FlyPathBody

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.parabola_level1_main, null).let {
            val stage = IStage::class.java.load()
            it.measure(
                View.MeasureSpec.makeMeasureSpec(
                    stage.getCoordinateRect().width().toInt(),
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    stage.getCoordinateRect().height().toInt(),
                    View.MeasureSpec.EXACTLY
                )
            )
            it.layout(
                0,
                0,
                stage.getCoordinateRect().width().toInt(),
                stage.getCoordinateRect().height().toInt()
            )
            it
        }
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
            .height() - resource.getDimension(R.dimen.parabola_level1_coordinate_center_offset_bottom)
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
            R.drawable.playground_diamond.idToBitmap()
        ) as FlyPathBody
        rootViewTree!!.findViewById<ImageView>(R.id.go).setOnClickListener {
            MusicClipsPlayerManager.play(MusicClip.GO)
            postInMainDelay(500) {
                bodyManager.startPlay()
            }
        }
        flyBody.setGameListener(this)
        val leftStep = rootViewTree!!.findViewById<ImageView>(R.id.step_left)
        val rightStep = rootViewTree!!.findViewById<ImageView>(R.id.step_right)
        flyBody.setStartAndEndRectF(leftStep.showRectF(), rightStep.showRectF())
        initSuperMan()
    }

    private fun initSuperMan() {
        val superMan = rootViewTree!!.findViewById<ImageView>(R.id.superman)
        flyBody.bindView(superMan)
        val flyingBitmap = R.drawable.uikit_superman_flying.idToBitmap()
        val flySuccessBitmap = R.drawable.uikit_superman_fly_succeed.idToBitmap()
        flyBody.setFlyListener(object : IFlyStateListener {
            override fun onFlyStart() {
                superMan.setImageBitmap(flyingBitmap)
            }

            override fun onFlyEnd() {
                superMan.setImageBitmap(flySuccessBitmap)
                moveSuperManToSuperWomenAhead()
            }
        })
    }

    private fun moveSuperManToSuperWomenAhead() {
        val superWomen = rootViewTree!!.findViewById<ImageView>(R.id.superwomen)
        val superMan = rootViewTree!!.findViewById<ImageView>(R.id.superman)
        superMan.translationX = (superWomen.left - superMan.right - 30.px2dp()).toFloat()
    }

    private fun initFunctionControlPanel() {
        functionControlView = rootViewTree!!.findViewById(R.id.function_control)
        functionControlView.setFunctionChangeListener {
            flyBody.setFunctionCal(it)
        }
    }

    private fun initMonsters() {
        // createBarrier(240F, 900F, R.drawable.ellipse_level1_mine)
        createBarrier(440F, 790F, R.drawable.playground_mine)
        createBarrier(600F, 1000F, R.drawable.playground_mine)

        bodyManager.createBody<VirusBody>(
            BodyManager.BodyType.BARRIER,
            PointF(700F, 200F), R.drawable.playground_virus.idToBitmap()
        )

        bodyManager.createBody<FlyPathBody>(
            BodyManager.BodyType.OTHERS,
            PointF(300F, 900F), R.drawable.playground_diamond.idToBitmap()
        )

        bodyManager.createBody<Coin>(
            BodyManager.BodyType.COIN,
            PointF(900F, 900F),
            R.drawable.playground_coin_1.idToBitmap()
        )
    }

    private fun createBarrier(x: Float, y: Float, bitmapId: Int) {
        bodyManager.createBody<BarrierBody>(
            BodyManager.BodyType.BARRIER,
            PointF(x, y), bitmapId.idToBitmap()
        )
    }

    private fun initPlayGround() {
        val y = stage.getCoordinateRect()
            .height() - localContext.resources.getDimension(R.dimen.parabola_level1_coordinate_init_bottom_offset_bottom)
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
        val originalBitmap = R.drawable.uikit_superman_waiting_for_fly.idToBitmap()
        val superMan = rootViewTree!!.findViewById<ImageView>(R.id.superman)
        superMan.setImageBitmap(originalBitmap)

        showFailedHintDialog()
    }
}