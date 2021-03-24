package com.ai.zeld.common.service.world

import android.content.Context
import com.ai.zeld.common.basesection.section.ISectionChangeListener

interface IWorld {
    fun getContext():Context

    fun gotoNextSection()

    fun getCurrentSectionId():Int

    fun getCurrentSectionName():String

    fun getSectionId(name:String):Int

    fun gotoSection(id:Int)

    fun gotoSection(name:String)

    fun preloadAllSection()

    fun setOnSectionChangeListener(listener:ISectionChangeListener)

    fun removeSectionChangeListener(listener:ISectionChangeListener)
}