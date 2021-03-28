package com.ai.zeld.business.storyline

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.ai.zeld.business.storyline.model.*
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.ext.speakWaitForClick
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import kotlinx.android.synthetic.main.storyline_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser

@Section(SectionConfig.STORYLINE)
class StorylineSection : BaseSection() {
    private lateinit var storyline: Storyline
    override fun onPreload() {
        super.onPreload()
        storyline = parseXml()
        preloadAllResource()
    }

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.storyline_main, null)
    }

    override fun onSectionEnter() {
        super.onSectionEnter()
        startTalkStoryLine()
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
                            segment?.still = Still()
                            segment?.still?.src = parser.getAttributeResourceValue(null, "src", -1)
                        }
                        "narrator" -> {
                            segment?.narrator = Narrator()
                        }
                        "speech" -> {
                            val speech = Speech()
                            val elapseTime = parser.getAttributeValue(null, "elapse_time")
                            speech.elapseTime = elapseTime?.toLong() ?: 3000
                            speech.text = parser.getAttributeValue(null, "text")
                            speech.prefix = parser.getAttributeValue(null, "prefix")
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
                segment.still?.srcDrawable?.let { src -> switchStill(src) }
                segment.narrator?.speeches?.forEach {
                    speakStage.speakWaitForClick(it.prefix + ":", it.text, it.elapseTime)
                }
            }
        }
    }

    private suspend fun switchStill(drawable: Drawable) {
        still.clearAnimation()
        still.animate().alpha(0F).setDuration(100L).start()
        delay(100L)
        still.setImageDrawable(drawable)
        still.animate().alpha(1F).setDuration(100L).start()
        delay(100L)
    }
}