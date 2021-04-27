package com.ai.zeld.common.media

import android.media.MediaPlayer
import com.ai.zeld.util.app.App

object BackgroundMusicPlayer {
    private lateinit var mediaPlayer: MediaPlayer

    fun init() {
        mediaPlayer = MediaPlayer.create(App.application, R.raw.media_saierda)
    }

    fun play() {
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.stop()
    }
}