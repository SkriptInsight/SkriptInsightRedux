package io.github.skriptinsight.redux.core.patterns.impl

import io.github.skriptinsight.redux.core.patterns.SkriptPatternElement

class LiteralPatternElement(val literal: String) : SkriptPatternElement {
    companion object {
        private val literalReplacementRegex = Regex("([()\\[\\]])")
    }

    override fun toString(): String {
        return literalReplacementRegex.replace(literal, "\\$1")
    }
}