package io.github.skriptinsight.redux.file.analysis.inspection.diagnostic

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.model.SkriptDiagnostic
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode

interface DiagnosticProvider {
    fun provideDiagnostic(file: SkriptFile, node: AbstractSkriptNode): Sequence<SkriptDiagnostic>
}
