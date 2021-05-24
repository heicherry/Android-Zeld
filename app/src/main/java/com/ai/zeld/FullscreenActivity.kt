package com.ai.zeld

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ai.zeld.business.world.World
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.app.App
import com.ai.zeld.util.claymore.load
import com.hjq.toast.ToastUtils
import kotlin.system.exitProcess


class FullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initApp()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_fullscreen)
        (IWorld::class.java.load() as World).initWorld(this, R.id.main_container)
    }

    private fun initApp() {
        App.activity = this
        App.application = application
        ToastUtils.init(application)
    }

    override fun onDestroy() {
        super.onDestroy()
        exitProcess(0)
    }
}