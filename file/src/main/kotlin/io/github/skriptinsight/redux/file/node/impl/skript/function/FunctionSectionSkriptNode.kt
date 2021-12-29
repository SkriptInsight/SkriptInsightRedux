package io.github.skriptinsight.redux.file.node.impl.skript.function

import io.github.skriptinsight.redux.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData

class FunctionSectionSkriptNode(
    val signature: FunctionSignature?,
    lineNumber: Int, rawContent: String, indentations: Array<NodeIndentationData>,
    nodeContentResult: NodeContentResult
) : SectionSkriptNode(lineNumber, rawContent, indentations, nodeContentResult) {
}