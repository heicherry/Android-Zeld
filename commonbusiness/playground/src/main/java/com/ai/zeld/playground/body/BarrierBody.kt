package com.ai.zeld.playground.body

import android.animation.ValueAnimator
import android.graphics.*
import android.view.animation.BounceInterpolator
import com.ai.zeld.common.media.MusicClip
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.R
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.realPos
import com.ai.zeld.util.scale

open class BarrierBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    private val bombBitmap = R.drawable.playground_bomb.idToBitmap()
    private var bombRectF = RectF()
    private var isBombing = false

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        if (allCollisionBody.count { it.bodyType == BodyManager.BodyType.HERO } > 0) {
            isAlive = false
            bombRectF.set(bombBitmap.realPos(PointF(rectF.centerX(), rectF.centerY())))
            doBomb()
            playDeadMusic()
        }
    }

    open fun playDeadMusic() {
        MusicClipsPlayerManager.play(MusicClip.BOMB)
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


    override fun draw(canvas: Canvas) {
        if (isAlive) {
            super.draw(canvas)
        } else {
            canvas.drawBitmap(bombBitmap, null, bombRectF, paint)
        }
    }
}