package io.github.skriptinsight.redux.core.utils

import kotlin.reflect.KProperty

interface ExtraDataContainer {

    val extraData: MutableMap<String, Any>

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(property: KProperty<T>): T? {
        return get(property.name)
    }

    operator fun <T> set(property: KProperty<T>, value: T?) {
        set(property.name, value)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(name: String): T? {
        return extraData[name] as? T?
    }

    operator fun set(name: String, value: Any?) {
        if (value == null) {
            extraData.remove(name)
        } else {
            extraData[name] = value
        }
    }

}