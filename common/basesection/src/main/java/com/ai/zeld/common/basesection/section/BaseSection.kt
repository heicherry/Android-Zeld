package com.ai.zeld.common.basesection.section

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.ai.zeld.common.basesection.speak.ISpeakStage

abstract class BaseSection : Fragment(){
    protected var rootViewTree: View? = null
    private var sectionId = 0
    protected lateinit var localContext: Context
    lateinit var speakStage: ISpeakStage

    fun setContext(context: Context) {
        localContext = context
    }

    @CallSuper
    open fun onPreload() {
        rootViewTree = onBuildViewTree()
    }

    fun setSectionId(id: Int) {
        sectionId = id
    }

    fun getSectionId() = sectionId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootViewTree == null) {
            rootViewTree = onBuildViewTree()
        }
        return rootViewTree
    }

    abstract fun onBuildViewTree(): View

    /**
     * 在fragment切换前调用
     */
    open fun onSectionEnter() {}

    open fun onExitSection() {

    }

    abstract fun onReset()
}

enum class State {
    FOREPLAY, DINNER
}