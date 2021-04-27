package com.ai.zeld.common.media

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import com.ai.zeld.util.app.App
import com.ai.zeld.util.postInMainDelay

@SuppressLint("StaticFieldLeak")
object MusicClipsPlayerManager {
    private lateinit var context: Context
    private lateinit var soundPool: SoundPool

    // 能同时播放的最大声音数
    private const val MAX_STREAMS = 5

    private const val SOUND_SAT_ER_DA = 0

    fun init() {
        context = App.application
        soundPool = SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
    }

    fun load(resId: Int): Int {
        return soundPool.load(context, resId, 1)
    }

    fun unload(resId: Int) {
        soundPool.unload(resId)
    }

    private fun play(
        soundID: Int,
        leftVolume: Float,
        rightVolume: Float,
        priority: Int,
        loop: Int,
        rate: Float
    ): Int {
        return soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate)
    }

    fun play(soundID: Int): Int {
        return play(soundID, 1F, 1F, 0, 0, 1F)
    }

    fun loopPlay(soundID: Int): Int {
        return play(soundID, 1F, 1F, 0, -1, 1F)
    }

    fun stop(streamId: Int) {
        soundPool.stop(streamId)
    }


    fun pause(streamID: Int) {
        soundPool.pause(streamID)
    }

    fun resume(streamID: Int) {
        soundPool.resume(streamID)
    }

    fun pauseAll() {
        soundPool.autoPause()
    }

    fun resumeAll() {
        soundPool.autoResume()
    }

    fun release() {
        soundPool.release()
    }
}

enum class MusicClip {
    BOMB, DEAD
}