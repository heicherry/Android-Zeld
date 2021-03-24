package com.ai.zeld.common.basesection.section

import androidx.fragment.app.Fragment

abstract class BaseSection : Fragment() {
    abstract fun onPreload()

    fun getSectionName(): String{
        return ""
    }

     fun switchState(state: State){

     }

    abstract fun onForeplayShow()

    abstract fun onDinnerShow()

    /**
     * 在fragment切换前调用
     */
    fun onSectionEnter(){}

    fun onExitSection(){

    }

    fun exitSection(){

    }
}


enum class State {
    FOREPLAY, DINNER
}