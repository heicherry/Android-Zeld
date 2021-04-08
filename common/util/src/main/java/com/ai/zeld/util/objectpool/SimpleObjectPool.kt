package com.ai.zeld.util.objectpool

open class SimpleObjectPool<T>(initNum: Int, val createFun: () -> T) {
    private val objects = mutableListOf<T>()

    init {
        initNum.until(0).forEach { _ -> objects.add(createFun()) }
    }

    open fun borrow() = synchronized(objects) {
        if (objects.size == 0) {
            createFun()
        } else {
            objects.removeLast()
        }
    }


    open fun recover(value: T) = synchronized(objects) {
        if (!objects.contains(value)) {
            objects.add(value)
        }
    }
}