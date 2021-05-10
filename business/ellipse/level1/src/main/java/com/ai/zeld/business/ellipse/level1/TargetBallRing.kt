package com.ai.zeld.business.ellipse.level1

import android.graphics.*
import com.ai.zeld.playground.Body
import com.ai.zeld.util.center
import com.ai.zeld.util.containRectF
import com.ai.zeld.util.firstPointF
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.moveOrLineTo
import com.ai.zeld.util.path.path2Array
import com.ai.zeld.util.realPos

class TargetBallRing(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    // 轨迹信息
    var floatArray: FloatArray? = null
    var path: Path? = null

    // 绘制相关

    private var cal: TriangleFunction? = null
    var containRectF: RectF? = null

    fun setFunctionCal(cal: TriangleFunction) {
        this.cal = cal
        val start =
            -(stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_margin_left))
        val end =
            stage.getCenterPointF().x - resources.getDimension(R.dimen.ellipse_level1_margin_right)
        val upPath = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            start,
            end,
            step = 3F,
            { cal.cal(it, true) }
        )

        path = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            end,
            start,
            step = -3F,
            { cal.cal(it, false) },
            upPath
        )

        floatArray = path2Array(path!!, 1F)
        containRectF = floatArray?.containRectF()
        val firstPointF = floatArray!!.firstPointF()
        path!!.moveOrLineTo(firstPointF.x, firstPointF.y)
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        paint.color = Color.YELLOW
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        path?.let { canvas.drawPath(it, paint) }
        bitmap?.let { bitmap ->
            containRectF?.let { container ->
                canvas.drawBitmap(bitmap, null, bitmap.realPos(container.center()), paint)
            }
        }
    }
}
