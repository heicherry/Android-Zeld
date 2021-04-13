package com.ai.zeld.business.ellipse.level1

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat


class TouchView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private var lastX = 0
    private var lastY = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                //将点下的点的坐标保存
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                //计算出需要移动的距离
                val dx: Int = event.rawX.toInt() - lastX
                val dy: Int = event.rawY.toInt() - lastY
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                val parent = parent as View
                ViewCompat.offsetLeftAndRight(parent, dx)
                ViewCompat.offsetTopAndBottom(parent, dy)
            }
        }
        return true
    }
}