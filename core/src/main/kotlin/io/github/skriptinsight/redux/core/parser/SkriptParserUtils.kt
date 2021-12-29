package io.github.skriptinsight.redux.core.parser

import io.github.skriptinsight.redux.core.location.Position
import io.github.skriptinsight.redux.core.location.Range

object SkriptParserUtils {

    fun findNextNotEscaped(code: String, toFind: Char, startIndex: Int = 0): Int {
        var index = startIndex
        while (index < code.length) {
            if (code[index] == toFind) {
                return index
            }
            if (code[index] == '\\') {
                index += 2
            } else {
                index++
            }
        }
        return -1
    }

    fun splitRangesByChar(
        code: String,
        toSplit: Char,
        lineNumber: Int,
        trimStart: Boolean = false,
        offset: Position = Position(0, 0)
    ): MutableList<Range> {
        fun range(start: Int, end: Int): Range {
            return Range(
                Position(lineNumber, start) + offset,
                Position(lineNumber, end) + offset
            )
        }

        var index = 0
        var lastSplitIndex = 0
        val splits = mutableListOf<Range>()

        while (index < code.length) {
            if (code[index] == '"') {
                if (code[index - 1] == '\\') {
                    index += 2
                } else {
                    val closingQuoteIndex = findNextNotEscaped(code, '"', index + 1)
                    if (closingQuoteIndex == -1) {
                        throw IllegalStateException("Unclosed quote")
                    }
                    index = closingQuoteIndex + 1
                }
            } else if (code[index] == toSplit) {
                if (code[index - 1] == '\\') {
                    index += 2
                } else {
                    if (trimStart) lastSplitIndex += countStartingSpaces(code, lastSplitIndex, index)

                    splits.add(range(lastSplitIndex, index))
                    lastSplitIndex = index + 1
                    index++
                }
            } else {
                index++
            }
        }
        if (lastSplitIndex < code.length) {
            if (trimStart) lastSplitIndex += countStartingSpaces(code, lastSplitIndex, index)
            splits.add(range(lastSplitIndex, code.length))
        }

        return splits
    }

    private fun countStartingSpaces(
        code: String,
        startIndex: Int,
        endIndex: Int,
    ): Int {
        code.substring(startIndex, endIndex).let { sequence ->
            return sequence.takeWhile { it.isWhitespace() }.count()
        }
    }

    fun findNextNotInsideBracket(
        code: String,
        toFind: Char,
        openBracket: Char,
        closeBracket: Char,
        startIndex: Int
    ): Int {
        var index = startIndex
        while (index < code.length) {
            val c = code[index]
            if (c == openBracket) {
                index = findNextClosingBracket(code, openBracket, closeBracket, index + 1, true)
            } else if (c == toFind) {
                return index
            }
            index++
        }
        return -1
    }

    fun findNextClosingBracket(
        code: String,
        openingChar: Char,
        closingChar: Char,
        startIndex: Int,
        isInsideGroup: Boolean
    ): Int {
        var counter = if (isInsideGroup) 1 else 0
        var index = startIndex
        while (index < code.length) {
            when (code[index]) {
                '\\' -> {
                    index += 2
                }
                openingChar -> {
                    counter++
                    index++
                }
                closingChar -> {
                    counter--
                    if (counter == 0) {
                        return index
                    }
                    index++
                }
                else -> {
                    index++
                }
            }
        }
        return -1
    }
}