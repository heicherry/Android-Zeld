package com.ai.zeld.business.menu

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ai.zeld.common.service.menu.IMenu
import com.ai.zeld.common.uikit.panel.GlobalDialog
import com.ai.zeld.util.app.App
import com.ai.zeld.util.clickWithTrigger
import com.ai.zeld.util.extendTouchRect
import com.ai.zeld.util.px2dp
import kotlin.math.roundToInt

class Menu : IMenu {
    override fun preload() {

    }

    override fun getView(): View {
        val menuView = ImageView(App.activity)
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        menuView.setImageResource(R.drawable.menu_icon)
        menuView.clickWithTrigger {
            openMenu()
        }
        lp.gravity = Gravity.RIGHT
        lp.marginEnd = 150.px2dp().toInt()
        lp.topMargin = 120.px2dp().toInt()
        menuView.layoutParams = lp
        menuView.doOnLayout {
            it.extendTouchRect(10.px2dp().roundToInt())
        }
        return menuView
    }

    override fun openMenu() {
        showDialog()
    }

    private fun showDialog() {
        val view = LayoutInflater.from(App.activity)
            .inflate(R.layout.menu_main, null, false)
        val dialog = GlobalDialog(view)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setWindowAnimations(R.style.DialogInStyle)
        }
        dialog.setCancelable(true)
        dialog.show()

        setupRecycleView(dialog, view.findViewById(R.id.sections))
    }

    @SuppressLint("NewApi")
    private fun setupRecycleView(dialog: GlobalDialog, recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(App.activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MenuAdapter(dialog)
    }
}