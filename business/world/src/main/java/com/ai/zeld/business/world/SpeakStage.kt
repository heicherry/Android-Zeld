package com.ai.zeld.business.world

import com.ai.zeld.business.world.views.HorseTextView
import com.ai.zeld.common.basesection.speak.ISpeakStage

class SpeakStage(private val speakStage: HorseTextView) : ISpeakStage {
    override fun girlSpeak(content: String, timeElapse: Long, onEnd: (() -> Unit)?) {
        speakStage.update(content, "女英雄:", onEnd, null, 1000L, timeElapse)
    }

    override fun boySpeak(content: String, timeElapse: Long, onEnd: (() -> Unit)?) {
        speakStage.update(content, "男英雄:", onEnd, null, 1000L, timeElapse)
    }

    override fun speak(content: String, timeElapse: Long, onEnd: (() -> Unit)?) {
        speakStage.update(content, "", onEnd, null, 1000L, timeElapse)
    }
}