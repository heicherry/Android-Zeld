package com.ai.zeld.business.menu

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
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
import com.ai.zeld.util.gone
import com.ai.zeld.util.visible

@RequiresApi(Build.VERSION_CODES.N)
class MenuAdapter : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private val sections = mutableListOf<SectionUnit>()

    init {
        parseSections()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun parseSections() {
        sections.clear()
        IWorld::class.java.load().getAllSectionId().filter {
            IWorld::class.java.load().getSectionById(it).getCoverId() != -1
        }.forEach {
            val section = IWorld::class.java.load().getSectionById(it)
            val annotation =
                section::class.java.getAnnotationsByType(Section::class.java).firstOrNull()
                    ?: return@forEach
            val unit =
                SectionUnit(section, annotation.title, annotation.level, true, section.getCoverId())
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
        val item = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, null)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= sections.size) return
        val unit = sections[position]
        holder.coverView.setImageResource(unit.coverId)
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