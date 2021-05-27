package com.ai.zeld.playground

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.ai.zeld.common.basesection.ext.speakWaitForClick
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.service.menu.IMenu
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.common.uikit.panel.GlobalDialog
import com.ai.zeld.util.app.App
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.gone
import com.ai.zeld.util.idToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser

abstract class BaseBusinessSection : BaseSection(), IGameResult {
    private val succeedBitmap = R.drawable.playground_succeed.idToBitmap()
    private val failedBitmap = R.drawable.playground_failed.idToBitmap()

    private var needShowPrologue = true

    override fun onFailed() {
    }

    override fun onSucceed(diamondCount: Int) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val parent = FrameLayout(localContext)
        parent.addView(
            view, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        parent.addView(IMenu::class.java.load().getView())
        return parent
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showGameResultHintDialog(succeed: Boolean) {
        val view = LayoutInflater.from(App.activity)
            .inflate(R.layout.playground_dialog_game_result, null, false)
        val icon = view.findViewById<ImageView>(R.id.play_icon)
        if (succeed) icon.setImageBitmap(succeedBitmap)
        else icon.setImageBitmap(failedBitmap)
        val dialog = GlobalDialog(view)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setWindowAnimations(R.style.DialogInStyle)
        }

        view.findViewById<ImageView>(R.id.play_continue).apply {
            setOnClickListener {
                IWorld::class.java.load().gotoNextSection()
                dialog.dismiss()
            }
            if (!succeed) gone()
        }

        view.findViewById<ImageView>(R.id.play_again).setOnClickListener {
            onReset()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun getCoverId(): Int {
        return R.drawable.playground_cover_sample
    }

    protected open fun getPrologueXmlId() = R.xml.prologue

    override fun onSectionEnter() {
        super.onSectionEnter()
        IWorld::class.java.load().lockSection(getSectionId(), false)
        val prologueId = getPrologueXmlId()
        if (prologueId != -1) {
            startTalkPrologue(parsePrologueXml(prologueId))
        }
    }

    private fun startTalkPrologue(prologue: List<String>) {
        GlobalScope.launch(Dispatchers.Main) {
            prologue.forEach {
                speakStage.speakWaitForClick("", it, 2000)
            }
        }
    }

    private fun parsePrologueXml(xmlId: Int): List<String> {
        val parser = localContext.resources.getXml(xmlId)
        val speech = mutableListOf<String>()
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "node" -> {
                            speech.add(parser.getAttributeValue(null, "text"))
                        }
                    }
                }
            }
            parser.next()
        }
        return speech
    }
}