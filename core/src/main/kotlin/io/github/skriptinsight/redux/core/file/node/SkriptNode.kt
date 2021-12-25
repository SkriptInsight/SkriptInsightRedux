package io.github.skriptinsight.redux.core.file.node

import io.github.skriptinsight.redux.core.SyntaxFacts.linePattern
import io.github.skriptinsight.redux.core.file.SkriptFile
import io.github.skriptinsight.redux.core.file.extensions.getGroupRange
import io.github.skriptinsight.redux.core.file.location.Position
import io.github.skriptinsight.redux.core.file.location.Range
import io.github.skriptinsight.redux.core.file.node.indentation.NodeIndentationData
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Represents a line from a [Skript file][SkriptFile].
 *
 * @param lineNumber The number of this node.
 * @param rawContent The raw content of this node.
 * @param indentations The information about the indentation of this node
 * @author NickAcPT
 */
data class SkriptNode(val lineNumber: Int, val rawContent: String, val indentations: Array<NodeIndentationData>) {

    companion object {

        val linePatternRegex: Pattern = Pattern.compile(linePattern)

        @JvmStatic
        fun fromLine(lineNumber: Int, content: String): SkriptNode {
            return SkriptNode(
                lineNumber,
                content,
                NodeIndentationData.fromIndentation(content.takeWhile { it.isWhitespace() })
            )
        }
    }

    /**
     * The un-indented content of this node.
     */
    val unIndentedRawContent: String = rawContent.dropWhile { it.isWhitespace() }

    /**
     * The normalized count of the indentation of this node.
     */
    val normalizedIndentCount = indentations.sumOf { it.type.size * it.amount }

    /**
     * The count of the indentation of this node.
     */
    internal val rawIndentCount = rawContent.takeWhile { it.isWhitespace() }.count()

    var contentRange: Range? = null
    var commentRange: Range? = null

    var content: String = ""
    var comment: String = ""

    init {
        computeNodeContentData()
    }

    val isSectionNode: Boolean = content.endsWith(":")

    val isEmptyNode: Boolean = content.isEmpty() && comment.isEmpty()

    val isCommentNode: Boolean = content.isEmpty() && comment.isNotEmpty()

    /**
     * The parent of this node.
     */
    var parent: SkriptNode? = null
        get() = field
        set(value) {
            //Remove from old parent
            field?.children?.remove(this)
            //Add to new parent
            value?.children?.add(this)

            field = value
        }

    var children: MutableList<SkriptNode>? = mutableListOf()

    private fun computeNodeContentData() {
        val linePatternMatcher = linePatternRegex.matcher(unIndentedRawContent)
        // Check if line has a comment
        if (linePatternMatcher.matches()) {
            val originalContent = linePatternMatcher.group(1)
            content = originalContent.trimEnd()
            comment = linePatternMatcher.group(2)

            //Compute range for content and comment
            val trimmedCharsAmount = originalContent.length - content.length
            contentRange =
                groupRange(linePatternMatcher, 1, rawIndentCount, offsetEnd = rawIndentCount - trimmedCharsAmount)
            commentRange = groupRange(linePatternMatcher, 2, rawIndentCount)
        } else {
            //No comment. Default to un-indented raw content and no comment
            content = unIndentedRawContent.trimEnd()
            contentRange = pos(rawIndentCount)..pos(rawIndentCount + content.length)
            commentRange = contentRange!!.end..contentRange!!.end
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SkriptNode) return false

        if (lineNumber != other.lineNumber) return false
        if (rawContent != other.rawContent) return false
        if (!indentations.contentEquals(other.indentations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lineNumber.hashCode()
        result = 31 * result + rawContent.hashCode()
        result = 31 * result + indentations.contentHashCode()
        return result
    }

    private fun pos(column: Int): Position {
        return Position(lineNumber, column)
    }

    private fun groupRange(matcher: Matcher, group: Int, offsetStart: Int = 0, offsetEnd: Int = offsetStart): Range {
        return matcher.getGroupRange(group, offsetStart, offsetEnd, lineNumber)
    }

    override fun toString(): String {
        return "SkriptNode(lineNumber=$lineNumber, indentations=${indentations.contentToString()}, content='$content', comment='$comment', isSectionNode=$isSectionNode, isEmptyNode=$isEmptyNode, isCommentNode=$isCommentNode)"
    }
}