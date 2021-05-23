package com.ai.zeld.business.world

import android.app.Activity
import android.content.Context
import android.util.AndroidRuntimeException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.ai.zeld.business.world.sectionstate.SectionStateCenter
import com.ai.zeld.business.world.views.HorseTextView
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.ISectionChangeListener
import com.ai.zeld.common.service.stage.IStage
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.*
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.thread.ThreadPlus
import com.ai.zeld.util.thread.checkMainThread

class World : IWorld {
    companion object {
        private const val TAG = "World"
    }

    private lateinit var context: Activity
    private lateinit var sectionCenter: SectionUnitCenter
    private val sectionStateCenter = SectionStateCenter()
    private var currentSectionId = 0
    private val sectionChangeListeners = ListenerWrap<ISectionChangeListener>()
    private var worldContainerViewId = 0
    private var isSectionSwitching = false

    // 世界布局
    private lateinit var stageWorldContainer: View
    private lateinit var speakerWorld: View
    private lateinit var speakStage: SpeakStage

    fun initWorld(context: Activity, container: Int) {
        this.context = context
        worldContainerViewId = container
        sectionCenter = SectionUnitCenter(context)
        sectionCenter.init()
        val initialId = sectionCenter.getInitialSectionId()
        currentSectionId = initialId
        sectionStateCenter.initInitialState(initialId)
        initStage()
        initSpeakStage()
        switchSection(currentSectionId)
    }

    private fun initStage() {
        val root = context.findViewById<FrameLayout>(worldContainerViewId)
        LayoutInflater.from(context).inflate(R.layout.world_base_layout, root, true)
        stageWorldContainer = context.findViewById(R.id.stage_container)
        (IStage::class.java.load() as Stage).init(stageWorldContainer)
        speakerWorld = context.findViewById(R.id.speaker)
    }

    private fun initSpeakStage() {
        val speakView = context.findViewById<HorseTextView>(R.id.speak_stage)
        speakStage = SpeakStage(speakView)
        sectionCenter.getAllSectionId().forEach {
            sectionCenter.getSectionById(it).speakStage = speakStage
        }
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

    override fun getCurrentSectionId() = currentSectionId

    override fun getAllSectionId() = sectionCenter.getAllSectionId()

    override fun getSectionById(id: Int) = sectionCenter.getSectionById(id)

    override fun preloadAllSection(progress: ((Float) -> Unit)?, onEnd: (() -> Unit)?) {
        val allSectionId = sectionCenter.getAllSectionId()
        allSectionId.forEach {
            postInPreload {
                val sectionName = sectionCenter.getSectionNameById(it)
                val timeStart = System.currentTimeMillis()
                sectionCenter.getSectionById(it).onPreload()
                val timeElapse = System.currentTimeMillis() - timeStart
                Log.i(TAG, "preload section($sectionName) elapse: $timeElapse")
                postInMain {
                    progress?.invoke((it + 1).toFloat() / allSectionId.size)
                }
            }
        }
        postInPreload {
            postInMain {
                progress?.invoke(1F)
            }
        }
        postInPreload {
            postInMain {
                ThreadPlus.preloadThread?.quitSafely()
                onEnd?.invoke()
            }
        }
    }

    override fun setOnSectionChangeListener(listener: ISectionChangeListener) {
        sectionChangeListeners.addListener(listener)
    }

    override fun removeSectionChangeListener(listener: ISectionChangeListener) {
        sectionChangeListeners.removeLister(listener)
    }

    override fun lockSection(id: Int, isLock: Boolean) {
        sectionStateCenter.apply {
            if (isLock) lockSection(id)
            else unlockSection(id)
        }
    }

    override fun isSectionLock(id: Int) = sectionStateCenter.getStateById(id)?.isLock ?: true

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
        currentSectionId = targetId
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