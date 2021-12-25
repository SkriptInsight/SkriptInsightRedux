package io.github.skriptinsight.redux.core.patterns

import io.github.skriptinsight.redux.core.SyntaxFacts
import io.github.skriptinsight.redux.core.parser.SkriptParserUtils
import io.github.skriptinsight.redux.core.patterns.impl.*
import java.util.*
import java.util.regex.Pattern

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
                    val endIndex = if (openBracket == closeBracket) SkriptParserUtils.findNextNotEscaped(
                        pattern,
                        openBracket,
                        index + 1
                    ) else SkriptParserUtils.findNextClosingBracket(
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

            isGroupMatch = isGroupMatch or parseGroup('<', '>') {
                RegexPatternElement(runCatching { Pattern.compile(it) }.getOrNull(), it)
            }

            isGroupMatch = isGroupMatch or parseGroup('%', '%') {
                val flagValues = TypePatternElement.Flags.values()
                val flagChars = it.takeWhile { c -> flagValues.any { fc -> fc.character == c } }
                val flags = flagChars.toCharArray().map { c -> flagValues.first { f -> f.character == c } }
                val flagSet =
                    if (flags.isNotEmpty()) EnumSet.copyOf(flags) else EnumSet.noneOf(TypePatternElement.Flags::class.java)
                TypePatternElement(it.drop(flagChars.length).split("/"), flagSet)
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