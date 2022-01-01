package io.github.skriptinsight.redux.file.analysis.inspection.impl

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.analysis.inspection.SkriptCodeInspection
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.DiagnosticProvider
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.DiagnosticSeverity
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.model.SkriptDiagnostic
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.node.impl.skript.function.FunctionSectionSkriptNode
import java.util.*

object InvalidFunctionParameterInspection : SkriptCodeInspection, DiagnosticProvider {
    override val id: String = "invalid-function-parameter"
    override val name: String = "Invalid function parameter"
    override val description: String = "Reports invalid function parameters"

    override val diagnosticProviders: List<DiagnosticProvider>
        get() = Collections.singletonList(this)

    override fun provideDiagnostic(file: SkriptFile, node: AbstractSkriptNode): Sequence<SkriptDiagnostic> = sequence {
        if (node is FunctionSectionSkriptNode) {
            node.signature?.parameters?.forEach { parameter ->
                if (!parameter.isValid) {
                    yield(
                        SkriptDiagnostic(
                            this@InvalidFunctionParameterInspection,
                            parameter.fullRange,
                            DiagnosticSeverity.ERROR
                        )
                    )
                }
            }
        }
    }


}