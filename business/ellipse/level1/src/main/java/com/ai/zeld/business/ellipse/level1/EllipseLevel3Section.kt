package com.ai.zeld.business.ellipse.level1

import android.graphics.RectF
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.basesection.section.SectionLevel
import com.ai.zeld.common.basesection.section.SectionTitle
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.util.idToBitmap

@Section(SectionConfig.ELLIPSE_HIGH,  title = SectionTitle.ELLIPSE, level = SectionLevel.HARD)
class EllipseLevel3Section : EllipseLevel1Section() {

    override fun initTargetBody() {
        targetBody = bodyManager.createBody(
            BodyManager.BodyType.OTHERS,
            RectF(),
            R.drawable.playground_rose.idToBitmap()
        )
        targetBody.setFunctionCal(TriangleFunction(EllipseData(300F, 80F, 300F, 200F)))
    }

    override fun initMonsters() {
        createBarrier(700F, 1500F, R.drawable.playground_mine)
        createBarrier(600F, 1000F, R.drawable.playground_mine)
        createBarrier(800F, 800F, R.drawable.playground_mine)

        createBarrier(300F, 1200F, R.drawable.playground_mine)
        //TODO:地雷显示不全
        createBarrier(450F, 1450F, R.drawable.playground_mine)
    }

    override fun initFunctionControlPanel() {
        super.initFunctionControlPanel()
        functionControlView.reset(-288, -19, 100, 150)
    }
}