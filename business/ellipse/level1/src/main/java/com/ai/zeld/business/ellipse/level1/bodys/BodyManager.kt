package com.ai.zeld.business.ellipse.level1.bodys

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import com.ai.zeld.util.app.App

class BodyManager(internal val updateCallback: () -> Unit) {
    private val context = App.application
    private val allBody = mutableListOf<Body>()

    fun createBody(type: BodyType, rectF: RectF, bitmap: Bitmap): Body {
        val body = when (type) {
            BodyType.BARRIER -> BarrierBody(bitmap, rectF)
            BodyType.FLY -> FlyBody(bitmap, rectF)
        }.apply {
            bodyManager = this@BodyManager
        }
        allBody.add(body)
        return body
    }

    fun createBody(type: BodyType, center: PointF, bitmap: Bitmap): Body {
        val rectF = RectF(
            center.x - bitmap.width / 2,
            center.y - bitmap.height / 2,
            center.x + bitmap.width / 2,
            center.y + bitmap.height / 2
        )
        return createBody(type, rectF, bitmap)
    }

    fun draw(canvas: Canvas) {
        allBody.forEach {
            it.draw(canvas)
        }
    }


    enum class BodyType {
        BARRIER, FLY
    }
}