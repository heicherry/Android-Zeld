package com.ai.zeld.common.service.menu

import android.view.View

interface IMenu {
    fun preload()
    fun getView(): View
    fun openMenu()
}