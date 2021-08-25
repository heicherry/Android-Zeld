package com.ai.zeld.business.parabola.level1

interface IFlyStateListener {
    fun onFlyStart()
    fun onFlyEnd()
    fun onError(isStartError: Boolean)
}