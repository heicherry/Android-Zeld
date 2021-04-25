package com.ai.zeld.business.ellipse.level1.bodys

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.RectF
import com.ai.zeld.util.convertToBody
import com.ai.zeld.util.realPos
import com.ai.zeld.util.toPointF
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World

class JumpingBarrier(bitmap: Bitmap, rectF: RectF) : BarrierBody(bitmap, rectF) {
    lateinit var box2DBody: Body
    override fun initBody(world: World) {
        super.initBody(world)
        box2DBody = rectF.convertToBody(world, BodyDef.BodyType.DynamicBody, true)
    }

    override fun getCurrentPos(): RectF {

        return bitmap.realPos(box2DBody.position.toPointF())
    }
}