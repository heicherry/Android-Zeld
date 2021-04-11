package com.ai.zeld.business.world.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CoordinateSystemView : View {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private var paint = Paint()
    private var trialPaint = Paint()
    private var coordinateSystemRectF = RectF(0F, 0F, 500F, 500F)
    private var centerPointF =
        PointF(coordinateSystemRectF.centerX(), coordinateSystemRectF.centerY())

    private var leftPoint = PointF()
    private var rightPoint = PointF()
    private var topPoint = PointF()
    private var bottomPoint = PointF()

    private fun init() {
        paint.apply {
            color = Color.RED
            strokeWidth = 3F
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            pathEffect = DashPathEffect(floatArrayOf(1F, 10F), 0F)
        }

        trialPaint.apply {
            color = Color.RED
            strokeWidth = 3F
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        updatePoints()
    }

    private fun updatePoints() {
        leftPoint.apply {
            coordinateSystemRectF.let {
                x = it.left
                y = centerPointF.y
            }
        }
        rightPoint.apply {
            coordinateSystemRectF.let {
                x = it.right
                y = centerPointF.y
            }
        }
        topPoint.apply {
            coordinateSystemRectF.let {
                x = centerPointF.x
                y = it.top
            }
        }
        bottomPoint.apply {
            coordinateSystemRectF.let {
                x = centerPointF.x
                y = it.bottom
            }
        }
        invalidate()
    }

    private fun drawTrial(
        canvas: Canvas,
        fromX: Float,
        fromY: Float,
        toX: Float,
        toY: Float,
        height: Float,
        bottom: Float
    ) {
        val juli = Math.sqrt(
            ((toX - fromX) * (toX - fromX)
                    + (toY - fromY) * (toY - fromY)).toDouble()
        ).toFloat()
        val juliX = toX - fromX
        val juliY = toY - fromY
        val dianX = toX - height / juli * juliX
        val dianY = toY - height / juli * juliY

        val path = Path()
        path.moveTo(toX, toY)
        path.lineTo(
            dianX + bottom / juli * juliY, dianY - bottom / juli * juliX
        )
        path.moveTo(toX, toY)
        path.lineTo(
            dianX - bottom / juli * juliY, dianY + bottom / juli * juliX
        )
        canvas.drawPath(path, trialPaint)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(leftPoint.x, leftPoint.y, rightPoint.x, rightPoint.y, paint)
        canvas.drawLine(topPoint.x, topPoint.y, bottomPoint.x, bottomPoint.y, paint)
        drawTrial(canvas, leftPoint.x, leftPoint.y, rightPoint.x, rightPoint.y, 10F, 10F)
        drawTrial(canvas, topPoint.x, topPoint.y, bottomPoint.x, bottomPoint.y, 10F, 10F)
    }

    fun updateCoordinate(coordinate: RectF, centerPointF: PointF) {
        coordinateSystemRectF.set(coordinate)
        if (centerPointF != null) {
            this.centerPointF.set(centerPointF)
        } else {
            this.centerPointF.set(coordinateSystemRectF.centerX(), coordinateSystemRectF.centerY())
        }
        updatePoints()
    }
}