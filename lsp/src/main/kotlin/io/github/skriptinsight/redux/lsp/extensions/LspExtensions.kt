package io.github.skriptinsight.redux.lsp.extensions

import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.model.SkriptDiagnostic
import io.github.skriptinsight.redux.core.location.Position as InsightPosition
import io.github.skriptinsight.redux.core.location.Range as InsightRange
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.DiagnosticSeverity as InsightDiagnosticSeverity
import org.eclipse.lsp4j.Diagnostic as LspDiagnostic
import org.eclipse.lsp4j.DiagnosticSeverity as LspDiagnosticSeverity
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

fun InsightDiagnosticSeverity.toLspDiagnosticSeverity(): LspDiagnosticSeverity {
    return when (this) {
        InsightDiagnosticSeverity.ERROR -> LspDiagnosticSeverity.Error
        InsightDiagnosticSeverity.WARNING -> LspDiagnosticSeverity.Warning
        InsightDiagnosticSeverity.INFORMATION -> LspDiagnosticSeverity.Information
        InsightDiagnosticSeverity.HINT -> LspDiagnosticSeverity.Hint
    }
}

fun SkriptDiagnostic.toLspDiagnostic(): LspDiagnostic {
    return LspDiagnostic(
        range.toLspRange(),
        message ?: inspection.name,
        severity.toLspDiagnosticSeverity(),
        "SkriptInsight",
        inspection.id
    )
}