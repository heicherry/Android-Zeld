package com.ai.zeld.business.parabola.level1

import android.graphics.PointF
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.body.Coin
import com.ai.zeld.playground.body.VirusBody
import com.ai.zeld.util.idToBitmap


@Section(SectionConfig.FLY_HIGH)
class ParabolaLevel3Section : ParabolaLevel1Section() {
    override fun initMonsters() {
        createBarrier(240F, 1000F, R.drawable.playground_mine)
        createBarrier(350F, 790F, R.drawable.playground_mine)
        createBarrier(500F, 1300F, R.drawable.playground_mine)
        createBarrier(750F, 1100F, R.drawable.playground_mine)

        bodyManager.createBody<VirusBody>(
            BodyManager.BodyType.BARRIER,
            PointF(400F, 500F), R.drawable.playground_virus.idToBitmap()
        )

        bodyManager.createBody<VirusBody>(
            BodyManager.BodyType.BARRIER,
            PointF(750F, 200F), R.drawable.playground_virus.idToBitmap()
        )

        bodyManager.createBody<Coin>(
            BodyManager.BodyType.COIN,
            PointF(90F, 1500F),
            R.drawable.playground_coin_1.idToBitmap()
        )

        bodyManager.createBody<Coin>(
            BodyManager.BodyType.COIN,
            PointF(220F, 1200F),
            R.drawable.playground_coin_1.idToBitmap()
        )

        bodyManager.createBody<Coin>(
            BodyManager.BodyType.COIN,
            PointF(800F, 1500F),
            R.drawable.playground_coin_1.idToBitmap()
        )
    }
}