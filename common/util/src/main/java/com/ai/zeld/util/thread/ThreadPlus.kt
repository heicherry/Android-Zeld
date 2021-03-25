package com.ai.zeld.util.thread

import android.os.Handler
import android.os.Looper

object ThreadPlus {
    val mainHandler = Handler(Looper.getMainLooper())
}