package com.ai.zeld.business.menu

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ai.zeld.common.service.world.IWorld
import com.ai.zeld.util.claymore.load

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private val allSectionId = IWorld::class.java.load().getAllSectionId().filter {
        IWorld::class.java.load().getSectionById(it).getCoverId() != -1
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val coverView: ImageView = view.findViewById(R.id.section_cover)
        val title: TextView = view.findViewById(R.id.title)

        init {
            title.typeface = Typeface.createFromAsset(view.context.assets, "pmzd.TTF")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, null)

        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= allSectionId.size) return
        holder.coverView.setImageResource(
            IWorld::class.java.load().getSectionById(allSectionId[position]).getCoverId()
        )

    }

    override fun getItemCount() = allSectionId.size
}