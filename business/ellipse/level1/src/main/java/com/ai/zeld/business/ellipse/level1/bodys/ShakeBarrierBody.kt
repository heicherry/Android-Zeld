package com.ai.zeld.business.ellipse.level1.bodys

import android.animation.ValueAnimator
import android.graphics.*
import android.util.Log
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.media.MusicClip
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.postInMainDelay
import com.ai.zeld.util.realPos
import com.ai.zeld.util.scale
import com.badlogic.gdx.physics.box2d.World

open class ShakeBarrierBody(bitmap: Bitmap, rectF: RectF) : BarrierBody(bitmap, rectF) {

    private val bombBitmap = R.drawable.ellipse_level1_bomb.idToBitmap()
    private var bombRectF = RectF()
    private var isBombing = false
    private var currentRotate: Float = 0F

    override fun initBody(world: World) {
        super.initBody(world)
        postInMainDelay(500) {
            doShake()
        }
    }

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        isAlive = false
        bombRectF.set(bombBitmap.realPos(PointF(rectF.centerX(), rectF.centerY())))
        doBomb()
        playDeadMusic()
    }


    private fun doBomb() {
        val originRectF = bombRectF
        val animator = ValueAnimator.ofFloat(0.1F, 1F)
        animator.apply {
            duration = 300
            addUpdateListener {
                bombRectF = originRectF.scale(it.animatedValue as Float)
            }
            interpolator = BounceInterpolator()
        }.start()
    }

    private fun doShake() {
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
        if (isAlive) {
            canvas.save()
            val currentPos = getCurrentPos()
            canvas.rotate(currentRotate, currentPos.centerX(), currentPos.centerY())
            super.draw(canvas)
            canvas.restore()
        } else {
            canvas.drawBitmap(bombBitmap, null, bombRectF, paint)
        }
    }
}