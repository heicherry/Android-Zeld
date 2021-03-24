package com.ai.zeld.common.basesection.section

interface ISectionChangeListener {
    fun onSectionPreChange(currentSectionId: Int, targetSectionId: Int)
    fun onSectionChanged(preSectionId: Int, currentSectionId: Int)
}