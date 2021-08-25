package com.ai.zeld.common.service.world

import android.content.Context
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.ISectionChangeListener

interface IWorld {
    fun getContext(): Context

    fun gotoNextSection()

    fun gotoSection(id: Int)

    fun gotoNextSectionLater()

    fun getCurrentSectionId(): Int

    fun getAllSectionId(): List<Int>

    fun getSectionById(id: Int): BaseSection

    fun preloadAllSection(progress: ((Float) -> Unit)? = null, onEnd: (() -> Unit)? = null)

    fun setOnSectionChangeListener(listener: ISectionChangeListener)

    fun removeSectionChangeListener(listener: ISectionChangeListener)

    fun lockSection(id: Int, isLock: Boolean)

    fun isSectionLock(id: Int): Boolean
}