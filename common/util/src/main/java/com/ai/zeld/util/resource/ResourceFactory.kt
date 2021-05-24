package com.ai.zeld.util.resource

import android.os.Build
import android.util.AndroidRuntimeException
import android.util.Log
import androidx.annotation.RequiresApi
import com.ai.zeld.util.app.App
import dalvik.system.DexFile
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object ResourceFactory {
    fun preload() {
        buildResourceMap()
    }

    private fun buildResourceMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getClasses(App.application.packageCodePath)
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
                val clazz = Class.forName(className)
                Log.i("ayy", "class: $className")
            }
        }
        return classes
    }
}