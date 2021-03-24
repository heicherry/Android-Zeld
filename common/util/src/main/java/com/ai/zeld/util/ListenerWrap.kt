package com.ai.zeld.util

class ListenerWrap<T> {
    private val listeners = mutableListOf<T>()

    fun addListener(listener: T) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    fun removeLister(listener: T) {
        listeners.remove(listener)
    }

    fun dispatchInvoke(block: (listener: T) -> Unit) {
        mutableListOf<T>().let {
            it.addAll(listeners)
            it.forEach { listener ->
                block.invoke(listener)
            }
        }
    }
}