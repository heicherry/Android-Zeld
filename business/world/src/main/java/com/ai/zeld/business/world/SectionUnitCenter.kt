package com.ai.zeld.business.world

import android.content.Context
import android.os.Build
import android.util.AndroidRuntimeException
import androidx.annotation.RequiresApi
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import dalvik.system.DexFile
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class SectionUnitCenter(private val context: Context) {
    private val sectionUnits = mutableListOf<SectionUnit>()
    private val sectionMap = mutableMapOf<Int, String>()

    fun init() {
        buildSectionMap()
        buildSections()
        instanceAllSections()
    }

    fun getAllSectionId(): List<Int> {
        val ids = mutableListOf<Int>()
        sectionUnits.forEach {
            ids.add(it.id)
        }
        return ids.toList().sorted()
    }

    fun getInitialSectionId(): Int {
        return sectionMap.keys.minOrNull()!!
    }

    fun getSectionById(id: Int): BaseSection {
        if (!sectionMap.keys.contains(id)) throw AndroidRuntimeException("getSectionById id $id is not exit")
        return sectionUnits.firstOrNull { it.id == id }!!.section
    }

    fun findNextSectionId(id: Int): Int {
        if (!sectionMap.keys.contains(id)) return -1
        sectionMap.keys.sorted().let {
            val index = it.indexOf(id)
            return if (index + 1 < it.size) {
                it[index + 1]
            } else {
                -1
            }
        }
    }

    fun getSectionNameById(id: Int): String {
        if (!sectionMap.keys.contains(id)) throw AndroidRuntimeException("getSectionNameById id $id is not exit")
        return sectionMap[id]!!
    }

    fun getSectionId(section: BaseSection): Int {
        return sectionUnits.firstOrNull { it.section == section }?.id ?: -1
    }

    private fun instanceAllSections() {
        sectionUnits.forEach {
            it.section = it.clazz.newInstance() as BaseSection
            it.section.setSectionId(it.id)
            it.section.setContext(context)
        }
    }

    private fun buildSections() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getClasses(context.packageCodePath)
        } else {
            throw AndroidRuntimeException("not support version! phone version must higher ${Build.VERSION_CODES.N}")
        }
    }

    private fun buildSectionMap() {
        SectionConfig::class.java.fields.forEach {
            try {
                val value = it.get(SectionConfig) as Int
                val name = it.name
                sectionMap[value] = name
            } catch (e: Exception) {
            }
        }
    }

    private fun getSectionInfoById(id: Int): SectionUnit? {
        return sectionUnits.firstOrNull { it.id == id }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(ClassNotFoundException::class, IOException::class)
    private fun getClasses(packageCodePath: String): List<Class<*>> {
        val df = DexFile(packageCodePath) //通过DexFile查找当前的APK中可执行文件
        val enumeration: Enumeration<String> = df.entries() //获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
        val classes: MutableList<Class<*>> = ArrayList()
        while (enumeration.hasMoreElements()) {
            val className: String = enumeration.nextElement()
            if (className.startsWith("com.ai.zeld.business")) {
                val clazz = Class.forName(className)
                val annotation = clazz.getAnnotationsByType(Section::class.java).firstOrNull()
                if (annotation != null) {
                    val sectionName = sectionMap[annotation.value]
                    if (getSectionInfoById(annotation.value) == null && sectionName != null) {
                        sectionUnits.add(SectionUnit(annotation.value, sectionName, clazz))
                    }
                }
            }
        }
        return classes
    }

    private data class SectionUnit(val id: Int, val name: String, val clazz: Class<*>) {
        lateinit var section: BaseSection
    }
}

