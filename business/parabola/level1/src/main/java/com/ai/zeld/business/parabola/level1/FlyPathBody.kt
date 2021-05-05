package com.ai.zeld.business.parabola.level1

import android.graphics.*
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.Diamond
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array

class FlyPathBody(bitmap: Bitmap, rectF: RectF) : Body(bitmap, rectF) {

    // 轨迹信息
    var floatArray: FloatArray? = null
    var path: Path? = null

    private var cal: ParabolaFunction? = null

    private var gameResultListener: IGameResult? = null
    private val allDiamonds = mutableListOf<Diamond>()

    fun setGameListener(listener: IGameResult) {
        gameResultListener = listener
    }

    fun setFunctionCal(cal: ParabolaFunction) {
        this.cal = cal
        val start =
            -(stage.getCenterPointF().x - resources.getDimension(R.dimen.parabola_level1_margin_left))
        val end =
            stage.getCenterPointF().x - resources.getDimension(R.dimen.parabola_level1_margin_right)
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
