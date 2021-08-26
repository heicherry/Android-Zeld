package com.ai.zeld.common.media

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Vibrator
import android.util.Log
import com.ai.zeld.util.app.App
import com.ai.zeld.util.postInMainDelay

@SuppressLint("StaticFieldLeak")
object MusicClipsPlayerManager {
    private lateinit var context: Context
    private lateinit var soundPool: SoundPool

    // 能同时播放的最大声音数
    private const val MAX_STREAMS = 5

    private val resMap = mutableMapOf<MusicClip, Int>()

    private val vibrator = App.application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun init() {
        context = App.application
        soundPool = SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
        loadAllRes()
    }

    private fun loadAllRes() {
        loadResId(MusicClip.BOMB, R.raw.media_bomb)
        loadResId(MusicClip.DEAD, R.raw.media_dead)
        loadResId(MusicClip.COIN, R.raw.media_coin)
        loadResId(MusicClip.GO, R.raw.media_go)
        loadResId(MusicClip.SUCCEED, R.raw.media_succeed)
        loadResId(MusicClip.FAILED, R.raw.media_failed)

    }

    private fun loadResId(clip: MusicClip, resId: Int) {
        resMap[clip] = load(resId)
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

    fun play(clip: MusicClip) {
        resMap[clip]?.let {
            play(it)
            vibrator.vibrate(10)
        }
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
    BOMB, DEAD, COIN, GO,SUCCEED,FAILED
}