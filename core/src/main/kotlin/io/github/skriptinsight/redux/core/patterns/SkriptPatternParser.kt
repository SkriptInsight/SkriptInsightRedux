package io.github.skriptinsight.redux.core.patterns

import io.github.skriptinsight.redux.core.SyntaxFacts
import io.github.skriptinsight.redux.core.parser.SkriptParserUtils
import io.github.skriptinsight.redux.core.patterns.impl.ChoicePatternElement
import io.github.skriptinsight.redux.core.patterns.impl.ComplexSkriptPatternElement
import io.github.skriptinsight.redux.core.patterns.impl.LiteralPatternElement
import io.github.skriptinsight.redux.core.patterns.impl.OptionalPatternElement

object SkriptPatternParser {
    /**
     * Parses a pattern string into a [SkriptPatternElement]
     *
     * @param pattern The pattern string to parse
     * @throws IllegalArgumentException If the pattern string is invalid
     */
    fun parsePattern(pattern: String): SkriptPatternElement {
        val patternElements = mutableListOf<SkriptPatternElement>()
        val literalCache = StringBuilder()
        var index = 0

        fun createLiteralFromCache() {
            if (literalCache.isNotEmpty()) {
                patternElements.add(LiteralPatternElement(literalCache.toString()))
                literalCache.clear()
            }
        }

        while (index < pattern.length) {
            val char = pattern[index]

            fun parseGroup(openBracket: Char, closeBracket: Char, handler: (String) -> SkriptPatternElement): Boolean {
                if (char == openBracket) {
                    createLiteralFromCache()
                    val endIndex = SkriptParserUtils.findNextClosingBracket(
                        pattern, openBracket, closeBracket, index, false
                    )

                    if (endIndex == -1) {
                        throw IllegalArgumentException("No matching '$closeBracket' found for '$openBracket' at index $index")
                    }

                    val content = pattern.substring(index + 1, endIndex)
                    patternElements.add(handler(content))

                    // Jump to the end of the pattern
                    index = endIndex
                    return true
                }
                return false
            }

            var isGroupMatch: Boolean = parseGroup('[', ']') {
                OptionalPatternElement(parsePattern(it))
            }

            isGroupMatch = isGroupMatch or parseGroup('(', ')') {
                ChoicePatternElement(SyntaxFacts.choiceSplitterPattern.split(it).map { parseChoice(it) })
            }

            if (!isGroupMatch) {
                literalCache.append(char)
            }
            index++
        }

        createLiteralFromCache()
        if (patternElements.size == 1) {
            return patternElements.first()
        }
        return ComplexSkriptPatternElement(patternElements)
    }

    private fun parseChoice(it: String): ChoicePatternElement.Choice {
        val split = it.split('¦')
        return if (split.size == 1) {
            ChoicePatternElement.Choice(parsePattern(split[0]), null, null)
        } else {
            val parseMark = kotlin.runCatching { split[0].toInt() }.getOrNull()
            val code = split[1]
            ChoicePatternElement.Choice(parsePattern(code), parseMark, split[0])
        }
    }
}