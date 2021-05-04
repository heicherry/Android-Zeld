package com.ai.zeld.playground

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.util.*
import com.ai.zeld.util.claymore.load
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*


class Box2DView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    companion object {
        private const val BOUNDARY_THICKNESS = 10F
        private const val BALL_RADIUS = 50F
    }

    private var inited = false

    // Rect 区域
    private val boundaryLeftRectF = RectF()
    private val boundaryRightRectF = RectF()
    private val boundaryTopRectF = RectF()
    private val boundaryBottomRectF = RectF()
    private val ballRectF = RectF()
    private val playGroundRectF = RectF()

    // 绘制工具区域
    private val paint = Paint()
    private var boundaryShow = true

    // box2d
    private var world: World = World(Vector2(0F, 1F), true)
    private val stage = IStage::class.java.load()
    private var playGround: Box2dBody? = null


    // 非Box2d的世界
    private val bodyManager = BodyManager(world) {
        postInvalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != 0 && h != 0 && !inited) {
            inited = true
            initBoundary()
            createBounds()
        }
        postInvalidate()
    }

    private fun initBoundary() {
        boundaryTopRectF.set(0F, 0F, width.toFloat(), BOUNDARY_THICKNESS)
        boundaryBottomRectF.set(0F, height - BOUNDARY_THICKNESS, width.toFloat(), height.toFloat())
        boundaryLeftRectF.set(0F, 0F, BOUNDARY_THICKNESS, height.toFloat())
        boundaryRightRectF.set(width - BOUNDARY_THICKNESS, 0F, width.toFloat(), height.toFloat())
        ballRectF.set(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            width / 2 + BALL_RADIUS,
            height / 2 + BALL_RADIUS
        )
    }


    fun showBoundary(show: Boolean) {
        if (boundaryShow != show) postInvalidate()
        boundaryShow = show
    }

    fun getBodyManager() = bodyManager

    fun updatePlayGround(y: Float) {
        val start = resources.getDimension(R.dimen.playground_margin_left)
        val end =
            stage.getCoordinateRect().right - resources.getDimension(R.dimen.playground_margin_right)
        playGroundRectF.set(start, y, end, y + 1)
        playGround?.let { world.destroyBody(it) }
        playGround = playGroundRectF.convertToBody(world, BodyDef.BodyType.StaticBody, false)
        postInvalidate()
    }


    private fun drawBoundary(canvas: Canvas) {
        (boundaryLeftRectF + boundaryRightRectF + boundaryTopRectF + boundaryBottomRectF).forEach {
            it.draw(canvas, Color.BLUE)
        }
    }


    private fun createBounds() {
        (boundaryLeftRectF + boundaryRightRectF + boundaryTopRectF + boundaryBottomRectF).forEach {
            it.convertToBody(world, BodyDef.BodyType.StaticBody, false, 0.8F)
        }
    }

    private fun drawPlayGround(canvas: Canvas) {
        playGroundRectF.draw(canvas,Color.BLUE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        world.stepExt()
        if (boundaryShow) {
            drawBoundary(canvas)
        }
        drawPlayGround(canvas)
        bodyManager.draw(canvas)
        postInvalidate()
    }
}