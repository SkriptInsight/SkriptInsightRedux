package io.github.skriptinsight.redux.core.file.extensions

import io.github.skriptinsight.redux.core.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.core.file.node.impl.CommentSkriptNode
import io.github.skriptinsight.redux.core.file.node.impl.EmptySkriptNode

fun AbstractSkriptNode.isOnSameIndentLevel(currentLevel: Int): Boolean {
    if (indentations.isEmpty() && currentLevel == 0) return true

    return rawIndentCount == currentLevel
}

internal fun AbstractSkriptNode.isChildrenAccordingToIndent(indent: Int): Boolean {
    return this.rawIndentCount > indent || this is EmptySkriptNode || this is CommentSkriptNode
}