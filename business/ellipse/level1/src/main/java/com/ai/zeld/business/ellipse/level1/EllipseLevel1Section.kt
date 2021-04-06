package com.ai.zeld.business.ellipse.level1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.ai.zeld.business.elllipse.level1.R
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.World


@Section(SectionConfig.HERO_CAN_NOT_FLY)
class EllipseLevel1Section : BaseSection() {


    override fun onPreload() {
        super.onPreload()
        Box2D.init()
        initWorld()
    }

    @SuppressLint("InflateParams")
    override fun onBuildViewTree(): View {
        return LayoutInflater.from(localContext).inflate(R.layout.ellipse_level1_main, null)
    }

    private fun initWorld(){

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSectionEnter() {
        super.onSectionEnter()

    }

}