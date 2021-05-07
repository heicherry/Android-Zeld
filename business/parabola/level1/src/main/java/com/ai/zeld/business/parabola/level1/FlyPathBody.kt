package com.ai.zeld.business.parabola.level1

import android.graphics.*
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.Diamond
import com.ai.zeld.util.eachPoint
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
import com.ai.zeld.util.point

class FlyPathBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    // 轨迹信息
    var floatArray: FloatArray? = null
    var path: Path? = null

    private var cal: ParabolaFunction? = null

    private var gameResultListener: IGameResult? = null
    private val allDiamonds = mutableListOf<Diamond>()
    private var startRectF: RectF? = null
    private var endRectF: RectF? = null

    fun setGameListener(listener: IGameResult) {
        gameResultListener = listener
    }

    fun setStartAndEndRectF(start: RectF, end: RectF) {
        startRectF = start
        endRectF = end
    }

    fun setFunctionCal(cal: ParabolaFunction) {
        this.cal = cal
        val start = -stage.getCenterPointF().x
        val end = stage.getCenterPointF().x
        path = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            start,
            end,
            step = 3F,
            cal
        )
        floatArray = path2Array(path!!, 1F)
        postInvalidate()
    }

    override fun startPlay() {
        super.startPlay()
        run()
    }

    private fun run() {

    }

    override fun draw(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        path?.let { canvas.drawPath(it, paint) }

        startRectF?.let {
            canvas.drawRect(it, paint)
            val pointIndex = calRectFLeavePoint(it)
            if (pointIndex == -1) return@let
            val point = floatArray!!.point(pointIndex)
            canvas.drawCircle(point.x, point.y, 10F, paint)
        }
        endRectF?.let { canvas.drawRect(it, paint) }


    }

    override fun onCollision(allCollisionBody: List<Body>) {
        super.onCollision(allCollisionBody)
        allCollisionBody.filterIsInstance<Diamond>().forEach {
            if (!allDiamonds.contains(it)) {
                allDiamonds.add(it)
            }
        }
        if (allCollisionBody.count { it.bodyType == BodyManager.BodyType.BARRIER } > 0) {
            isAlive = false
            gameResultListener?.onFailed()
        }
    }

    private fun calRectFLeavePoint(rectF: RectF): Int {
        val array = floatArray ?: return -1
        var isEnter = false
        var leavePointIndex = -1
        array.eachPoint { index, point ->
            if (!isEnter) {
                isEnter = rectF.contains(point.x, point.y)
            } else {
                if (!rectF.contains(point.x, point.y)) {
                    leavePointIndex = index
                    return@eachPoint
                }
            }
        }
        return leavePointIndex
    }

}
