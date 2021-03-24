package com.ai.zeld.business.world

import android.content.Context
import com.ai.zeld.common.service.world.IWorld

class World : IWorld {
    private lateinit var context: Context
    private lateinit var sectionControl: SectionControl

    fun initWorld(context: Context) {
        this.context = context
        sectionControl = SectionControl(context)
        sectionControl.init()
    }

    override fun getContext(): Context {
        return context
    }

    override fun gotoNextSection() {


    }

    override fun getCurrentSectionId(): Int {
        return 1
    }

    override fun getCurrentSectionName(): String {
        return ""
    }

    override fun getSectionId(name: String): Int {
        return -1
    }

    override fun gotoSection(id: Int) {

    }

    override fun gotoSection(name: String) {

    }

}