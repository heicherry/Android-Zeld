package com.ai.zeld.common.basesection.ext

import com.ai.zeld.common.basesection.speak.ISpeakStage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun ISpeakStage.speakWaitForClick(prefix: String, content: String, timeElapse: Long = -1) {
    return suspendCoroutine { ret ->
        speak(prefix, content, timeElapse, {
            ret.resume(Unit)
        }, null)
    }
}

//fun girlSpeak(content: String, timeElapse: Long = -1, onEnd: (() -> Unit)? = null)
//fun boySpeak(content: String, timeElapse: Long = -1, onEnd: (() -> Unit)? = null)
//fun speak(content: String, timeElapse: Long = -1, onEnd: (() -> Unit)? = null)