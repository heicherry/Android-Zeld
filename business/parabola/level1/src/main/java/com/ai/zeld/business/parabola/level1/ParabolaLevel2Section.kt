package com.ai.zeld.business.parabola.level1

import android.graphics.PointF
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.body.Coin
import com.ai.zeld.playground.body.VirusBody
import com.ai.zeld.util.idToBitmap


@Section(SectionConfig.FLY_MIDDLE)
class ParabolaLevel2Section : ParabolaLevel1Section() {

    override fun initMonsters() {
        createBarrier(340F, 1200F, R.drawable.playground_mine)
        createBarrier(600F, 900F, R.drawable.playground_mine)
        createBarrier(800F, 1300F, R.drawable.playground_mine)

        bodyManager.createBody<VirusBody>(
            BodyManager.BodyType.BARRIER,
            PointF(500F, 200F), R.drawable.playground_virus.idToBitmap()
        )
    }

}