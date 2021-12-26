package io.github.skriptinsight.redux.core.file.node

import io.github.skriptinsight.redux.core.file.node.content.NodeContentUtils
import io.github.skriptinsight.redux.core.file.node.impl.CommentSkriptNode
import io.github.skriptinsight.redux.core.file.node.impl.EmptySkriptNode
import io.github.skriptinsight.redux.core.file.node.impl.NormalSkriptNode
import io.github.skriptinsight.redux.core.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.core.file.node.indentation.NodeIndentationData

object SkriptNodeUtils {
    @JvmStatic
    fun createSkriptNodeFromLine(lineNumber: Int, content: String): AbstractSkriptNode {
        val indentationData = NodeIndentationData.fromIndentation(content.takeWhile { it.isWhitespace() })

        val nodeContentResult = NodeContentUtils.computeContentData(lineNumber, content, indentationData)

        val hasContent = nodeContentResult.content.isNotBlank()
        val hasComment = nodeContentResult.comment.isNotBlank()

        return if (!hasContent) {
            if (hasComment) {
                CommentSkriptNode(lineNumber, content, indentationData, nodeContentResult)
            } else {
                EmptySkriptNode(lineNumber, content, indentationData, nodeContentResult)
            }
        } else {
            if (nodeContentResult.content.endsWith(":")) {
                SectionSkriptNode(lineNumber, content, indentationData, nodeContentResult)
            } else {
                NormalSkriptNode(lineNumber, content, indentationData, nodeContentResult)
            }
        }
    }
}