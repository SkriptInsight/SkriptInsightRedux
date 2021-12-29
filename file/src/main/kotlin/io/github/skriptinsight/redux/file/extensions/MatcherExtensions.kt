package io.github.skriptinsight.redux.file.extensions

import io.github.skriptinsight.redux.core.location.Position
import io.github.skriptinsight.redux.core.location.Range
import java.util.regex.Matcher

@JvmOverloads
fun Matcher.getGroupRange(id: Int, offsetStart: Int = 0, offsetEnd: Int = 0, lineNumber: Int): Range {
    return Position(lineNumber, offsetStart + start(id))..Position(lineNumber, offsetEnd + end(id))
}

@JvmOverloads
fun Matcher.getGroupRange(id: String, offsetStart: Int = 0, offsetEnd: Int = 0, lineNumber: Int): Range {
    return Position(lineNumber, offsetStart + start(id))..Position(lineNumber, offsetEnd + end(id))
}

@JvmName("getSimpleGroupRange")
@JvmOverloads
fun Matcher.getGroupRange(id: Int, offset: Int = 0, lineNumber: Int): Range? {
    return getGroupRange(
        id,
        offsetStart = offset,
        offsetEnd = offset,
        lineNumber = lineNumber
    ).takeUnless { it.start.column == -1 || it.end.column == -1 }
}

@JvmName("getSimpleGroupRange")
@JvmOverloads
fun Matcher.getGroupRange(id: String, offset: Int = 0, lineNumber: Int): Range {
    return getGroupRange(id, offsetStart = offset, offsetEnd = offset, lineNumber = lineNumber)
}

fun Matcher.getGroupContentAndRange(id: String, offset: Int, lineNumber: Int): Pair<String?, Range?> {
    return Pair(group(id), getGroupRange(id, offset, lineNumber))
}

fun Matcher.getGroupContentAndRange(id: Int, offset: Int, lineNumber: Int): Pair<String?, Range?> {
    return Pair(group(id), getGroupRange(id, offset, lineNumber))
}