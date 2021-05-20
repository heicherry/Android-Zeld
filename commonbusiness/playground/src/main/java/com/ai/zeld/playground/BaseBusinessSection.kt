package com.ai.zeld.playground

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.uikit.panel.GlobalDialog
import com.ai.zeld.util.app.App
import com.ai.zeld.util.idToBitmap

abstract class BaseBusinessSection : BaseSection(), IGameResult {
    private val succeedBitmap = R.drawable.playground_succeed.idToBitmap()
    private val failedBitmap = R.drawable.playground_failed.idToBitmap()

    override fun onFailed() {
    }

    override fun onSucceed(diamondCount: Int) {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showGameResultHintDialog(succeed: Boolean) {
        val view = LayoutInflater.from(App.activity)
            .inflate(R.layout.playground_dialog_game_result, null, false)
        val icon = view.findViewById<ImageView>(R.id.play_icon)
        if (succeed) icon.setImageBitmap(succeedBitmap)
        else icon.setImageBitmap(failedBitmap)
        val dialog = GlobalDialog(App.activity, view)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setWindowAnimations(R.style.DialogInStyle)
        }

        view.findViewById<ImageView>(R.id.play_continue).setOnClickListener {
            //TODO: 跳转到下一个场景
            dialog.dismiss()
        }

        view.findViewById<ImageView>(R.id.play_again).setOnClickListener {
            onReset()
            dialog.dismiss()
        }
        dialog.show()
    }
}