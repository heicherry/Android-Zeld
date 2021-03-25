package com.ai.zeld.business.storyline

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.storyline_main, container, false)
    }

    override fun onForeplayShow() {
    }

    override fun onDinnerShow() {
    }
}