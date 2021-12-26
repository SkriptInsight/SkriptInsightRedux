package io.github.skriptinsight.redux.core.file.node.impl

import io.github.skriptinsight.redux.core.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.core.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.core.file.node.indentation.NodeIndentationData

class NormalSkriptNode internal constructor(
    lineNumber: Int,
    rawContent: String,
    indentations: Array<NodeIndentationData>, nodeContentResult: NodeContentResult? = null
) : AbstractSkriptNode(
    lineNumber, rawContent,
    indentations, nodeContentResult,
)