package io.github.skriptinsight.redux.analysis.diagnostic

import io.github.skriptinsight.redux.core.location.Range

data class SkriptDiagnostic(
    val message: String,
    val type: DiagnosticType,
    val range: Range
)

