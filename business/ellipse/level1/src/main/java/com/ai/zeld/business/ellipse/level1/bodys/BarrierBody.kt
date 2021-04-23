package com.ai.zeld.business.ellipse.level1.bodys

import android.animation.ValueAnimator
import android.graphics.*
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.realPos
import com.ai.zeld.util.scale

open class BarrierBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    private val bombBitmap = R.drawable.ellipse_level1_bomb.idToBitmap()
    private val bombRectF = RectF()
    private var isBombing = false

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        isAlive = false
        bombRectF.set(bombBitmap.realPos(PointF(rectF.centerX(), rectF.centerY())))
        doBomb()
    }

    private fun doBomb() {
        val originRectF = bombRectF
        val animator = ValueAnimator.ofFloat(0.1F, 1F)
        animator.apply {
            duration = 500
            addUpdateListener {
                bombRectF.set(originRectF.scale(it.animatedValue as Float))
                bodyManager.step()
            }
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