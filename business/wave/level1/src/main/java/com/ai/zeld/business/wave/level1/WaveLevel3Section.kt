package com.ai.zeld.business.wave.level1

import android.graphics.PointF
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.playground.body.Coin
import com.ai.zeld.playground.body.ShakeBarrierBody
import com.ai.zeld.playground.body.VirusBody
import com.ai.zeld.util.idToBitmap

@Section(SectionConfig.WAVE_HIGH)
class WaveLevel3Section : WaveLevel1Section() {

    override fun initMonsters() {
        createBarrier(440F, 790F, R.drawable.playground_mine)
        createBarrier(600F, 1000F, R.drawable.playground_mine)

        bodyManager.createBody<VirusBody>(
            BodyManager.BodyType.BARRIER,
            PointF(700F, 200F), R.drawable.playground_virus.idToBitmap()
        )

        bodyManager.createBody<VirusBody>(
            BodyManager.BodyType.BARRIER,
            PointF(300F, 400F), R.drawable.playground_virus.idToBitmap()
        )

        bodyManager.createBody<ShakeBarrierBody>(
            BodyManager.BodyType.BARRIER,
            PointF(300F, 900F), R.drawable.playground_mine_2.idToBitmap()
        )

        bodyManager.createBody<Coin>(
            BodyManager.BodyType.COIN,
            PointF(900F, 900F),
            R.drawable.playground_coin_1.idToBitmap()
        )
    }
}