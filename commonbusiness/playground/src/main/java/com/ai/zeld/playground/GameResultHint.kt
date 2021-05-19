package com.ai.zeld.playground

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.ai.zeld.common.uikit.panel.GlobalDialog
import com.ai.zeld.util.Block
import com.ai.zeld.util.app.App
import com.ai.zeld.util.idToBitmap

private val succeedBitmap = R.drawable.playground_succeed.idToBitmap()
private val failedBitmap = R.drawable.playground_failed.idToBitmap()

@SuppressLint("UseCompatLoadingForDrawables")
fun showGameResultHintDialog(succeed: Boolean, again: Block, next: Block) {
    val view = LayoutInflater.from(App.activity)
        .inflate(R.layout.playground_dialog_game_result, null, false)
    val icon = view.findViewById<ImageView>(R.id.play_icon)
    if (succeed) icon.setImageBitmap(succeedBitmap)
    else icon.setImageBitmap(failedBitmap)
    val dialog = GlobalDialog(App.activity, view)
    dialog.window?.let {
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
    view.findViewById<ImageView>(R.id.play_continue).setOnClickListener {
        next()
        dialog.dismiss()
    }
    view.findViewById<ImageView>(R.id.play_again).setOnClickListener {
        again()
        dialog.dismiss()
    }
    dialog.show()
}