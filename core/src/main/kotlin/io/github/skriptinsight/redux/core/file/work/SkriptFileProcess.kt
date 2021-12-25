package io.github.skriptinsight.redux.core.file.work

import io.github.skriptinsight.redux.core.file.SkriptFile
import io.github.skriptinsight.redux.core.file.node.SkriptNode

abstract class UnitSkriptFileProcess : SkriptFileProcess<Unit>()

abstract class SkriptFileProcess<R> : FileProcess<SkriptFile, R, SkriptNode>() {

}