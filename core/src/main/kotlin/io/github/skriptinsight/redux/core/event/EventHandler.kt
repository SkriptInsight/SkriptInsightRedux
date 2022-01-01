package io.github.skriptinsight.redux.core.event

fun interface EventHandler<T: Event> {
    fun handle(event: T)
}