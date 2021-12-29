package io.github.skriptinsight.redux.file.workspace.skript

import io.github.skriptinsight.redux.core.SkriptSyntaxFacts
import io.github.skriptinsight.redux.file.extensions.getGroupContentAndRange
import io.github.skriptinsight.redux.file.node.content.NodeContentResult
import io.github.skriptinsight.redux.file.node.impl.SectionSkriptNode
import io.github.skriptinsight.redux.file.node.impl.skript.FunctionSectionSkriptNode
import io.github.skriptinsight.redux.file.node.indentation.NodeIndentationData
import io.github.skriptinsight.redux.file.workspace.providers.SectionParser
import java.util.regex.Pattern

object FunctionSectionParser : SectionParser {
    private val functionRegexPattern = Pattern.compile(SkriptSyntaxFacts.functionRegex)
    private val functionParamsPattern = Pattern.compile(SkriptSyntaxFacts.paramRegex)

    override fun parseSection(
        lineNumber: Int,
        rawContent: String,
        indentations: Array<NodeIndentationData>,
        nodeContentResult: NodeContentResult
    ): SectionSkriptNode? {
        // If the line is not a function definition, return null
        // Function definitions start with "function"
        val content = nodeContentResult.content.trimEnd(':')
        if (!content.startsWith("function")) {
            return null
        }

        // If the line is a function definition, parse it
        val functionMatcher = functionRegexPattern.matcher(content)
        if (!functionMatcher.matches()) {
            return null
        }

        val (functionName, functionRange) = functionMatcher.getGroupContentAndRange("name", 0, lineNumber)
        val (params, paramsRange) = functionMatcher.getGroupContentAndRange("params", 0, lineNumber)
        val (returnType, returnTypeRange) = functionMatcher.getGroupContentAndRange("return", 0, lineNumber)

        return FunctionSectionSkriptNode(
            functionName,
            functionRange,
            params,
            paramsRange,
            returnType,
            returnTypeRange,
            lineNumber, content, indentations, nodeContentResult
        )
    }
}