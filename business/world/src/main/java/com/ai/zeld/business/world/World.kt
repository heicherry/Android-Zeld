package com.ai.zeld.business.world

import android.app.Activity
import android.content.Context
import android.util.AndroidRuntimeException
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.ai.zeld.common.basesection.section.ISectionChangeListener
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.*
import com.ai.zeld.util.thread.ThreadPlus
import com.ai.zeld.util.thread.checkMainThread

class World : IWorld {
    private lateinit var context: Activity
    private lateinit var sectionCenter: SectionUnitCenter
    private var currentSectionId = 0
    private val sectionChangeListeners = ListenerWrap<ISectionChangeListener>()
    private var worldContainerViewId = 0
    private var isSectionSwitching = false

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
        val nextSectionId = sectionCenter.findNextSectionId(currentSectionId)
        if (-1 != nextSectionId) {
            switchSection(nextSectionId)
        }
    }

    override fun gotoNextSectionLater() {
        val nextSectionId = sectionCenter.findNextSectionId(currentSectionId)
        if (-1 != nextSectionId) {
            switchSectionLater(nextSectionId)
        }
    }

    override fun getCurrentSectionId(): Int {
        return 1
    }

    override fun preloadAllSection(progress: ((Float) -> Unit)?) {
        val allSectionId = sectionCenter.getAllSectionId()
        allSectionId.forEach {
            postInPreload {
                sectionCenter.getSectionById(it).onPreload()
                postInMain {
                    progress?.invoke((it + 1).toFloat() / allSectionId.size)
                }
            }
        }
        postInPreload {
            postInMain {
                ThreadPlus.preloadThread?.quitSafely()
                progress?.invoke(1F)
            }
        }
    }

    override fun setOnSectionChangeListener(listener: ISectionChangeListener) {
        sectionChangeListeners.addListener(listener)
    }

    override fun removeSectionChangeListener(listener: ISectionChangeListener) {
        sectionChangeListeners.removeLister(listener)
    }

    private fun switchSection(targetId: Int) {
        checkMainThread("section switch must do in main thread!")
        if (isSectionSwitching) {
            throw AndroidRuntimeException("current is switching!")
        }
        isSectionSwitching = true
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
        section.onSectionEnter()
        sectionChangeListeners.dispatchInvoke {
            it.onSectionChanged(
                lastSectionId,
                currentSectionId
            )
        }
        isSectionSwitching = false
    }

    private fun switchSectionLater(targetId: Int) {
        postInMain {
            switchSection(targetId)
        }
    }
}