package io.github.skriptinsight.redux.file.node.indentation

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.extensions.isChildrenAccordingToIndent
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode

object IndentationUtils {

    fun computeNodeDataParents(file: SkriptFile) {
        // Compute indentation levels
        val indentationLevels = computeIndentationLevelsForNode(file.nodes.values)

        indentationLevels.forEach {
            file.runProcess(ComputeNodeDataParentAndChildrenProcess(it))
        }
    }

    fun computeIndentationLevelsForNode(nodeData: MutableCollection<AbstractSkriptNode>): List<Int> {
        val firstIndent = nodeData.firstOrNull()?.rawIndentCount ?: 0

        return nodeData
            // First, take all the nodes that are the same indent or are a child of the first indent
            .takeWhile { it.rawIndentCount == firstIndent || it.isChildrenAccordingToIndent(firstIndent) }
            // Then, select all indentations
            .flatMap { it.indentations.asIterable() }
            // Now, group by the amount of indentations
            .groupBy { it.amount }
            // Return the key of the group (aka the amount)
            .map { it.key }
            .toMutableList().apply {
                //Insert level zero
                add(0, 0)
            }
    }
}

