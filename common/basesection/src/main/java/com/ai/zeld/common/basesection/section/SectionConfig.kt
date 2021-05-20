package com.ai.zeld.common.basesection.section

import androidx.annotation.Keep

@Keep
object SectionConfig {
    const val SPLASH = 0

//    const val STORYLINE = HERO_CAN_NOT_FLY + 1
    const val STORYLINE = 100//SPLASH + 1

    // 隔岸表白
    const val FLY_MIN = 10//SPLASH + 1
    const val FLY_MIDDLE = 200//FLY_MIN + 1
    const val FLY_HIGH = 1//FLY_MIDDLE + 1

    // 砖石情缘
//    const val MASONRY_LOVE = LEAP_FIRE_2 + 1
//    const val MASONRY_LOVE_1 = MASONRY_LOVE + 1

    // 英雄不能飞
    //const val HERO_CAN_NOT_FLY = MASONRY_LOVE_1 + 1
}