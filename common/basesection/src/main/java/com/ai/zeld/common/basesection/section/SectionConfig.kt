package com.ai.zeld.common.basesection.section

import androidx.annotation.Keep

@Keep
object SectionConfig {
    const val SPLASH = 0
    const val HERO_CAN_NOT_FLY = SPLASH + 1

    const val STORYLINE = HERO_CAN_NOT_FLY + 1
    //const val STORYLINE = SPLASH + 1

    // 飞跃火山
    const val LEAP_FIRE = STORYLINE + 1
    const val LEAP_FIRE_1 = LEAP_FIRE + 1
    const val LEAP_FIRE_2 = LEAP_FIRE_1 + 1

    // 砖石情缘
    const val MASONRY_LOVE = LEAP_FIRE_2 + 1
    const val MASONRY_LOVE_1 = MASONRY_LOVE + 1

    // 英雄不能飞
    //const val HERO_CAN_NOT_FLY = MASONRY_LOVE_1 + 1
}