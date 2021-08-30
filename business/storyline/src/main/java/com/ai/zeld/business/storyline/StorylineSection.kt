package com.ai.zeld.business.storyline

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.ai.zeld.business.storyline.model.*
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.ext.speakWaitForClick
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import com.ai.zeld.common.media.BackgroundMusicPlayer
import com.ai.zeld.common.media.MusicClipsPlayerManager
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.app.App
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.postInMainDelay
import kotlinx.android.synthetic.main.storyline_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser

@Section(SectionConfig.STORYLINE, level = "", title = "")
class StorylineSection : BaseSection() {
    private lateinit var storyline: Storyline
    private val sp = App.application.getSharedPreferences("main", Context.MODE_PRIVATE)

    private var hasLoadStoryLine: Boolean
        get() = sp.getBoolean("hasLoadStoryLine", false)
        set(value) {
            sp.edit().putBoolean("hasLoadStoryLine", value).apply()
        }

    override fun onPreload() {
        super.onPreload()
        storyline = parseXml()
        preloadAllResource()
        BackgroundMusicPlayer.init()
        postInMainDelay(2000) {
            BackgroundMusicPlayer.play()
        }
        MusicClipsPlayerManager.init()
    }

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.storyline_main, null)
    }

    override fun onSectionEnter() {
        super.onSectionEnter()
        if (!hasLoadStoryLine) {
            startTalkStoryLine()
        } else {
            IWorld::class.java.load().gotoNextSectionLater()
        }
    }

    override fun onReset() {

    }

    private fun parseXml(): Storyline {
        val parser = localContext.resources.getXml(R.xml.storyline)
        val storyline = Storyline()
        var segment: Segment? = null
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "segment" -> {
                            segment = Segment()
                            storyline.segments.add(segment)
                        }
                        "still" -> {
                            segment?.still = Still().apply {
                                src = parser.getAttributeResourceValue(null, "src", -1)
                                scaleType = parser.getAttributeValue(null, "scaletype") ?: scaleType
                            }
                        }
                        "narrator" -> {
                            segment?.narrator = Narrator()
                        }
                        "speech" -> {
                            val speech = Speech()
                            val elapseTime = parser.getAttributeValue(null, "elapse_time")
                            speech.elapseTime = elapseTime?.toLong() ?: 3000
                            speech.text = parser.getAttributeValue(null, "text")
                            speech.prefix =
                                parser.getAttributeValue(null, "prefix") ?: speech.prefix
                            segment?.narrator?.speeches?.add(speech)
                        }
                    }
                }
            }
            parser.next()
        }
        return storyline
    }

    private fun preloadAllResource() {
        storyline.segments.forEach {
            val srcId = it.still?.src ?: -1
            if (srcId != -1) {
                it.still?.srcDrawable = localContext.resources.getDrawable(srcId, null)
            }
        }
    }

    private fun startTalkStoryLine() {
        GlobalScope.launch(Dispatchers.Main) {
            storyline.segments.forEach { segment ->
                segment.still?.srcDrawable?.let { src -> switchStill(segment.still!!, src) }
                segment.narrator?.speeches?.forEach {
                    val prefix = if (it.prefix.isNotEmpty()) it.prefix + ":" else it.prefix
                    speakStage.speakWaitForClick(prefix, it.text, it.elapseTime)
                }
            }
            hasLoadStoryLine = true
            IWorld::class.java.load().gotoNextSection()
        }
    }

    private suspend fun switchStill(bean: Still, drawable: Drawable) {
        still.clearAnimation()
        still.animate().alpha(0F).setDuration(100L).start()
        delay(100L)
        still.scaleType = when (bean.scaleType) {
            "center" -> ImageView.ScaleType.CENTER
            else -> ImageView.ScaleType.FIT_XY
        }
        still.setImageDrawable(drawable)
        still.animate().alpha(1F).setDuration(100L).start()
        delay(100L)
    }
}