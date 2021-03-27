package com.ai.zeld.business.storyline

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import com.ai.zeld.business.storyline.model.*
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import org.xmlpull.v1.XmlPullParser

@Section(SectionConfig.STORYLINE)
class StorylineSection : BaseSection() {
    override fun onPreload() {
        super.onPreload()
    }


    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.storyline_main, null)
    }

    override fun onSectionEnter() {
        super.onSectionEnter()
        parseXml()
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
                            val src = parser.getAttributeValue(null, "src")
                            segment?.still?.src = localContext.resources.getIdentifier(
                                src,
                                "drawable",
                                localContext.packageName
                            )
                        }
                        "narrator" -> {
                            segment?.narrator = Narrator()
                        }
                        "speech" -> {
                            val speech = Speech()
                            val elapseTime = parser.getAttributeValue(null, "elapse_time")
                            speech.elapseTime = elapseTime?.toLong() ?: 3000
                            speech.text = parser.getAttributeValue(null, "text")
                            segment?.narrator?.speeches?.add(speech)
                        }
                    }
                }
                XmlPullParser.END_TAG -> {

                }
            }
            parser.next()
        }
        return storyline
    }
}