package com.ai.zeld.business.ellipse.level1

import android.graphics.RectF
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.basesection.section.SectionLevel
import com.ai.zeld.common.basesection.section.SectionTitle
import com.ai.zeld.playground.BodyManager
import com.ai.zeld.util.idToBitmap

@Section(SectionConfig.ELLIPSE_MIDDLE,  title = SectionTitle.ELLIPSE, level = SectionLevel.MIDDLE)
class EllipseLevel2Section : EllipseLevel1Section() {

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
    }
}