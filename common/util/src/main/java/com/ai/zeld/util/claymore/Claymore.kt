package com.ai.zeld.util.claymore

import java.util.*

fun <T> Class<T>.load(): T {
    return ServiceLoader.load(this).first()
}