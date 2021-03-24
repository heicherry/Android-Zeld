package com.ai.zeld

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ai.zeld.business.world.World
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.claymore.load


class FullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        (IWorld::class.java.load() as World).initWorld(this)
        setContentView(R.layout.activity_fullscreen)
    }
}