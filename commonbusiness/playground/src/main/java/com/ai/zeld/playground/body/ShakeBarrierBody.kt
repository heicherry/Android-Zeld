package com.ai.zeld.playground.body

import android.animation.ValueAnimator
import android.graphics.*
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import com.ai.zeld.playground.Body
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.R
import com.ai.zeld.util.*
import com.badlogic.gdx.physics.box2d.World

open class ShakeBarrierBody(bitmap: Bitmap, rectF: RectF) : BarrierBody(bitmap, rectF) {

    private val bombBitmap = R.drawable.playground_bomb.idToBitmap()
    private var bombRectF = RectF()
    private var isBombing = false
    private var currentRotate: Float = 0F
    private var currentScale: Float = 0.3F
    private var selfBombAnimator: ValueAnimator? = null
    private var blockBombAnimator: ValueAnimator? = null
    private var scaleAnimator: ValueAnimator? = null


    override fun initBody(world: World) {
        super.initBody(world)
        postInMainDelay(500) {
            doShake()
        }
    }

    override fun onCollision(allCollisionBody: List<Body>) {
        if (allCollisionBody.count { it.bodyType == BodyManager.BodyType.HERO } > 0) {
            switchState(State.BLOCK_BOMB)
        }
    }

    private fun doBomb(): ValueAnimator {
        isBombing = true
        val originRectF = bombRectF
        val animator = ValueAnimator.ofFloat(0.1F, 1F)
        animator.apply {
            duration = 300
            addUpdateListener {
                bombRectF = originRectF.scale(it.animatedValue as Float)
            }
            interpolator = BounceInterpolator()
        }.start()
        return animator
    }

    private fun doShake(): ValueAnimator {
        val animator = ValueAnimator.ofFloat(0F, -30F, 0F, 30F, 0F)
        animator.apply {
            duration = 2000
            addUpdateListener {
                currentRotate = it.animatedValue as Float
            }
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }
        animator.start()
        return animator
    }

    private fun doScaleAnimate(): ValueAnimator {
        val animator = ValueAnimator.ofFloat(0.3F, 1F)
        animator.apply {
            duration = 5000
            addUpdateListener {
                currentScale = it.animatedValue as Float
            }
            interpolator = LinearInterpolator()
            startDelay = 1000
        }
        animator.start()
        return animator
    }

    override fun startPlay() {
        super.startPlay()
        switchState(State.PREHEAT)
    }

    override fun getCurrentPos(): RectF {
        return if (currentState == State.SELF_BOMB) {
            bombRectF
        } else {
            rectF.scale(currentScale)
        }
    }

    override fun draw(canvas: Canvas) {
        when (currentState) {
            State.READY, State.PREHEAT -> {
                val currentPos = getCurrentPos()
                paint.color = Color.RED
                canvas.save()
                canvas.rotate(currentRotate, currentPos.centerX(), currentPos.centerY())
                canvas.drawBitmap(bitmap!!, null, currentPos, paint)
                canvas.restore()
            }
            State.SELF_BOMB -> {
                canvas.drawBitmap(bombBitmap, null, bombRectF, paint)
            }
            State.DEAD -> {
                // do nothing
            }
            State.BLOCK_BOMB -> {
                canvas.drawBitmap(bombBitmap, null, bombRectF, paint)
            }
            State.BLOCK_SUCCEED -> {
                canvas.drawBitmap(bombBitmap, null, bombRectF, paint)
            }
        }
    }

    private var currentState: State = State.READY

    private fun switchState(newState: State) {
        currentState = newState
        when (newState) {
            State.PREHEAT -> {
                scaleAnimator = doScaleAnimate()
                scaleAnimator?.doOnEndExt {
                    switchState(State.SELF_BOMB)
                }
            }
            State.SELF_BOMB -> {
                bombRectF.set(bombBitmap.realPos(PointF(rectF.centerX(), rectF.centerY())))
                selfBombAnimator = doBomb()
                selfBombAnimator?.doOnEndExt {
                    switchState(State.DEAD)
                }
            }
            State.BLOCK_BOMB -> {
                scaleAnimator?.cancel()
                selfBombAnimator?.cancel()

                isAlive = false
                bombRectF.set(bombBitmap.realPos(PointF(rectF.centerX(), rectF.centerY())))
                blockBombAnimator = doBomb()
                blockBombAnimator?.doOnEndExt {
                    switchState(State.BLOCK_SUCCEED)
                }
                playDeadMusic()
            }
        }
    }

    enum class State {
        READY, PREHEAT, SELF_BOMB, BLOCK_BOMB, DEAD, BLOCK_SUCCEED
    }
}