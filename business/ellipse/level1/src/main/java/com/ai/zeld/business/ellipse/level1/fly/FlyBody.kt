package com.ai.zeld.business.ellipse.level1.fly

import android.content.Context
import android.graphics.*
import com.ai.zeld.business.ellipse.level1.TriangleFunction
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
import com.ai.zeld.util.postInMainDelay

class FlyBody(
    val context: Context,
    val bitmap: Bitmap,
    val cal: TriangleFunction,
    private val updateCallback: () -> Unit
) {
    // 世界相关
    private val stage = IStage::class.java.load()

    // 轨迹信息
    var rectF = RectF()
    private var floatArray: FloatArray? = null
    var path: Path? = null

    // 绘制相关
    private var index = 0
    private val paint = Paint()

    init {
        paint.apply {
            isAntiAlias = true
        }
    }

    fun isEnd() = index >= floatArray?.size ?: Int.MAX_VALUE

    fun startFly() {
        if (isEnd()) return
        val resources = context.resources
        val start =
            -(stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_wave_margin_left))
        val end =
            stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_wave_margin_right)
        path = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            start,
            end,
            step = 3F,
            cal
        )
        floatArray = path2Array(path!!, 1F)
        run()
    }

    private fun run() {
        if (isEnd()) return
        val array = floatArray ?: return
        val point = PointF(array[index], array[index + 1])
        rectF.set(
            point.x - bitmap.width / 2,
            point.y - bitmap.height / 2,
            point.x + bitmap.width / 2,
            point.y + bitmap.height / 2
        )
        index += 6
        updateCallback.invoke()
        postInMainDelay(20) {
            run()
        }
    }

    fun draw(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        path?.let { canvas.drawPath(it, paint) }
        canvas.drawBitmap(bitmap, null, rectF, paint)
    }
}
