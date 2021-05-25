package com.ai.zeld.business.menu

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionLevel
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.claymore.load
import com.ai.zeld.util.clickWithTrigger
import com.ai.zeld.util.gone
import com.ai.zeld.util.idToBitmap
import com.ai.zeld.util.visible
import com.hjq.toast.ToastUtils

@RequiresApi(Build.VERSION_CODES.N)
class MenuAdapter(private val attachDialog: Dialog) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private val sections = mutableListOf<SectionUnit>()

    init {
        parseSections()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun parseSections() {
        sections.clear()
        val world = IWorld::class.java.load()
        world.getAllSectionId().filter {
            IWorld::class.java.load().getSectionById(it).getCoverId() != -1
        }.forEach {
            val section = IWorld::class.java.load().getSectionById(it)
            val annotation =
                section::class.java.getAnnotationsByType(Section::class.java).firstOrNull()
                    ?: return@forEach
            val unit =
                SectionUnit(
                    section,
                    annotation.title,
                    annotation.level,
                    world.isSectionLock(section.getSectionId()),
                    section.getCoverId()
                )
            sections.add(unit)
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val coverView: ImageView = view.findViewById(R.id.section_cover)
        val title: TextView = view.findViewById(R.id.title)
        val lock: View = view.findViewById(R.id.lock)
        val level: TextView = view.findViewById(R.id.level)

        init {
            title.typeface = Typeface.createFromAsset(view.context.assets, "pmzd.TTF")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val startTime = System.currentTimeMillis()
        val item = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, null)
        Log.i("ayy","xml load: ${System.currentTimeMillis() - startTime}")
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= sections.size) return
        val unit = sections[position]
        holder.coverView.setImageBitmap(unit.coverId.idToBitmap())
        holder.coverView.clickWithTrigger {
            // if (unit.isLock) {
            //    ToastUtils.show(R.string.menu_lock_hint)
            // } else {
            IWorld::class.java.load().gotoSection(unit.section.getSectionId())
            attachDialog.dismiss()
            //}
        }

        holder.title.text = unit.title
        holder.lock.apply {
            if (unit.isLock) visible()
            else gone()
        }
        holder.level.text = unit.level
        when (unit.level) {
            SectionLevel.EASY -> holder.level.setTextColor(Color.GREEN)
            SectionLevel.MIDDLE -> holder.level.setTextColor(Color.BLUE)
            SectionLevel.HARD -> holder.level.setTextColor(Color.RED)
        }
    }

    override fun getItemCount() = sections.size

    data class SectionUnit(
        val section: BaseSection,
        val title: String,
        val level: String,
        val isLock: Boolean,
        val coverId: Int
    )
}