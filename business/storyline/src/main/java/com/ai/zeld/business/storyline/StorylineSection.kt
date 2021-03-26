package com.ai.zeld.business.storyline

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig

@Section(SectionConfig.STORYLINE)
class StorylineSection : BaseSection() {
    override fun onPreload() {
        super.onPreload()
        Thread.sleep(3000)
    }


    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.storyline_main, null)
    }
}