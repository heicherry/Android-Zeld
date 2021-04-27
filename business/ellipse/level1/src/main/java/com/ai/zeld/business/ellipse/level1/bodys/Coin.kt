package com.ai.zeld.business.ellipse.level1.bodys

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.*
import com.ai.zeld.common.media.MusicClip
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.util.app.App
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.postInMainDelay
import com.badlogic.gdx.physics.box2d.World

class Coin(rectF: RectF) : Body(null, rectF) {
    private val allBitmap = mutableListOf<Bitmap>()
    private var currentBitmap: Bitmap? = null

    override fun initBody(world: World) {
        super.initBody(world)
        initBitmapRes()
        playCoinAnimator()
    }

    private fun initBitmapRes() {
        for (i in 1..8) {
            val resId = App.application.resources.getIdentifier(
                "ellipse_level1_coin_$i",
                "drawable",
                App.application.packageName
            )
            allBitmap.add(resId.idToBitmap())
        }
    }

    private fun playCoinAnimator() {
        val animator = ValueAnimator.ofInt(1, 6)
        animator.apply {
            duration = 500
            addUpdateListener {
                currentBitmap = allBitmap[it.animatedValue as Int]
            }
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }
        postInMainDelay(1000) {
            animator.start()
        }
    }

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        if (allCollisionBody.filterIsInstance<FlyBody>().count() > 0) {
            isAlive = false
            MusicClipsPlayerManager.play(MusicClip.COIN)
        }
    }

    override fun draw(canvas: Canvas) {
        if (isAlive) {
            currentBitmap?.let {
                canvas.drawBitmap(it, null, rectF, paint)
            }
        }
    }
}