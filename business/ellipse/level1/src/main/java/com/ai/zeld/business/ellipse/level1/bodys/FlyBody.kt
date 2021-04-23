package com.ai.zeld.business.ellipse.level1.bodys

import android.graphics.*
import android.util.Log
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
import com.ai.zeld.util.postInMainDelay

class FlyBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    // 轨迹信息
    private var floatArray: FloatArray? = null
    var path: Path? = null

    // 绘制相关
    private var index = 0

    private var cal: ((Float) -> Float)? = null

    fun setFunctionCal(cal: (Float) -> Float) {
        this.cal = cal
    }


    fun isEnd() = index >= floatArray?.size ?: Int.MAX_VALUE

    fun startFly() {
        if (isEnd() || cal == null) return
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
            cal!!
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
        postInvalidate()
        postInMainDelay(20) {
            run()
        }
    }

    override fun draw(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        path?.let { canvas.drawPath(it, paint) }
        super.draw(canvas)
    }

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        Log.i("ayy", "onCollision")
    }
}
