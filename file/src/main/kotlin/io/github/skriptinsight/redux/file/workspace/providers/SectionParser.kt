package io.github.skriptinsight.redux.file.workspace.providers

import io.github.skriptinsight.redux.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData

interface SectionParser {
    fun parseSection(
        lineNumber: Int,
        rawContent: String,
        indentations: Array<NodeIndentationData>,
        nodeContentResult: NodeContentResult
    ): SectionSkriptNode?
}