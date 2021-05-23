package com.ai.zeld.business.world.sectionstate

import android.content.Context
import androidx.core.content.edit
import com.ai.zeld.util.app.App
import com.ai.zeld.util.toJson
import com.ai.zeld.util.toOb

class SectionStateCenter {
    private val sp = App.application.getSharedPreferences("section_state", Context.MODE_PRIVATE)

    fun initInitialState(initialId: Int) {
        val section = getStateById(initialId)
        if (section == null) {
            changeState(initialId) {
                it.isLock = false
            }
        }
    }

    fun getStateById(id: Int): SectionState? {
        return sp.getString(id.toString(), "")?.toOb<SectionState>()
    }

    fun lockSection(id: Int) {
        changeState(id) {
            it.isLock = true
        }
    }

    fun unlockSection(id: Int) {
        changeState(id) {
            it.isLock = false
        }
    }

    private fun saveSectionState(sectionState: SectionState) {
        sp.edit(true) {
            putString(sectionState.sectionId.toString(), sectionState.toJson())
        }
    }

    private fun changeState(id: Int, block: (SectionState) -> Unit) {
        val state = getStateById(id) ?: SectionState(id, true)
        block(state)
        saveSectionState(state)
    }
}

data class SectionState(val sectionId: Int, var isLock: Boolean)
