package io.github.skriptinsight.redux.core.patterns.impl

import io.github.skriptinsight.redux.core.patterns.SkriptPatternElement
import java.util.*

class TypePatternElement(val types: List<String>, val flags: EnumSet<Flags>) : SkriptPatternElement {
    enum class Flags(val character: Char) {
        NULL_VALUE_WHEN_EMITTED('-'),
        LITERALS_ONLY('*'),
        NO_LITERALS('~'),
        VARIABLES_ONLY('^'),
        ALLOW_CONDITIONAL_EXPRESSIONS('='),
    }

    fun hasFlag(flag: Flags): Boolean {
        return flags.contains(flag)
    }


    override fun toString(): String {
        return "$flagsAsString%${types.joinToString("/")}%"
    }

    private val flagsAsString get() = flags.toList().sorted().joinToString("") { it.character.toString() }
}