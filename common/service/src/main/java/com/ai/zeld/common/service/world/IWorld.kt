package com.ai.zeld.common.service.world

import android.content.Context

interface IWorld {
    fun getContext():Context

    fun gotoNextSection()

    fun getCurrentSectionId():Int

    fun getCurrentSectionName():String

    fun getSectionId(name:String):Int

    fun gotoSection(id:Int)

    fun gotoSection(name:String)
}