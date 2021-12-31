package io.github.skriptinsight.redux.file.work

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import java.util.*

abstract class SkriptFileProcess<R>(title: String, description: String?) : FileProcess<SkriptFile, R, AbstractSkriptNode>(UUID.randomUUID(),
    title, description
)