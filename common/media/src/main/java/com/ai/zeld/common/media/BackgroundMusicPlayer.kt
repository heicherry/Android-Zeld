package com.ai.zeld.common.media

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.ai.zeld.util.app.App

object BackgroundMusicPlayer {
    private lateinit var mediaPlayer: MediaPlayer

    fun init() {
        mediaPlayer = MediaPlayer.create(App.application, R.raw.media_saierda)
    }

    fun play() {
        Log.i("haha","start play")
        mediaPlayer.isLooping = true
        mediaPlayer.start()
//        mediaPlayer.setOnCompletionListener {
//            it.reset()
//            it.start()
//            it.isLooping = true
//        }
    }

    fun stop() {
        mediaPlayer.stop()
    }
}