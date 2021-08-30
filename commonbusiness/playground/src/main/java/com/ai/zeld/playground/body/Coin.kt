package com.ai.zeld.playground.body

import android.animation.ValueAnimator
import android.graphics.*
import com.ai.zeld.common.media.MusicClip
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.util.app.App
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.postInMainDelay
import com.badlogic.gdx.physics.box2d.World

class Coin(rectF: RectF) : Body(null, rectF) {
    constructor(bitmap: Bitmap?, rectF: RectF) : this(rectF)

    private val allBitmap = mutableListOf<Bitmap>()
    private var currentBitmap: Bitmap? = null
    private var animator:ValueAnimator?= null

    override fun initBody(world: World) {
        super.initBody(world)
        initBitmapRes()
        playCoinAnimator()
    }

    private fun initBitmapRes() {
        for (i in 1..8) {
            val resId = App.application.resources.getIdentifier(
                "playground_coin_$i",
                "drawable",
                App.application.packageName
            )
            allBitmap.add(resId.idToBitmap())
        }
    }

    private fun playCoinAnimator() {
        animator = ValueAnimator.ofInt(1, 6)
        animator?.apply {
            duration = 500
            addUpdateListener {
                currentBitmap = allBitmap[it.animatedValue as Int]
            }
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }
        postInMainDelay(1000) {
            if(animator?.isRunning != true) {
                animator?.start()
            }
        }
    }

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        if (allCollisionBody.count { it.bodyType == BodyManager.BodyType.HERO } > 0) {
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

    override fun onDestroy() {
        super.onDestroy()
        animator?.cancel()
    }
}