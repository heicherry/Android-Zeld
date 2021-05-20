package com.ai.zeld.common.uikit.panel

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.ai.zeld.util.app.App

class GlobalDialog(val view: View) : Dialog(App.activity) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //一定要在setContentView之后调用，否则无效
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    }
}