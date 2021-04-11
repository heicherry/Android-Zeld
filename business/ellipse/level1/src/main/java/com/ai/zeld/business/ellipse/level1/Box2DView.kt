package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.PathUtils
import com.ai.zeld.util.objectpool.PointFPool
import com.ai.zeld.util.objectpool.RectFPool
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
import com.ai.zeld.util.postInMainDelay
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import kotlin.math.atan
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
    private val coordinateRectF = RectF()

    // 绘制工具区域
    private val paint = Paint()
    private var wavePath: Path? = null

    // box2d
    private var world: World = World(Vector2(0F, 10F), true)
    private var ballBody: Body? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != 0 && h != 0 && !inited) {
            inited = true
            initCoordinate()
            initBoundary()
            initWave()
            createBounds()
            createBox2DBall()
            testContactListener()
        }
        postInvalidate()
    }

    private fun initCoordinate() {
        coordinateRectF.set(0F, 0F, width.toFloat(), height.toFloat())
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

    private fun initWave() {
        var body = createWaveBody(150F, 50F, 0F, 0F)

        postInMainDelay(5000) {
            world.destroyBody(body)
            body = createWaveBody(150F, 50F, 100F, 100F)

            postInMainDelay(5000) {
                //world.destroyBody(body)
                val pos = body.position
                pos.y -= 50
                body.setTransform(pos, 0F)
                body.isAwake = true
            }
        }
    }

    private fun createWaveBody(a: Float, b: Float, c: Float, d: Float): Body {
        val centerPoint = PointFPool.borrow(coordinateRectF.centerX(), coordinateRectF.centerY())
        val start = -500F
        val end = 500F
        val cal: (Float) -> Float = { x: Float ->
            a * sin(x / b + c) + d
        }
        wavePath = createPath(centerPoint, coordinateRectF, start, end, step = 1F, cal)
        val pointArray = path2Array(wavePath!!, 1F)
        pointArray.forEachIndexed { index, fl ->
            pointArray[index] = fl.toBox2D()
        }

        val pointTest = arrayOf(0F, height / 2F, width.toFloat(), height / 2F)
        pointTest.forEachIndexed { index, fl ->
            pointTest[index] = fl.toBox2D()
        }

        val shape = ChainShape()
        //shape.createChain(pointTest.toFloatArray())
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

    private fun testContactListener() {
        val listener = object : ContactListener {
            override fun beginContact(contact: Contact) {
                val posA = contact.fixtureA.body.position
                val posB = contact.fixtureB.body.position
                contact.worldManifold
                Log.i("ayy", "beginContact posA: $posA  posB :$posB")
            }

            override fun endContact(contact: Contact) {
                val posA = contact.fixtureA.body.position
                val posB = contact.fixtureB.body.position
                Log.i("ayy", "endContact posA: $posA  posB :$posB")
            }

            override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
            }

            override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
            }
        }
        world.setContactListener(listener)
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
        bodyDef.position.set(
            (width / 2 + 100).toFloat().toBox2D(),
            10.toFloat().toBox2D()
        )
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

    private fun calSegmentAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        if (x1 == x2) return Math.PI.toFloat() / 2
        val k = (y2 - y1) / (x2 - x1)
        return atan(k.toDouble()).toFloat()
    }

    private fun testPath(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        canvas.drawPath(wavePath!!, paint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制边界
        doPhysicalStep()
        drawBoundary(canvas)
        drawBall(canvas)
        testPath(canvas)
        postInvalidate()
    }
}

fun Float.toBox2D(): Float {
    return this / 50
}

fun Float.toPixels(): Float {
    return this * 50
}