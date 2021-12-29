package io.github.skriptinsight.redux.file.node.impl.skript.function

import io.github.skriptinsight.redux.core.location.Range
import io.github.skriptinsight.redux.core.parser.SkriptParserUtils


data class FunctionSignature(
    val name: String, val nameRange: Range,
    val parameters: List<FunctionParameter>, val parametersRange: Range,
    val returnType: String?, val returnTypeRange: Range?
) {
    companion object {
        fun createFromSectionNode(
            name: String?,
            nameRange: Range?,
            rawParameters: String?,
            rawParametersRange: Range?,
            rawReturnType: String?,
            rawReturnTypeRange: Range?,
            lineNumber: Int,
        ): FunctionSignature? {

            // Invalid Function: doesn't have a name
            if (name == null || nameRange == null) {
                return null
            }

            // Invalid Function: doesn't have parameters
            if (rawParameters == null || rawParametersRange == null) {
                return null
            }
            val initialIndex = rawParametersRange.start

            val parameters = SkriptParserUtils.splitRangesByChar(rawParameters, ',', lineNumber, true).map { range ->
                FunctionParameter.createParameter(rawParameters, range, initialIndex, lineNumber)
            }

            return FunctionSignature(
                name, nameRange,
                parameters, rawParametersRange,
                rawReturnType, rawReturnTypeRange
            )
        }
    }
}
