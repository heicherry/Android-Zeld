package com.ai.zeld.util

import android.graphics.RectF
import com.ai.zeld.util.path.createPath
import com.ai.zeld.util.path.path2Array
import com.badlogic.gdx.physics.box2d.*


fun Float.toBox2D(): Float {
    return this / 50
}

fun Float.toPixels(): Float {
    return this * 50
}

fun RectF.convertToBody(
    world: World,
    type: BodyDef.BodyType,
    isCircle: Boolean = false,
    friction: Float = 0.5F
): Body {
    val bodyDef = BodyDef()
    bodyDef.type = type
    val box: Shape
    if (isCircle) {
        box = CircleShape()
        box.radius = (width() / 2).toBox2D()
    } else {
        box = PolygonShape()
        box.setAsBox(width().toBox2D(), height().toBox2D())
    }

    val fixtureDef = FixtureDef().apply {
        shape = box
        density = 0.5f
        this.friction = friction
        restitution = 0.98f
    }
    bodyDef.position.set(centerX().toBox2D(), centerY().toBox2D())
    val body = world.createBody(bodyDef)
    body.createFixture(fixtureDef)
    return body
}

fun World.stepExt() {
    step(Box2dConfig.DT, Box2dConfig.VELOCITY_ITERATIONS, Box2dConfig.TIMES_ITERATIONS)
}

typealias Box2dBody = Body


fun FloatArray.createWaveBody(world: World, type: BodyDef.BodyType): Body {
    val box2dArray = FloatArray(size)
    forEachIndexed { index, fl ->
        box2dArray[index] = fl.toBox2D()
    }

    val shape = ChainShape()
    shape.createChain(box2dArray)

    val bodyDef = BodyDef()
    bodyDef.type = BodyDef.BodyType.StaticBody

    val fixtureDef = FixtureDef().apply {
        this.shape = shape
        density = 0.5f
        friction = 0.5f
        restitution = 0.98f
    }

    bodyDef.position.set(0F, 0F)
    val body = world.createBody(bodyDef)
    body.createFixture(fixtureDef)
    return body
}