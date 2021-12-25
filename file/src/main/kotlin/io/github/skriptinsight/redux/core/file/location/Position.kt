package io.github.skriptinsight.redux.core.file.location

/**
 * Represents a zero-indexed position in a character sequence
 * @param lineNumber The zero-indexed line number.
 * @param column The zero-indexed position in the line.
 * @author NickAcPT
 */
data class Position(val lineNumber: Int, val column: Int) {
    operator fun rangeTo(other: Position): Range {
        return Range(this, other)
    }

    fun resolvePosition(string: String): Int {
        return resolvePosition(string.lines())
    }

    fun resolvePosition(lines: List<String>): Int {
        var targetLine = lineNumber

        if (lines.size < targetLine)
            targetLine = lines.size - 1

        val lineBreakSize = System.lineSeparator().length
        var charsUntilLine = 0

        for (i in lines.indices) {
            val line = lines[i]

            if (i < targetLine) {
                charsUntilLine += line.length + lineBreakSize
            } else {
                charsUntilLine += column
                break
            }
        }

        return charsUntilLine
    }

    operator fun compareTo(other: Position): Int {
        if (this == other) return 0
        val lineComparison = lineNumber.compareTo(other.lineNumber)
        return if (lineComparison != 0) lineComparison else column.compareTo(other.column)
    }
}

fun String.substring(position: Position): String {
    return this.substring(position.resolvePosition(this))
}

