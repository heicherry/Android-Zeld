package com.ai.zeld.common.basesection.speak

interface ISpeakStage {
    fun girlSpeak(content: String, timeElapse: Long = -1, onEnd: (() -> Unit)? = null)
    fun boySpeak(content: String, timeElapse: Long = -1, onEnd: (() -> Unit)? = null)
    fun speak(content: String, timeElapse: Long = -1, onEnd: (() -> Unit)? = null)
}