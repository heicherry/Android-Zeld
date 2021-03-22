package com.ai.zeld.common.basesection.world

import android.util.Log
import com.ai.zeld.common.service.world.IWorld

class World : IWorld {
    override fun print(content: String) {
        Log.i("ayy","content: $content")
    }
}