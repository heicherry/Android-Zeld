package com.ai.zeld.business.ellipse.level1

import com.ai.zeld.business.ellipse.level1.bodys.BarrierBody

interface IGameResult {
    fun onSucceed(diamondCount: Int)

    fun onFailed()
}