package io.github.skriptinsight.redux.core.parser

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