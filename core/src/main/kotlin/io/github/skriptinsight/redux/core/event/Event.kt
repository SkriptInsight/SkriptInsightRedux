package io.github.skriptinsight.redux.core.event

import io.github.skriptinsight.redux.core.event.bus.EventBus

interface Event

fun Event.post(): Event {
    return EventBus.dispatch(this)
}