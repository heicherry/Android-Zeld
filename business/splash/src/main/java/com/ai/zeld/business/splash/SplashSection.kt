package com.ai.zeld.business.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.media.BackgroundMusicPlayer
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.postInMainDelay
import com.badlogic.gdx.physics.box2d.Box2D
import kotlinx.android.synthetic.main.splash_main.*

@Section(SectionConfig.SPLASH, level = "", title = "")
class SplashSection : BaseSection() {
    override fun onPreload() {
        super.onPreload()
        Box2D.init()
        // 这里不能做任何事情。
    }

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.splash_main, null)
    }

    override fun onReset() {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateProgress(0F)
        IWorld::class.java.load().preloadAllSection({
            updateProgress(it)
        }) {
            onPreloadFinished()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgress(progress: Float) {
        bar.progress = (progress * 100).toInt()
        val progressHintPre = localContext.getString(R.string.splash_loading_hint)
        loading_hint.text = "$progressHintPre(${bar.progress}%)"
    }

    private fun onPreloadFinished() {
        postInMainDelay(1000) {
            IWorld::class.java.load().gotoNextSection()
        }
    }
}