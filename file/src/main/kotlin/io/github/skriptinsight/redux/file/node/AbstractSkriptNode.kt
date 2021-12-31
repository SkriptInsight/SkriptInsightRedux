package io.github.skriptinsight.redux.file.node

import io.github.skriptinsight.redux.core.location.Range
import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.file.node.content.NodeContentUtils
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData
import java.util.*

/**
 * Represents a line from a [Skript file][SkriptFile].
 *
 * @param lineNumber The number of this node.
 * @param rawContent The raw content of this node.
 * @param indentations The information about the indentation of this node
 * @author NickAcPT
 */
abstract class AbstractSkriptNode(
    var lineNumber: Int,
    val rawContent: String,
    val indentations: Array<NodeIndentationData>,
    nodeContentResult: NodeContentResult?
) {
    /**
     * The un-indented content of this node.
     */
    private val unIndentedRawContent: String

    /**
     * The normalized count of the indentation of this node.
     */
    val normalizedIndentCount: Int

    /**
     * The count of the indentation of this node.
     */
    internal val rawIndentCount: Int

    var contentRange: Range
    var commentRange: Range

    var content: String
    var comment: String

    open val ranges: List<Range>
        get() = listOf(contentRange, commentRange)

    init {
        val (contentResult, contentRangeResult, commentResult, commentRangeResult, unIndentedRawContentResult, normalizedIndentCountResult, rawIndentCountResult) = nodeContentResult
            ?: NodeContentUtils.computeContentData(
                lineNumber, rawContent, indentations
            )

        this.content = contentResult
        this.comment = commentResult
        this.contentRange = contentRangeResult
        this.commentRange = commentRangeResult
        this.unIndentedRawContent = unIndentedRawContentResult
        this.normalizedIndentCount = normalizedIndentCountResult
        this.rawIndentCount = rawIndentCountResult
    }

    /**
     * The parent of this node.
     */
    var parent: AbstractSkriptNode? = null
        set(value) {
            //Remove from old parent
            field?.children?.remove(this)
            //Add to new parent
            value?.children?.add(this)

            field = value
        }

    var children: MutableList<AbstractSkriptNode>? = Collections.synchronizedList(mutableListOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractSkriptNode) return false

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

    override fun toString(): String {
        return "AbstractSkriptNode(lineNumber=$lineNumber, indentations=${indentations.contentToString()}, content='$content', comment='$comment')"
    }
}
