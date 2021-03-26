package com.ai.zeld.common.service.world

import android.content.Context
import com.ai.zeld.common.basesection.section.ISectionChangeListener

interface IWorld {
    fun getContext(): Context

    fun gotoNextSection()

    fun gotoNextSectionLater()

    fun getCurrentSectionId(): Int

    fun preloadAllSection(progress: ((Float) -> Unit)? = null)

    fun setOnSectionChangeListener(listener: ISectionChangeListener)

    fun removeSectionChangeListener(listener: ISectionChangeListener)
}