package com.ai.zeld.business.ellipse.level1

import android.graphics.*
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.Diamond
import com.ai.zeld.util.firstPointF
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.moveOrLineTo
import com.ai.zeld.util.path.path2Array
import com.ai.zeld.util.postInMainDelay

class BallRing(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    // 轨迹信息
    var floatArray: FloatArray? = null
    var path: Path? = null

    // 绘制相关
    private var index = 0

    private var cal: ((Float) -> Float)? = null

    private var gameResultListener: IGameResult? = null
    private val allDiamonds = mutableListOf<Diamond>()

    fun setGameListener(listener: IGameResult) {
        gameResultListener = listener
    }

    fun setFunctionCal(cal: (Float) -> Float) {
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
            cal
        )

        path = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            end,
            start,
            step = -3F,
            { -cal(it) },
            upPath
        )

        floatArray = path2Array(path!!, 1F)
        val firstPointF = floatArray!!.firstPointF()
        path!!.moveOrLineTo(firstPointF.x, firstPointF.y)
        postInvalidate()
    }

    override fun startPlay() {
        super.startPlay()
        run()
    }

    private fun run(){

    }

    override fun draw(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        path?.let { canvas.drawPath(it, paint) }
        if (isAlive) {
            super.draw(canvas)
        }
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
