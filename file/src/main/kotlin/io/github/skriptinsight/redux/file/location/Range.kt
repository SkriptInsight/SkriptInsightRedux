package io.github.skriptinsight.redux.file.location

/**
 * Represents a range of zero-indexed positions in a character sequence.
 * (start, end]
 * @param start The starting position.
 * @param end The ending position.
 * @author NickAcPT
 */
data class Range(val start: Position, val end: Position) {

    /**
     * Checks whether [other] [Range] is within this [Range].
     * @return true if [other] [Range] is inside this [Range], false otherwise
     */
    fun contains(other: Range): Boolean {
        return other.start >= start && other.end <= end
    }

    /**
     * Checks whether [other] [Position] is within this [Range].
     * @return true if [other] [Position] is inside this [Range], false otherwise
     */
    fun contains(other: Position): Boolean {
        return other >= start && other <= end
    }
}