package io.github.skriptinsight.redux.file.analysis.managers

import io.github.skriptinsight.redux.core.event.post
import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.analysis.event.SkriptFileInspectionResultsEvent
import io.github.skriptinsight.redux.file.analysis.inspection.SkriptCodeInspection
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.model.SkriptDiagnostic
import io.github.skriptinsight.redux.file.analysis.inspection.impl.InvalidFunctionParameterInspection
import io.github.skriptinsight.redux.file.analysis.process.SkriptCodeInspectionProcess

object InspectionManager {
    val inspections = mutableListOf<SkriptCodeInspection>()

    init {
        registerInspection(InvalidFunctionParameterInspection)
    }

    fun registerInspection(inspection: SkriptCodeInspection) {
        inspections.add(inspection)
    }

    fun inspectFile(file: SkriptFile) = inspectFile(file, 0, file.maxLineNumber)

    fun inspectFile(file: SkriptFile, startLine: Int, endLine: Int): List<SkriptDiagnostic> {
        val diagnostics = file.runProcess(SkriptCodeInspectionProcess(), startLine, endLine).flatten()
        file.workspace.logger.info("Inspected file ${file.url} with ${diagnostics.size} diagnostics")
        SkriptFileInspectionResultsEvent(file, file.workspace, diagnostics).post()

        return diagnostics
    }
}