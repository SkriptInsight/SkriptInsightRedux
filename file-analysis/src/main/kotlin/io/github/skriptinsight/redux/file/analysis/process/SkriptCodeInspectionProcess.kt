package io.github.skriptinsight.redux.file.analysis.process

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.model.SkriptDiagnostic
import io.github.skriptinsight.redux.file.analysis.managers.InspectionManager
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.work.SkriptFileProcess

class SkriptCodeInspectionProcess : SkriptFileProcess<List<SkriptDiagnostic>>(
    "Analyzing code", "Running code inspections on the file"
) {
    override fun doWork(
        file: SkriptFile, lineNumber: Int, rawContent: String, context: AbstractSkriptNode
    ): List<SkriptDiagnostic> {
        return InspectionManager.inspections.flatMap { inspection -> inspection.diagnosticProviders.flatMap { it.provideDiagnostic(file, context) } }
    }
}