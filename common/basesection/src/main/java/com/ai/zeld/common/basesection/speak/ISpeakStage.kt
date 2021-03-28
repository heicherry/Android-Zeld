package com.ai.zeld.common.basesection.speak

interface ISpeakStage {
    fun girlSpeak(
        content: String,
        timeElapse: Long = -1,
        waitingForClick: (() -> Unit)?,
        onEnd: (() -> Unit)? = null
    )

    fun boySpeak(
        content: String,
        timeElapse: Long = -1,
        waitingForClick: (() -> Unit)?,
        onEnd: (() -> Unit)? = null
    )

    fun speak(
        prefix: String,
        content: String,
        timeElapse: Long = -1,
        waitingForClick: (() -> Unit)?,
        onEnd: (() -> Unit)? = null
    )
}