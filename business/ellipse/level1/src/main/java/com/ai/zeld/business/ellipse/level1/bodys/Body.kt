package com.ai.zeld.business.ellipse.level1.bodys

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.util.app.App
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.convertToBody
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World

open class Body(
    val bitmap: Bitmap,
    var rectF: RectF
) {
    // 世界相关
    protected val stage = IStage::class.java.load()
    protected val paint = Paint()
    protected val context = App.application
    protected val resources: Resources = context.resources
    protected var world: World? = null
    var isAlive = true

    internal lateinit var bodyManager: BodyManager

    init {
        paint.apply {
            isAntiAlias = true
        }
    }

    open fun initBody(world: World) {
        this.world = world
    }

    open fun getCurrentPos() = rectF

    open fun draw(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        canvas.drawBitmap(bitmap, null, getCurrentPos(), paint)
    }

    fun postInvalidate() {
        bodyManager.step()
    }

    open fun onCollision(allCollisionBody: List<Body>) {

    }
}