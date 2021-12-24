package io.github.skriptinsight.redux.core.patterns.impl

import io.github.skriptinsight.redux.core.patterns.SkriptPatternElement

class ChoicePatternElement(val choices: List<Choice>) : SkriptPatternElement {

    data class Choice(val pattern: SkriptPatternElement, val parseMark: Int?, val parseMarkRaw: String?) {
        override fun toString(): String {
            return "${parseMark?.let { "$it¦" } ?: ""}$pattern"
        }
    }

    override fun toString(): String {
        return "(${choices.joinToString("|")})"
    }
}