package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ai.zeld.business.ellipse.level1.bodys.BodyManager
import com.ai.zeld.business.ellipse.level1.bodys.FlyBody
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.util.*
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
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
    private var flyBody: FlyBody? = null
    private val monsterRectFList = mutableListOf<RectF>()

    // 绘制工具区域
    private val paint = Paint()
    private var wavePath: Path? = null
    private var boundaryShow = true

    // box2d
    private var world: World = World(Vector2(0F, 1F), true)
    private var ballBody: Body? = null
    private val stage = IStage::class.java.load()
    private var waveBody: Body? = null
    private var playGround: Body? = null
    private val monsterBodyList = mutableMapOf<Body, Bitmap>()

    // 绘制资源
    private var ballBitmap: Bitmap? = null

    // 非Box2d的世界
    private val bodyManager = BodyManager {
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

    fun updateBall(rectF: RectF, bitmap: Bitmap? = null) {
        ballBitmap = bitmap
        ballRectF.set(rectF)
        ballBody?.let { world.destroyBody(it) }
        ballBody = ballRectF.convertToBody(world, BodyDef.BodyType.DynamicBody, true, 0.98F)
        ballBody?.let { it.applyLinearImpulse(0F, 10F, it.worldCenter.x, it.worldCenter.y, true) }
        postInvalidate()
    }

    fun updateWaveFun(cal: TriangleFunction) {
//        waveBody?.let { world.destroyBody(it) }
//        waveBody = createWaveBody(cal)
        postInvalidate()
    }

    fun updateFly(cal: TriangleFunction, bitmap: Bitmap) {
        flyBody = bodyManager.createBody(BodyManager.BodyType.FLY, RectF(), bitmap) as FlyBody
        flyBody?.let {
            it.setFunctionCal(cal)
            it.startFly()
        }
        bodyManager.createBody(
            BodyManager.BodyType.BARRIER, PointF(100F, 100F),
            R.drawable.ellipse_level1_virus.idToBitmap()
        )
    }

    fun addMonster(x: Float, y: Float, bitmap: Bitmap) {
        val rectF = RectF(
            x - bitmap.width / 2,
            y - bitmap.height / 2,
            x + bitmap.width / 2,
            y + bitmap.height / 2
        )
        monsterRectFList.add(rectF)
        monsterBodyList[rectF.convertToBody(world, BodyDef.BodyType.DynamicBody, true)] = bitmap
    }

    fun updatePlayGround(y: Float) {
        val start = resources.getDimension(R.dimen.ellipse_level1_wave_margin_left)
        val end =
            stage.getCoordinateRect().right - resources.getDimension(R.dimen.ellipse_level1_wave_margin_right)
        playGroundRectF.set(start, y, end, y + 1)
        playGround?.let { world.destroyBody(it) }
        playGround = playGroundRectF.convertToBody(world, BodyDef.BodyType.StaticBody, false)
        postInvalidate()
    }

    private fun createWaveBody(cal: TriangleFunction): Body {
        val start =
            -(stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_wave_margin_left))
        val end =
            stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_wave_margin_right)
        wavePath = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            start,
            end,
            step = 3F,
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
            friction = 1f
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
            if (ballBitmap != null) {
                val saveCount = canvas.save()
                canvas.rotate(ballBody!!.angle, ballRectF.centerX(), ballRectF.centerY())
                canvas.drawBitmap(ballBitmap!!, null, ballRectF, null)
                canvas.restoreToCount(saveCount)
            } else {
                canvas.drawCircle(
                    ballRectF.centerX(),
                    ballRectF.centerY(),
                    ballRectF.height() / 2,
                    paint
                )
            }
        }
    }

    private fun createBounds() {
        (boundaryLeftRectF + boundaryRightRectF + boundaryTopRectF + boundaryBottomRectF).forEach {
            it.convertToBody(world, BodyDef.BodyType.StaticBody, false, 0.8F)
        }
    }

    private fun drawWavePath(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        wavePath?.let { canvas.drawPath(it, paint) }
    }

    private fun drawPlayGround(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        canvas.drawRect(playGroundRectF, paint)
    }

    private fun drawFlyBody(canvas: Canvas) {
        bodyManager.draw(canvas)
    }

    private fun drawMonsters(canvas: Canvas) {
        monsterBodyList.forEach { (body, bitmap) ->
            val newX = body.position.x.toPixels() - ballRectF.width() / 2
            val newY = body.position.y.toPixels() - ballRectF.height() / 2
            val rectF = RectF(newX, newY, newX + bitmap.width, newY + bitmap.height)
            canvas.drawBitmap(bitmap, null, rectF, paint)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        world.stepExt()
        if (boundaryShow) {
            drawBoundary(canvas)
        }
        drawBall(canvas)
        drawWavePath(canvas)
        drawPlayGround(canvas)
        drawFlyBody(canvas)
        drawMonsters(canvas)
        postInvalidate()
    }
}