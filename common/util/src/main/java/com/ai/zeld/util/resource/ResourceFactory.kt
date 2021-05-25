package com.ai.zeld.util.resource

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AndroidRuntimeException
import androidx.annotation.RequiresApi
import com.ai.zeld.util.app.App
import dalvik.system.DexFile
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object ResourceFactory {
    private val allDrawableRes = mutableMapOf<Int, Bitmap>()

    fun preload() {
        buildResourceMap()
    }

    fun loadDrawable(id: Int): Bitmap? {
        return allDrawableRes[id]
    }

    private fun buildResourceMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            loadResource(getClasses(App.application.packageCodePath))
        } else {
            throw AndroidRuntimeException("not support version! phone version must higher ${Build.VERSION_CODES.N}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(ClassNotFoundException::class, IOException::class)
    private fun getClasses(packageCodePath: String): List<Class<*>> {
        val df = DexFile(packageCodePath) //通过DexFile查找当前的APK中可执行文件
        val enumeration: Enumeration<String> = df.entries() //获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
        val classes: MutableList<Class<*>> = ArrayList()
        while (enumeration.hasMoreElements()) {
            val className: String = enumeration.nextElement()
            if (className.startsWith("com.ai.zeld") && className.endsWith(".R")) {
                kotlin.runCatching {
                    classes.add(Class.forName("$className\$drawable"))
                }
            }
        }
        return classes
    }

    private fun loadResource(clazzNames: List<Class<*>>) {
        clazzNames.forEach { clazz ->
            val fields = clazz.fields
            for (i in fields.indices) {
                fields[i].isAccessible = true
                val name: String = fields[i].name
                kotlin.runCatching {
                    val resId: Int = App.application.resources.getIdentifier(
                        name,
                        "drawable",
                        App.application.packageName
                    )
                    allDrawableRes[resId] = BitmapFactory.decodeResource(
                        App.application.resources,
                        resId
                    )
                }
            }
        }
    }
}