package com.ai.zeld.common.basesection.section

import androidx.annotation.Keep

@Keep
object SectionConfig {
    const val SPLASH = 0

    const val STORYLINE =1

    // 隔岸表白
    const val FLY_MIN = 10//SPLASH + 1
    const val FLY_MIDDLE = 20//FLY_MIN + 1
    const val FLY_HIGH = 30//FLY_MIDDLE + 1

    // 完璧归赵
    const val ELLIPSE_MIN = 40
    const val ELLIPSE_MIDDLE = 50
    const val ELLIPSE_HIGH = 60

    // 英雄不能飞
    const val WAVE_MIN = 70
    const val WAVE_MIDDLE = 80
    const val WAVE_HIGH = 90
}

object SectionTitle {
    const val PARABOLA = "抛物线"
    const val ELLIPSE = "椭圆"
    const val WAVE = "三角函数"
}

object SectionLevel {
    const val EASY = "easy"
    const val MIDDLE = "middle"
    const val HARD = "hard"
}