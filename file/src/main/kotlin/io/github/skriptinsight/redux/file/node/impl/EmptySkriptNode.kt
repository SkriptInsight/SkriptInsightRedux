package io.github.skriptinsight.redux.file.node.impl

import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData

open class EmptySkriptNode internal constructor(
    lineNumber: Int,
    rawContent: String,
    indentations: Array<NodeIndentationData>, nodeContentResult: NodeContentResult? = null
) : AbstractSkriptNode(
    lineNumber, rawContent, indentations, nodeContentResult
)