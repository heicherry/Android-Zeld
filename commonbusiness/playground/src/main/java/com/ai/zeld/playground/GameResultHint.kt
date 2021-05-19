package com.ai.zeld.playground

import android.widget.ImageView
import com.ai.zeld.util.app.App
import com.ai.zeld.util.createDialog
import com.ai.zeld.util.gone

fun showGameResultHintDialog() {
    val dialog = createDialog(App.activity, R.layout.playground_dialog_game_result)
    dialog.window?.findViewById<ImageView>(R.id.play_continue)?.gone()
    dialog.show()
}