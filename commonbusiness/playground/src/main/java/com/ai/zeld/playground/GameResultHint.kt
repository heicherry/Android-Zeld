package com.ai.zeld.playground

import com.ai.zeld.util.app.App
import com.ai.zeld.util.createDialog

fun showFailedHintDialog() {
    createDialog(App.activity, R.layout.playground_dialog_succeed).show()
}