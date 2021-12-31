package io.github.skriptinsight.redux.file.node.processes

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.extensions.isChildrenAccordingToIndent
import io.github.skriptinsight.redux.file.extensions.isOnSameIndentLevel
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.file.work.UnitSkriptFileProcess

class ComputeNodeDataParentAndChildrenProcess(private val currentLevel: Int) : UnitSkriptFileProcess(
    "Structurally parsing code",
    "Analysing indentations (level ${currentLevel + 1})"
) {
    override fun doWork(file: SkriptFile, lineNumber: Int, rawContent: String, context: AbstractSkriptNode) {
        if (context is SectionSkriptNode && context.isOnSameIndentLevel(currentLevel)) {
            //Compute all child nodes
            val childrenNodes = file.nodes.values
                .stream()
                .skip((lineNumber + 1).toLong())
                .takeWhile { it.isChildrenAccordingToIndent(currentLevel) }

            childrenNodes.forEach { child ->
                //Set node as parent of the child
                child.parent = context
            }
        }
    }
}