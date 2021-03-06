package io.github.skriptinsight.redux.file.extensions

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.node.indentation.IndentationUtils.computeIndentationLevelsForNode

fun SkriptFile.printStructuralTree(): String {
    return buildString {
        val fileRootNodes = rootNodes
        val indentationLevels =
            computeIndentationLevelsForNode(nodes.values)

        fileRootNodes.forEach { it.printNodeChildren(this, indentationLevels) }
    }
}

private fun AbstractSkriptNode.printNodeChildren(sb: StringBuilder, indentationLevels: List<Int>) {
    sb.apply {
        val parentSpace = parent?.normalizedIndentCount?.takeIf { it > 0 }?.plus(1) ?: 0
        append(" ".repeat(parentSpace))

        val dashAmount = (normalizedIndentCount - parentSpace).coerceAtLeast(0)
        if (dashAmount != 0)
            append('|')
        append("-".repeat(dashAmount))

        append(content)
        append(comment)
        append(System.lineSeparator())

        children?.forEach { it.printNodeChildren(this, indentationLevels) }
    }
}