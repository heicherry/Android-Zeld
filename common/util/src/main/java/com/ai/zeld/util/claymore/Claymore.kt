package com.ai.zeld.util.claymore

import java.util.*

fun <T> Class<T>.load(): T {
    return Claymore.load(this) as T
}

private object Claymore {
    private val services = mutableMapOf<Class<*>, Any>()

    fun load(clazz: Class<*>): Any {
        var service = services[clazz]
        if (service == null) {
            service = ServiceLoader.load(clazz).first()
            services[clazz] = service!!
        }
        return service
    }
}