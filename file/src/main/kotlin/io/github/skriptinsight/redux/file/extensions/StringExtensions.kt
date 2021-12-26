package io.github.skriptinsight.redux.file.extensions

import io.github.skriptinsight.redux.core.location.Position
import io.github.skriptinsight.redux.core.location.Range


/**
 * Returns a substring specified by the given [range] of indices.
 */
fun String.substring(range: Range): String {
    return this.substring(range.start.resolvePosition(this) until range.end.resolvePosition(this))
}

/**
 * Returns a substring of this string that starts at the specified [position] and continues to the end of the string.
 */
fun String.substring(position: Position): String {
    return this.substring(position.resolvePosition(this))
}

