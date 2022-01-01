package io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.model

import io.github.skriptinsight.redux.core.location.Range
import io.github.skriptinsight.redux.file.analysis.inspection.SkriptCodeInspection
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.DiagnosticSeverity

/**
 * Represents a [SkriptDiagnostic] with the given [message] and [severity].
 *
 * @param range The range of the diagnostic.
 * @param severity the severity of the diagnostic.
 * @param message the message of the diagnostic.
 */
data class SkriptDiagnostic(
    val inspection: SkriptCodeInspection,
    val range: Range, val severity: DiagnosticSeverity, val message: String? = null
)