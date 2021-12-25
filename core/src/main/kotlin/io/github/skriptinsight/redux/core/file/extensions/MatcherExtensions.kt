package io.github.skriptinsight.redux.core.file.extensions

import io.github.skriptinsight.redux.core.file.location.Position
import io.github.skriptinsight.redux.core.file.location.Range
import java.util.regex.Matcher

@JvmOverloads
fun Matcher.getGroupRange(id: Int, offsetStart: Int = 0, offsetEnd: Int = 0, lineNumber: Int): Range {
    return Position(lineNumber, offsetStart + start(id))..Position(lineNumber, offsetEnd + end(id))
}

@JvmName("getSimpleGroupRange")
@JvmOverloads
fun Matcher.getGroupRange(id: Int, offset: Int = 0, lineNumber: Int): Range {
    return getGroupRange(id, offsetStart = offset, offsetEnd = offset, lineNumber = lineNumber)
}