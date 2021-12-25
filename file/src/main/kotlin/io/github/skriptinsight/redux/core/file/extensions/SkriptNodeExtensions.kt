package io.github.skriptinsight.redux.core.file.extensions

import io.github.skriptinsight.redux.core.file.node.SkriptNode

fun SkriptNode.isOnSameIndentLevel(currentLevel: Int): Boolean {
    if (indentations.isEmpty() && currentLevel == 0) return true

    return rawIndentCount == currentLevel
}

internal fun SkriptNode.isChildrenAccordingToIndent(indent: Int): Boolean {
    return this.rawIndentCount > indent || isEmptyNode || isCommentNode
}