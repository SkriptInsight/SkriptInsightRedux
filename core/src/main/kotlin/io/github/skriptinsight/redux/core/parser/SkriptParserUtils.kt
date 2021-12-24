package io.github.skriptinsight.redux.core.parser

object SkriptParserUtils {

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
        return code.length
    }
}