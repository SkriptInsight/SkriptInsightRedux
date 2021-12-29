package io.github.skriptinsight.redux.file.node.impl.skript

import io.github.skriptinsight.redux.core.location.Range
import io.github.skriptinsight.redux.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData

class FunctionSectionSkriptNode(
    val name: String?,
    val nameRange: Range?,
    val rawParameters: String?,
    val rawParametersRange: Range?,
    val rawReturnType: String?,
    val rawReturnTypeRange: Range?,
    lineNumber: Int, rawContent: String, indentations: Array<NodeIndentationData>,
    nodeContentResult: NodeContentResult
) : SectionSkriptNode(lineNumber, rawContent, indentations, nodeContentResult) {
}