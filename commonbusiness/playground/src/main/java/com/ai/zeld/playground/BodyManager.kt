package com.ai.zeld.playground

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import com.ai.zeld.util.app.App
import com.badlogic.gdx.physics.box2d.World

class BodyManager(val world: World, internal val updateCallback: () -> Unit) {
    private val context = App.application
    val allBody = mutableListOf<Body>()

    inline fun <reified T : Body> createBody(type: BodyType, rectF: RectF, bitmap: Bitmap?): T {
        val param = mutableListOf(Bitmap::class.java, RectF::class.java).toTypedArray()
        val construct = T::class.java.getConstructor(*param)
        construct.isAccessible = true
        val body = construct.newInstance(bitmap, rectF)
        body.bodyType = type
        body.bodyManager = this
        body.initBody(world)
        allBody.add(body)
        return body
    }

    inline fun <reified T : Body> createBody(
        type: BodyType,
        center: PointF,
        bitmap: Bitmap,
    ): T {
        val rectF = RectF(
            center.x - bitmap.width / 2,
            center.y - bitmap.height / 2,
            center.x + bitmap.width / 2,
            center.y + bitmap.height / 2
        )
        return createBody(type, rectF, bitmap)
    }

    fun createBody(
        type: BodyType,
        rectF: RectF
    ): Body {
        return createBody(type, rectF, null)
    }

    fun startPlay() {
        allBody.filter { it.isAlive }.forEach { it.startPlay() }
    }

    fun draw(canvas: Canvas) {
        allBody.forEach {
            it.draw(canvas)
        }
    }

    enum class BodyType {
        BARRIER, HERO, COIN, OTHERS
    }

    fun step() {
        collisionCheck()
        updateCallback.invoke()
    }

    private fun collisionCheck() {
        val all = mutableListOf<Body>()
        all.addAll(allBody)
        val aliveMap = mutableMapOf<Body, Boolean>()
        all.forEach {
            aliveMap[it] = it.isAlive
        }
        all.forEach { first ->
            if (aliveMap[first]!!) {
                val collisionBody = mutableListOf<Body>()
                all.forEach { second ->
                    if (second != first && aliveMap[second]!!) {
                        if (first.rectF.contains(second.rectF)
                            || second.rectF.contains(first.rectF)
                            || RectF.intersects(first.rectF, second.rectF)
                        ) {
                            collisionBody.add(second)
                        }
                    }
                }
                if (collisionBody.size > 0) {
                    first.onCollision(collisionBody)
                }
            }
        }
    }
}