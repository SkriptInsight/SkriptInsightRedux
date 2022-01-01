package io.github.skriptinsight.redux.core.event.bus

import io.github.skriptinsight.redux.core.event.Event
import io.github.skriptinsight.redux.core.event.EventHandler

object EventBus {
    private val handlers = mutableMapOf<Class<out Event>, MutableList<EventHandler<*>>>()

    fun <T : Event> register(eventClass: Class<T>, handler: EventHandler<T>) {
        handlers.getOrPut(eventClass) { mutableListOf() }.add(handler)
    }

    fun <T : Event> unregister(eventClass: Class<T>, handler: EventHandler<T>) {
        handlers[eventClass]?.remove(handler)
    }

    inline fun <reified T : Event> register(handler: EventHandler<T>) {
        register(T::class.java, handler)
    }

    inline fun <reified T : Event> unregister(handler: EventHandler<T>) {
        unregister(T::class.java, handler)
    }

    fun <T : Event> dispatch(event: T): T {
        handlers[event::class.java]?.forEach { (it as EventHandler<T>).handle(event) }

        return event
    }

}