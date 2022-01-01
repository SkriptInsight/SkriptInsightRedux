package io.github.skriptinsight.redux.file.event

import io.github.skriptinsight.redux.core.event.Event
import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace

data class SkriptFileLoadedEvent(val file: SkriptFile, val workspace: SkriptWorkspace) : Event