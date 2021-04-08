package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ai.zeld.util.objectpool.RectFPool
import com.ai.zeld.util.path.createPath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*


class Box2DView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    companion object {
        private const val BOUNDARY_THICKNESS = 10F
        private const val BALL_RADIUS = 50F
    }

    // Rect 区域
    private val boundaryLeftRectF = RectF()
    private val boundaryRightRectF = RectF()
    private val boundaryTopRectF = RectF()
    private val boundaryBottomRectF = RectF()
    private val ballRectF = RectF()

    // 绘制工具区域
    private val paint = Paint()

    // box2d
    private var world: World = World(Vector2(0F, 10F), true)
    private var ballBody: Body? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != 0 && h != 0) {
            initBoundary()
            createBounds()
            createBox2DBall()
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
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        val box = PolygonShape()

        val fixtureDef = FixtureDef().apply {
            shape = box
            density = 0.5f
            friction = 0.3f
            restitution = 0.5f
        }

        createBox2DWorldBound(bodyDef, fixtureDef, box, boundaryTopRectF)
        createBox2DWorldBound(bodyDef, fixtureDef, box, boundaryBottomRectF)
        createBox2DWorldBound(bodyDef, fixtureDef, box, boundaryLeftRectF)
        createBox2DWorldBound(bodyDef, fixtureDef, box, boundaryBottomRectF)
    }

    private fun createBox2DWorldBound(
        bodyDef: BodyDef,
        fixtureDef: FixtureDef,
        box: PolygonShape,
        rectF: RectF
    ) {
        box.setAsBox(rectF.width().toBox2D(), rectF.height().toBox2D())
        bodyDef.position.set(rectF.centerX().toBox2D(), rectF.centerY().toBox2D())
        world.createBody(bodyDef).createFixture(fixtureDef)
    }

    private fun createBox2DBall() {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        val box = CircleShape()

        val fixtureDef = FixtureDef().apply {
            shape = box
            density = 1.5f
            friction = 0.4f
            restitution = 0.6f
        }

        box.radius = (ballRectF.width() / 2).toBox2D()
        bodyDef.position.set((width / 2).toFloat().toBox2D(), (height / 2).toFloat().toBox2D())
        //bodyDef.linearVelocity[Math.random().toFloat()] = Math.random().toFloat() * 100

        ballBody = world.createBody(bodyDef)
        ballBody?.createFixture(fixtureDef)
        box.dispose()
    }

    private val mVelocityIterations = 5 //速率迭代器

    private val mPosiontIterations = 20 //迭代次数


    private fun doPhysicalStep() {
        val dt = 1f / 60f
        world.step(dt, mVelocityIterations, mPosiontIterations)
    }

    private fun buildWave(){
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody

        val box = PolygonShape()

    }

    private fun drawPathTest(canvas: Canvas){

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制边界
        doPhysicalStep()
        drawBoundary(canvas)
        drawBall(canvas)
        drawPathTest(canvas)
        postInvalidate()
    }
}

fun Float.toBox2D(): Float {
    return this / 50
}

fun Float.toPixels(): Float {
    return this * 50
}