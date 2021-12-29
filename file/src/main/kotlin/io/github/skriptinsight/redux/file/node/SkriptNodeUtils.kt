package io.github.skriptinsight.redux.file.node

import io.github.skriptinsight.redux.file.node.content.NodeContentUtils
import io.github.skriptinsight.redux.file.node.impl.CommentSkriptNode
import io.github.skriptinsight.redux.file.node.impl.EmptySkriptNode
import io.github.skriptinsight.redux.file.node.impl.NormalSkriptNode
import io.github.skriptinsight.redux.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace

object SkriptNodeUtils {
    @JvmStatic
    fun createSkriptNodeFromLine(workspace: SkriptWorkspace, lineNumber: Int, content: String): AbstractSkriptNode {
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
                var resultingSectionNode: SectionSkriptNode? = null
                for (sectionProvider in workspace.sectionProviders) {
                    resultingSectionNode =
                        sectionProvider.parseSection(lineNumber, content, indentationData, nodeContentResult)
                    if (resultingSectionNode != null) {
                        break
                    }
                }

                if (resultingSectionNode == null) resultingSectionNode =
                    SectionSkriptNode(lineNumber, content, indentationData, nodeContentResult)

                resultingSectionNode
            } else {
                NormalSkriptNode(lineNumber, content, indentationData, nodeContentResult)
            }
        }
    }
}