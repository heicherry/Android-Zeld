package com.ai.zeld.playground

interface IGameResult {
    fun onSucceed(diamondCount: Int)

    fun onFailed()
}