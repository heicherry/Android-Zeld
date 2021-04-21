package com.ai.zeld.business.ellipse.level1.bodys

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.util.app.App
import com.ai.zeld.util.claymore.load

open class Body(
    val bitmap: Bitmap,
    val rectF: RectF
) {
    // 世界相关
    protected val stage = IStage::class.java.load()
    protected val paint = Paint()
    protected val context = App.application
    protected val resources: Resources = context.resources

    internal lateinit var bodyManager: BodyManager

    init {
        paint.apply {
            isAntiAlias = true
        }
    }

    fun getCurrentPos() = rectF

    open fun draw(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        canvas.drawBitmap(bitmap, null, rectF, paint)
    }

    fun postInvalidate() {
        bodyManager.updateCallback.invoke()
    }
}