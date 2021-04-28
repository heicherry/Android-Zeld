package com.ai.zeld.business.ellipse.level1.bodys

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.media.MusicClip
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.util.*
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World

open class VirusBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    private lateinit var box2dBody: com.badlogic.gdx.physics.box2d.Body
    private var currentRotate = 0F

    override fun initBody(world: World) {
        super.initBody(world)
        box2dBody = rectF.convertToBody(world, BodyDef.BodyType.DynamicBody, true)
    }

    override fun getCurrentPos() = bitmap!!.realPos(box2dBody.position.toRealWorldPointF())

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        if (allCollisionBody.filterIsInstance<FlyBody>().isNotEmpty()) {
            isAlive = false
            world?.destroyBody(box2dBody)
            shake()
            MusicClipsPlayerManager.play(MusicClip.DEAD)
        }
    }

    private fun shake() {
        val animator = ValueAnimator.ofFloat(0F, -30F, 0F, 30F, 0F, -30F, 0F, 30F, 0F)
        animator.apply {
            duration = 2000
            addUpdateListener {
                currentRotate = it.animatedValue as Float
            }
            interpolator = LinearInterpolator()
        }
        animator.start()
    }

    override fun draw(canvas: Canvas) {
        rectF = getCurrentPos()
        if (isAlive) {
            super.draw(canvas)
        } else {
            canvas.save()
            val currentPos = getCurrentPos()
            canvas.rotate(currentRotate, currentPos.centerX(), currentPos.centerY())
            super.draw(canvas)
            canvas.restore()
        }
    }


}