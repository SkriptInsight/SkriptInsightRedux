package io.github.skriptinsight.redux.lsp.extensions

import java.util.concurrent.ConcurrentMap

fun <N> ConcurrentMap<Int, N>.shiftRangeRight(startRange: Int, count: Int, amount: Int, update: (N) -> Unit) {
    val endRange = startRange + count
    val range = endRange - amount downTo startRange

    // Go through the range specified by the start and end range
    for (i in range) {
        if (i > (endRange + amount)) {
            // If the index is greater than the end range amount, then we need to stop the loop
            break
        }
        // Get the value at the index
        val oldValue = this[i] ?: continue
        // Update the value at the index
        oldValue.apply(update)
        // Properly shift the value
        this[i + amount] = oldValue
    }
}