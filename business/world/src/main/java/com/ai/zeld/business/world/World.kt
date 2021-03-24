package com.ai.zeld.business.world

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.ai.zeld.common.basesection.section.ISectionChangeListener
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.ListenerWrap
import com.ai.zeld.util.gone
import com.ai.zeld.util.visible

class World : IWorld {
    private lateinit var context: Activity
    private lateinit var sectionCenter: SectionUnitCenter
    private var currentSectionId = 0
    private val sectionChangeListeners = ListenerWrap<ISectionChangeListener>()
    private var worldContainerViewId = 0

    // 世界布局
    private lateinit var stageWorld: View
    private lateinit var speakerWorld: View

    fun initWorld(context: Activity, container: Int) {
        this.context = context
        worldContainerViewId = container
        sectionCenter = SectionUnitCenter(context)
        sectionCenter.init()
        currentSectionId = sectionCenter.getInitialSectionId()
        initStage()
        switchSection(currentSectionId)
    }

    private fun initStage() {
        val root = context.findViewById<FrameLayout>(worldContainerViewId)
        LayoutInflater.from(context).inflate(R.layout.world_base_layout, root, true)
        stageWorld = context.findViewById(R.id.stage)
        speakerWorld = context.findViewById(R.id.speaker)
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

    override fun preloadAllSection() {

    }

    override fun setOnSectionChangeListener(listener: ISectionChangeListener) {
        sectionChangeListeners.addListener(listener)
    }

    override fun removeSectionChangeListener(listener: ISectionChangeListener) {
        sectionChangeListeners.removeLister(listener)
    }

    private fun switchSection(targetId: Int) {
        sectionChangeListeners.dispatchInvoke { it.onSectionPreChange(currentSectionId, targetId) }
        val lastSectionId = currentSectionId

        if (targetId == sectionCenter.getInitialSectionId()) {
            speakerWorld.gone()
        } else {
            speakerWorld.visible()
        }

        val fragmentActivity = context as FragmentActivity

        val lastSection = sectionCenter.getSectionById(currentSectionId)
        lastSection.onExitSection()
        val section = sectionCenter.getSectionById(targetId)
        fragmentActivity.supportFragmentManager.beginTransaction().replace(R.id.stage, section)
            .commitNowAllowingStateLoss()
        sectionChangeListeners.dispatchInvoke {
            it.onSectionChanged(
                lastSectionId,
                currentSectionId
            )
        }
    }
}