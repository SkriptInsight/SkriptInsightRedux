package io.github.skriptinsight.redux.core.patterns.impl

import io.github.skriptinsight.redux.core.patterns.SkriptPatternElement
import java.util.regex.Pattern

data class RegexPatternElement(val pattern: Pattern) : SkriptPatternElement {
    override fun toString(): String = "<$pattern>"
}