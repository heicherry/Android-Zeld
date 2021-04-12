package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.util.*
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import kotlin.math.sin


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

    // 绘制工具区域
    private val paint = Paint()
    private var wavePath: Path? = null
    private var boundaryShow = true

    // box2d
    private var world: World = World(Vector2(0F, 10F), true)
    private var ballBody: Body? = null
    private val stage = IStage::class.java.load()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != 0 && h != 0 && !inited) {
            inited = true
            initBoundary()
            initWave()
            createBounds()
            updateBallRectF(RectF(10F, 10F, 100F, 100F))
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

    fun updateBallRectF(rectF: RectF) {
        ballRectF.set(rectF)
        ballBody?.let { world.destroyBody(it) }
        ballBody = ballRectF.convertToBody(world, BodyDef.BodyType.DynamicBody, true)
        postInvalidate()
    }

    private fun initWave() {
        var body = createWaveBody(50F, 50F, 0F, 0F)

//        postInMainDelay(5000) {
//            world.destroyBody(body)
//            body = createWaveBody(150F, 50F, 100F, 100F)
//
//            postInMainDelay(5000) {
//                //world.destroyBody(body)
//                val pos = body.position
//                pos.y -= 50
//                body.setTransform(pos, 0F)
//                body.isAwake = true
//            }
//        }
    }

    private fun createWaveBody(a: Float, b: Float, c: Float, d: Float): Body {
        val start =
            -(stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_wave_margin_left))
        val end =
            stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_wave_margin_right)
        val cal: (Float) -> Float = { x: Float ->
            a * sin(x / b + c) + d
        }
        wavePath = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            start,
            end,
            step = 1F,
            cal
        )
        val pointArray = path2Array(wavePath!!, 1F)
        pointArray.forEachIndexed { index, fl ->
            pointArray[index] = fl.toBox2D()
        }

        val shape = ChainShape()
        shape.createChain(pointArray)

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody

        val fixtureDef = FixtureDef().apply {
            this.shape = shape
            density = 0.5f
            friction = 0.3f
            restitution = 0.5f
        }

        bodyDef.position.set(0F, 0F)
        val body = world.createBody(bodyDef)
        body.createFixture(fixtureDef)
        return body
    }

    private fun RectF.draw(canvas: Canvas, color: Int, des: String? = null) {
        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawRect(this, paint)
        if (des != null) {
            paint.color = Color.RED
            paint.textSize = 32F
            val bound = Rect()
            paint.getTextBounds(des, 0, des.length - 1, bound)
            canvas.drawText(
                des,
                centerX() - bound.width() / 2,
                centerY() - bound.height() / 2,
                paint
            )
        }
    }

    private fun drawBoundary(canvas: Canvas) {
        boundaryTopRectF.draw(canvas, Color.BLUE, "top")
        boundaryBottomRectF.draw(canvas, Color.BLUE, "bottom")
        boundaryLeftRectF.draw(canvas, Color.BLUE, "left")
        boundaryRightRectF.draw(canvas, Color.BLUE, "right")
    }

    private fun drawBall(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.RED
        ballBody?.apply {
            val newX = position.x.toPixels() - ballRectF.width() / 2
            val newY = position.y.toPixels() - ballRectF.height() / 2
            ballRectF.offsetTo(newX, newY)
            canvas.drawCircle(
                ballRectF.centerX(),
                ballRectF.centerY(),
                ballRectF.height() / 2,
                paint
            )
        }
    }

    private fun createBounds() {
        (boundaryLeftRectF + boundaryRightRectF + boundaryTopRectF + boundaryBottomRectF).forEach {
            it.convertToBody(world, BodyDef.BodyType.StaticBody)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        world.stepExt()
        if (boundaryShow) {
            drawBoundary(canvas)
        }
        drawBall(canvas)
        postInvalidate()
    }
}