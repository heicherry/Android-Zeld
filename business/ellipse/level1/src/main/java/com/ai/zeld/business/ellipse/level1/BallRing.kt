package com.ai.zeld.business.ellipse.level1

import android.graphics.*
import android.util.Log
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.Diamond
import com.ai.zeld.util.containRectF
import com.ai.zeld.util.firstPointF
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.moveOrLineTo
import com.ai.zeld.util.path.path2Array

class BallRing(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    // 轨迹信息
    var floatArray: FloatArray? = null
    var path: Path? = null
    var hPath: Path? = null

    // 绘制相关
    private var index = 0

    private var cal: TriangleFunction? = null

    private var gameResultListener: IGameResult? = null
    private val allDiamonds = mutableListOf<Diamond>()
    private var containRectF: RectF? = null
    private var hPoint: PointF? = null

    fun setGameListener(listener: IGameResult) {
        gameResultListener = listener
    }

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
            { cal(it, true) }
        )

        path = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            end,
            start,
            step = -3F,
            { cal(it, false) },
            upPath
        )

        floatArray = path2Array(path!!, 1F)
        containRectF = floatArray?.containRectF()
        val firstPointF = floatArray!!.firstPointF()
        path!!.moveOrLineTo(firstPointF.x, firstPointF.y)
        postInvalidate()
    }

    fun setNearestH(start: PointF, end: PointF) {
        hPath = Path()
//        hPath?.moveOrLineTo(start.x, start.y)
//        hPath?.moveOrLineTo(end.x, end.y)
//        postInvalidate()
        hPoint = end
    }

    override fun startPlay() {
        super.startPlay()
        run()
    }

    private fun run() {

    }

    override fun draw(canvas: Canvas) {
        drawFloatingWave(canvas)
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        path?.let { canvas.drawPath(it, paint) }
        hPath?.let { canvas.drawPath(it, paint) }
        if (isAlive) {
            super.draw(canvas)
        }
    }

    private val ww = 100
    private val wh = 20
    private var dx = 0

    private fun drawFloatingWave(canvas: Canvas) {
        val container = containRectF ?: return
        val ellipsePath = path ?: return
        val h = hPoint?.y ?: return

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.GREEN
        val wavePath = Path()
        var x1 = -dx + container.left
        val y = h
        wavePath.moveTo(x1, y);
        while (x1 <= container.right) {
            wavePath.quadTo(x1 + ww / 4, y - wh, x1 + ww / 2, y);
            wavePath.quadTo(x1 + ww / 4 * 3, y + wh, x1 + ww, y);
            x1 += ww;
        }
        wavePath.moveOrLineTo(container.right, container.bottom)
        wavePath.moveOrLineTo(container.left, container.bottom)
        wavePath.close()
        wavePath.op(ellipsePath, Path.Op.INTERSECT)
        canvas.drawPath(wavePath, paint)

        dx += 2
        if (dx >= ww) dx = 0

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
}
