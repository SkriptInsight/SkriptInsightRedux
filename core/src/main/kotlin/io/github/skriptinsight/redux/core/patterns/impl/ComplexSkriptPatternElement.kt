package io.github.skriptinsight.redux.core.patterns.impl

import io.github.skriptinsight.redux.core.patterns.SkriptPatternElement

class ComplexSkriptPatternElement(val elements: List<SkriptPatternElement>) : SkriptPatternElement {
    override fun toString(): String {
        return elements.joinToString("")
    }
}