package io.github.skriptinsight.redux.core.location

/**
 * Represents a zero-indexed position in a character sequence
 * @param lineNumber The zero-indexed line number.
 * @param column The zero-indexed position in the line.
 * @author NickAcPT
 */
data class Position(var lineNumber: Int, var column: Int) {
    operator fun rangeTo(other: Position): Range {
        return Range(this, other)
    }

    fun resolvePosition(string: String): Int {
        return resolvePosition(string.lines())
    }

    private fun resolvePosition(lines: List<String>): Int {
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

    operator fun plus(other: Position): Position {
        return Position(lineNumber + other.lineNumber, column + other.column)
    }

    operator fun minus(other: Position): Position {
        return Position(lineNumber - other.lineNumber, column - other.column)
    }

}