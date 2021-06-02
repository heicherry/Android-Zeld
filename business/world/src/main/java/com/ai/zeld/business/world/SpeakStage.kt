package com.ai.zeld.business.world

import com.ai.zeld.business.world.views.HorseTextView
import com.ai.zeld.common.basesection.speak.ISpeakStage

class SpeakStage(private val speakStage: HorseTextView) : ISpeakStage {
    override fun girlSpeak(
        content: String,
        timeElapse: Long,
        waitingForClick: (() -> Unit)?,
        onEnd: (() -> Unit)?
    ) {
        speakStage.update(content, "女英雄:", onEnd, waitingForClick, 200L, timeElapse)
    }

    override fun boySpeak(
        content: String,
        timeElapse: Long,
        waitingForClick: (() -> Unit)?,
        onEnd: (() -> Unit)?
    ) {
        speakStage.update(content, "男英雄:", onEnd, waitingForClick, 200L, timeElapse)
    }

    override fun speak(
        prefix: String,
        content: String,
        timeElapse: Long,
        waitingForClick: (() -> Unit)?,
        onEnd: (() -> Unit)?
    ) {
        speakStage.update(content, prefix, onEnd, waitingForClick, 200L, timeElapse)
    }
}