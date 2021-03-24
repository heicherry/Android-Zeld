package com.ai.zeld.business.world

import android.content.Context
import android.os.Build
import android.util.AndroidRuntimeException
import android.util.Log
import androidx.annotation.RequiresApi
import com.ai.zeld.common.basesection.annotation.Section
import com.ai.zeld.common.basesection.section.BaseSection
import com.ai.zeld.common.basesection.section.SectionConfig
import dalvik.system.DexFile
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class SectionControl(private val context: Context) {
    private val sections = mutableListOf<SectionInfo>()
    private val sectionMap = mutableMapOf<Int, String>()

    fun init() {
        buildSectionMap()
        buildSections()
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

    private fun getSectionInfoById(id: Int): SectionInfo? {
        return sections.firstOrNull { it.id == id }
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
                        sections.add(SectionInfo(annotation.value, sectionName, clazz))
                    }
                }
            }
        }
        return classes
    }
}

data class SectionInfo(val id: Int, val name: String, val clazz: Class<*>)