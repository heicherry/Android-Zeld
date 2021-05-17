package com.ai.zeld.util

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

fun createDialog(activity: Activity, resId: Int): Dialog {
    return AlertDialog.Builder(activity).create().apply {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(activity).inflate(resId, null, false)
        setView(view)
        window?.setBackgroundDrawable(context.resources.getDrawable(android.R.color.transparent))
    }
}
