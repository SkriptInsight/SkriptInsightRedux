package io.github.skriptinsight.redux.file.node.impl.skript.function

import io.github.skriptinsight.redux.core.location.Range
import io.github.skriptinsight.redux.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData

class FunctionSectionSkriptNode(
    val signature: FunctionSignature?,
    lineNumber: Int,
    rawContent: String,
    indentations: Array<NodeIndentationData>,
    nodeContentResult: NodeContentResult
) : SectionSkriptNode(lineNumber, rawContent, indentations, nodeContentResult) {

    override val ranges: List<Range>
        get() = super.ranges + (signature?.let {
            listOfNotNull(
                it.nameRange,
                it.parametersRange,
                it.returnTypeRange,
                *it.parameters.flatMap { p -> listOf(p.nameRange, p.typeRange, p.defaultValueRange) }.toTypedArray()
            )
        } ?: emptyList())
}