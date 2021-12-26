package io.github.skriptinsight.redux.core.file.work

import io.github.skriptinsight.redux.core.file.SkriptFile
import io.github.skriptinsight.redux.core.file.node.AbstractSkriptNode

abstract class SkriptFileProcess<R> : FileProcess<SkriptFile, R, AbstractSkriptNode>()