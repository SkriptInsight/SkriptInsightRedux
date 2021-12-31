package io.github.skriptinsight.redux.lsp.extensions

import io.github.skriptinsight.redux.core.location.Position as InsightPosition
import io.github.skriptinsight.redux.core.location.Range as InsightRange
import org.eclipse.lsp4j.Position as LspPosition
import org.eclipse.lsp4j.Range as LspRange

fun InsightRange.toLspRange(): LspRange {
    return LspRange(
        start.toLspPosition(),
        end.toLspPosition()
    )
}

fun InsightPosition.toLspPosition(): LspPosition {
    return LspPosition(lineNumber, column)
}

fun LspPosition.toInsightPosition(): InsightPosition {
    return InsightPosition(line, character)
}

fun LspRange.toInsightRange(): InsightRange {
    return InsightRange(
        start.toInsightPosition(),
        end.toInsightPosition()
    )
}