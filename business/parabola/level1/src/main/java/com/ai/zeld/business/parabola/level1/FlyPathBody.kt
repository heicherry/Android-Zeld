package com.ai.zeld.business.parabola.level1

import android.graphics.*
import android.util.Log
import android.view.View
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.Diamond
import com.ai.zeld.util.*
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
import kotlin.math.atan

class FlyPathBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    // 轨迹信息
    var floatArray: FloatArray? = null
    var path: Path? = null
    private var bandingView: View? = null
    private var runningIndex = -1
    private var startIndex = -1
    private var endIndex = -1
    private var flyListener: IFlyStateListener? = null
    private var isRunning = false

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
        floatArray = path2Array(path!!, 3F)
        postInvalidate()
    }

    fun bindView(view: View) {
        bandingView = view
    }

    fun setFlyListener(listener: IFlyStateListener) {
        flyListener = listener
    }

    override fun startPlay() {
        super.startPlay()
        calStartAndEnd()
        prepareRun()
        run()
    }

    private fun calStartAndEnd() {
        startRectF?.let {
            startIndex = calRectFLeavePoint(it)
        }
        endRectF?.let {
            endIndex = calRectFInPoint(it)
        }
    }

    private fun prepareRun() {
        runningIndex = -1
        if (startIndex == -1) {
            Log.e("ayy", "找不到起点呀！！！！")
            return
        }
        if (endIndex == -1) {
            Log.e("ayy", "找不到终点呀！！！！")
            return
        }
        if (startIndex >= endIndex) {
            Log.e("ayy", "坐标错误： startIndex:$startIndex  endIndex:$endIndex")
            return
        }
        runningIndex = startIndex
        isRunning = true
        flyListener?.onFlyStart()
    }

    private fun run() {
        if (!isRunning) return
        if (runningIndex != -1 && runningIndex < endIndex) {
            val currentPointF = floatArray?.point(runningIndex)
            val nextPointF = floatArray?.point(runningIndex + 1)
            if (currentPointF != null && nextPointF != null) {
                bandingView?.let { view ->
                    val originY = view.showRectF().center().y
                    if (originY >= currentPointF.y) {
                        val angle =
                            atan(((nextPointF.y - currentPointF.y) / (nextPointF.x - currentPointF.x)).toDouble()) / Math.PI * 180
                        view.rotation = (40F + angle).toFloat()
                        view.moveCenterTo(currentPointF)
                        rectF.set(view.showRectF().apply {
                            offset(currentPointF.x - centerX(), currentPointF.y - centerY())
                        })
                    }
                }
            }
            runningIndex += 2
            postInMainDelay(8) {
                run()
            }
        } else {
            isRunning = false
            if (runningIndex >= endIndex) {
                bandingView?.rotation = 0F
                flyListener?.onFlyEnd()
            }
        }
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        path?.let { canvas.drawPath(it, paint) }
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
            isRunning = false
            bandingView?.resetPos()
            postInvalidate()
        }
    }

    private fun calRectFLeavePoint(rectF: RectF): Int {
        val array = floatArray ?: return -1
        var isEnter = false
        var leavePointIndex = -1
        array.eachPoint { index, point ->
            if (leavePointIndex != -1) return@eachPoint
            if (!isEnter) {
                isEnter = rectF.contains(point.x, point.y)
            } else {
                if (!rectF.contains(point.x, point.y)) {
                    leavePointIndex = index
                }
            }
        }
        return leavePointIndex
    }

    private fun calRectFInPoint(rectF: RectF): Int {
        val array = floatArray ?: return -1
        var inIndex = -1
        array.eachPoint { index, point ->
            if (inIndex != -1) return@eachPoint
            if (rectF.contains(point.x, point.y)) {
                inIndex = index
            }
        }
        return inIndex
    }
}
