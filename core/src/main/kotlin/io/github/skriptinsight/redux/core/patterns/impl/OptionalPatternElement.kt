package io.github.skriptinsight.redux.core.patterns.impl

import io.github.skriptinsight.redux.core.patterns.SkriptPatternElement

class OptionalPatternElement(val value: SkriptPatternElement) : SkriptPatternElement {
    override fun toString(): String {
        return "[$value]"
    }
}