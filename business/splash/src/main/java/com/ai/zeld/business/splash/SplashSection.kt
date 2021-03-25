package com.ai.zeld.business.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import kotlinx.android.synthetic.main.splash_main.*

@Section(SectionConfig.SPLASH)
class SplashSection : BaseSection() {
    override fun onPreload() {
        // 这里不能做任何事情。
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bar.progress = 30
    }

    override fun onForeplayShow() {

    }

    override fun onDinnerShow() {
    }
}