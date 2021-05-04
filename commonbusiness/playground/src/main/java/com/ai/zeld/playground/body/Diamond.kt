package com.ai.zeld.playground.body

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import com.ai.zeld.playground.Body

class Diamond(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {
    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        isAlive = false
    }

    override fun draw(canvas: Canvas) {
        if (isAlive) {
            super.draw(canvas)
        }
    }
}