package com.ai.zeld.util.thread

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AndroidRuntimeException

object ThreadPlus {
    val mainHandler = Handler(Looper.getMainLooper())
    var preloadThread: HandlerThread? = null
    lateinit var preloadHandler: Handler

    init {
        initPreloadHandlerIfNeed()
    }

    private  fun initPreloadHandlerIfNeed(){
        if (preloadThread == null) {
            preloadThread = HandlerThread("app_preload")
            preloadThread!!.start()
            preloadHandler = Handler(preloadThread!!.looper)
        }
    }
}

fun checkMainThread(msg: String? = null) {
    if (Thread.currentThread().id != Looper.getMainLooper().thread.id) {
        throw AndroidRuntimeException(msg)
    }
}

