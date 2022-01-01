package io.github.skriptinsight.redux.file.node.impl.skript.function

import io.github.skriptinsight.redux.core.SkriptSyntaxFacts
import io.github.skriptinsight.redux.core.location.Position
import io.github.skriptinsight.redux.core.location.Range
import io.github.skriptinsight.redux.file.extensions.getGroupContentAndRange
import io.github.skriptinsight.redux.file.extensions.substring
import java.util.regex.Pattern

data class FunctionParameter private constructor(
    val fullRange: Range,
    val name: String?,
    val type: String?,
    val nameRange: Range?,
    val typeRange: Range?,
    val defaultValue: String? = null,
    val defaultValueRange: Range? = null
) {
    val isValid: Boolean
        get() = name != null && type != null

    companion object {
        private val functionParamsPattern = Pattern.compile(SkriptSyntaxFacts.paramRegex)

        fun createParameter(rawParameters: String, range: Range, initialIndex: Position, lineNumber: Int): FunctionParameter {
            val currentRawParameter = rawParameters.substring(range)
            val matcher = functionParamsPattern.matcher(currentRawParameter)
            val parameterRange = range.plus(initialIndex.copy(lineNumber = 0))

            // If the parameter doesn't match the parameter regex, then the parameter is invalid
            if (!matcher.matches()) {
                return FunctionParameter(parameterRange, null, null, null, null)
            }

            val (name, nameRange) = matcher.getGroupContentAndRange(1, 0, lineNumber)
            val (type, typeRange) = matcher.getGroupContentAndRange(2, 0, lineNumber)
            val (defaultValue, defaultValueRange) = matcher.getGroupContentAndRange(3, 0, lineNumber)

            // If name or type is null, then the parameter is invalid
            if (name == null || type == null) {
                return FunctionParameter(parameterRange, null, null, null, null)
            }

            return FunctionParameter(
                parameterRange,
                name,
                type,
                nameRange?.plus(initialIndex)?.plus(range.start),
                typeRange?.plus(initialIndex)?.plus(range.start),
                defaultValue,
                defaultValueRange?.plus(initialIndex)?.plus(range.start)
            )
        }
    }

    override fun toString(): String {
        if (!isValid) return "<invalid parameter>"
        return if (defaultValue == null) {
            "$name: $type"
        } else {
            "$name: $type = $defaultValue"
        }
    }
}