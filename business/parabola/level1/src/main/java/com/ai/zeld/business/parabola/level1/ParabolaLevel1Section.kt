package com.ai.zeld.business.parabola.level1

import android.annotation.SuppressLint
import android.graphics.PointF
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.media.MusicClip
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.playground.BaseBusinessSection
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.Box2DView
import com.ai.zeld.playground.body.BarrierBody
import com.ai.zeld.playground.body.Coin
import com.ai.zeld.playground.body.VirusBody
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.postInMainDelay
import com.ai.zeld.util.px2dp
import com.ai.zeld.util.showRectF
import com.badlogic.gdx.physics.box2d.Box2D


@Section(SectionConfig.FLY_MIN)
open class ParabolaLevel1Section : BaseBusinessSection() {
    private lateinit var world: IWorld
    private lateinit var stage: IStage
    private lateinit var box2DView: Box2DView
    private lateinit var functionControlView: ParabolaFunctionCalView
    protected lateinit var bodyManager: BodyManager
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
            .height() - localContext.resources.getDimension(R.dimen.parabola_level1_coordinate_init_bottom_offset_bottom)
        box2DView.updatePlayGround(y)
    }

    override fun onReset() {
        bodyManager.reset()
        initFlyBody()
        initMonsters()
        functionControlView.reset()
    }

    override fun onSucceed(diamondCount: Int) {
        showGameResultHintDialog(true)
    }

    override fun onFailed() {
        val originalBitmap = R.drawable.uikit_superman_waiting_for_fly.idToBitmap()
        val superMan = rootViewTree!!.findViewById<ImageView>(R.id.superman)
        superMan.setImageBitmap(originalBitmap)
        showGameResultHintDialog(false)
    }
}