package com.ai.zeld.business.wave.level1

import android.graphics.*
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.IGameResult
import com.ai.zeld.playground.body.Diamond
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

    private var gameResultListener: IGameResult? = null
    private val allDiamonds = mutableListOf<Diamond>()

    fun setGameListener(listener: IGameResult) {
        gameResultListener = listener
    }

    fun setFunctionCal(cal: (Float) -> Float) {
        this.cal = cal
        if (isEnd()) return
        val start =
            -(stage.getCenterPointF().x - resources.getDimension(R.dimen.wave_margin_left))
        val end =
            stage.getCenterPointF().x - resources.getDimension(R.dimen.wave_margin_right)
        path = createPath(
            stage.getCenterPointF(),
            stage.getCoordinateRect(),
            start,
            end,
            step = 3F,
            cal
        )
        floatArray = path2Array(path!!, 1F)
    }

    fun isEnd() = index >= floatArray?.size ?: Int.MAX_VALUE

    override fun startPlay() {
        super.startPlay()
        run()
    }

    private fun run() {
        if (isDestroy) return
        if (isEnd()) return
        val array = floatArray ?: return
        val point = PointF(array[index], array[index + 1])
        rectF.set(
            point.x - bitmap!!.width / 2,
            point.y - bitmap.height / 2,
            point.x + bitmap.width / 2,
            point.y + bitmap.height / 2
        )
        index += 6
        if (index >= array.size) {
            gameResultListener?.onSucceed(allDiamonds.size)
        }
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
