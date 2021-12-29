package io.github.skriptinsight.redux.file.node.content

import io.github.skriptinsight.redux.core.SkriptSyntaxFacts
import io.github.skriptinsight.redux.core.location.Position
import io.github.skriptinsight.redux.core.location.Range
import io.github.skriptinsight.redux.file.extensions.getGroupRange
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData
import java.util.regex.Matcher
import java.util.regex.Pattern

object NodeContentUtils {
    private val linePatternRegex: Pattern = Pattern.compile(SkriptSyntaxFacts.linePattern)

    fun computeContentData(
        lineNumber: Int,
        rawContent: String,
        indentations: Array<NodeIndentationData>
    ): NodeContentResult {
        fun pos(column: Int): Position {
            return Position(lineNumber, column)
        }

        fun groupRange(matcher: Matcher, group: Int, offsetStart: Int = 0, offsetEnd: Int = offsetStart): Range {
            return matcher.getGroupRange(group, offsetStart, offsetEnd, lineNumber)
        }

        val unIndentedRawContent: String = rawContent.dropWhile { it.isWhitespace() }

        val normalizedIndentCount = indentations.sumOf { it.type.size * it.amount }

        val rawIndentCount = rawContent.takeWhile { it.isWhitespace() }.count()

        var comment = ""
        var content = unIndentedRawContent.trimEnd()
        var contentRange = pos(rawIndentCount)..pos(rawIndentCount + content.length)
        var commentRange = contentRange.end..contentRange.end

        // Check if line has a comment
        if (unIndentedRawContent.contains("#")) {
            val linePatternMatcher = linePatternRegex.matcher(unIndentedRawContent)
            if (linePatternMatcher.matches()) {
                val originalContent = linePatternMatcher.group(1)
                content = originalContent.trimEnd()
                comment = linePatternMatcher.group(2)

                //Compute range for content and comment
                val trimmedCharsAmount = originalContent.length - content.length
                contentRange =
                    groupRange(linePatternMatcher, 1, rawIndentCount, offsetEnd = rawIndentCount - trimmedCharsAmount)
                commentRange = groupRange(linePatternMatcher, 2, rawIndentCount)
            }
        }

        return NodeContentResult(
            content,
            contentRange,
            comment,
            commentRange,
            unIndentedRawContent,
            normalizedIndentCount,
            rawIndentCount
        )
    }
}