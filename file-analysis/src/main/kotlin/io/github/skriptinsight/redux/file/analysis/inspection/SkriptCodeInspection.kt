package io.github.skriptinsight.redux.file.analysis.inspection

import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.DiagnosticProvider

interface SkriptCodeInspection {
    val id: String
    val name: String
    val description: String

    val diagnosticProviders: List<DiagnosticProvider>
}