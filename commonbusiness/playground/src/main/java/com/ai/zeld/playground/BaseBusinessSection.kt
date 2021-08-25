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
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.ai.zeld.common.basesection.ext.speakWaitForClick
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.service.menu.IMenu
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.common.uikit.panel.GlobalDialog
import com.ai.zeld.track.Track
import com.ai.zeld.util.app.App
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.gone
import com.ai.zeld.util.idToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser

abstract class BaseBusinessSection : BaseSection(), IGameResult {
    private val succeedBitmap = R.drawable.playground_succeed.idToBitmap()
    private val failedBitmap = R.drawable.playground_failed.idToBitmap()

    private var needShowPrologue = true
    private var isPrologueSpeeching = true
    private var prologueSpeechTask: Job? = null

    @CallSuper
    override fun onFailed() {
        Track.onSceneFinished(sectionName, false)
    }

    @CallSuper
    override fun onSucceed(diamondCount: Int) {
        Track.onSceneFinished(sectionName, true)
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
        Track.onSceneEnter(sectionName)
        IWorld::class.java.load().lockSection(getSectionId(), false)
        val prologueId = getPrologueXmlId()
        if (prologueId != -1) {
            startTalkPrologue(parsePrologueXml(prologueId))
        }
    }

    private fun startTalkPrologue(prologue: List<SpeechNode>) {
        prologueSpeechTask = GlobalScope.launch(Dispatchers.Main) {
            isPrologueSpeeching = true
            prologue.forEach {
                speakStage.speakWaitForClick(it.prefix, it.content, it.content.length * 200L)
            }
            isPrologueSpeeching = false
        }
    }

    override fun onExitSection() {
        super.onExitSection()
        prologueSpeechTask?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }

    fun isPrologueSpeeching() = isPrologueSpeeching

    private fun parsePrologueXml(xmlId: Int): List<SpeechNode> {
        val parser = localContext.resources.getXml(xmlId)
        val speech = mutableListOf<SpeechNode>()
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "node" -> {
                            val prefix = parser.getAttributeValue(null, "prefix")?.let {
                                "$it:"
                            } ?: ""
                            val content = parser.getAttributeValue(null, "text") ?: ""
                            speech.add(SpeechNode(prefix, content))
                        }
                    }
                }
            }
            parser.next()
        }
        return speech
    }

    data class SpeechNode(val prefix: String, val content: String)
}