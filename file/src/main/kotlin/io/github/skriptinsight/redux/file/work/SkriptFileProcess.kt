package io.github.skriptinsight.redux.file.work

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode

abstract class SkriptFileProcess<R> : FileProcess<SkriptFile, R, AbstractSkriptNode>()