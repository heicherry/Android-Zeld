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
import com.ai.zeld.util.realPos
import com.ai.zeld.util.scale

open class BarrierBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    private val bombBitmap = R.drawable.ellipse_level1_bomb.idToBitmap()
    private var bombRectF = RectF()
    private var isBombing = false

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        isAlive = false
        bombRectF.set(bombBitmap.realPos(PointF(rectF.centerX(), rectF.centerY())))
        doBomb()
        playDeadMusic()
    }

    open fun playDeadMusic(){
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